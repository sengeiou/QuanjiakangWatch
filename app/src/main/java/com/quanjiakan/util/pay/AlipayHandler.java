package com.quanjiakan.util.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.base.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.IHttpUrlConstants;
import com.quanjiakan.net.Retrofit2Util;
import com.quanjiakan.net.retrofit.service.RxGetAliPayStringService;
import com.quanjiakan.net_presenter.IPresenterBusinessCode;
import com.quanjiakan.util.common.LogUtil;
import com.quanjiakan.util.common.ParseToGsonUtil;
import com.quanjiakan.watch.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlipayHandler {

    private static final int SDK_PAY_FLAG = 1;

    public static final int PAID_SUCCESS = 1;

    public static final int PAID_FAILED = 0;

    private BaseActivity context;

    private AlipayCallback callback;

    public AlipayHandler(BaseActivity context, AlipayCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void prePay(final String title, final String body, final double money, String orderId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("body", body);
        params.put("total_fee", money + "");
        params.put("subject", title);
        params.put("orderid", orderId);

        //
        Retrofit retrofit = Retrofit2Util.getRetrofitStringResult(IHttpUrlConstants.BASEURL_STATIC);
        RxGetAliPayStringService rxGetRequest = retrofit.create(RxGetAliPayStringService.class);
        rxGetRequest.doPostAliPayInfoString(
                params.get(IParamsName.PARAMS_COMMON_TOKEN),
                params.get(IParamsName.PARAMS_COMMON_USERID)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        context.dismissMyDialog(IPresenterBusinessCode.ALI_PAY);
                    }

                    @Override
                    public void onError(Throwable e) {
                        context.dismissMyDialog(IPresenterBusinessCode.ALI_PAY);
                        context.onError(IPresenterBusinessCode.ALI_PAY, 200, e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        LogUtil.e("ALI_PAY" + response);
                        context.dismissMyDialog(IPresenterBusinessCode.ALI_PAY);
                        JsonObject object = new ParseToGsonUtil(response).getJsonObject();
                        if (object.has("code") && object.get("code").getAsString().equals("200")) {
                            pay(object);
                        } else {
                            context.onError(IPresenterBusinessCode.ALI_PAY, 200, context.getString(R.string.error_ali_create_order_error));
                        }
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    JsonObject object = (JsonObject) msg.obj;
                    PayResult payResult = new PayResult((String) object.get("result").getAsString());
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    System.out.print(resultInfo);
                    if (TextUtils.equals(resultStatus, ICommonData.ALI_PAY_RESULT_9000)) {
                        callback.paidComplete(PAID_SUCCESS, context.getString(R.string.hint_ali_pay_result_9000), object + "");
                    } else {
                        if (TextUtils.equals(resultStatus, ICommonData.ALI_PAY_RESULT_8000)) {
                            callback.paidComplete(PAID_FAILED, context.getString(R.string.hint_ali_pay_result_8000), "");
                        } else {
                            callback.paidComplete(PAID_FAILED, context.getString(R.string.hint_ali_pay_result_other), "");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public void pay(final JsonObject paymentInfo) {
        final String orderInfo = getOrderInfo(paymentInfo.get("subject").getAsString(),
                paymentInfo.get("body").getAsString(),
                paymentInfo.get("total_fee").getAsString(),
                paymentInfo.get("out_trade_no").getAsString(),
                paymentInfo);
        String sign = paymentInfo.get("sign").getAsString();
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(paymentInfo.get("sign").getAsString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType("RSA");
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                JsonObject object = new JsonObject();
                object.addProperty("result", result);
                object.addProperty("orderid", paymentInfo.get("out_trade_no").getAsString());
                msg.obj = object;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
//		Thread payThread = new Thread(payRunnable);
//		payThread.start();
        //TODO 使用线程池执行调用
        BaseApplication.getInstances().addThreadTask(payRunnable);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price, String orderId, JsonObject object) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + object.get("partner").getAsString() + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + object.get("seller_id").getAsString() + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + object.get("out_trade_no").getAsString() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + object.get("subject").getAsString() + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + object.get("body").getAsString() + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + object.get("total_fee").getAsString() + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + object.get("notify_url").getAsString() + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType(String signtype) {
        return "sign_type=\"" + signtype + "\"";
    }

//	private String sign(String content) {
//		return SignUtils.sign(content, RSA_PRIVATE);
//	}

//	public void h5pay(){
//		Intent intent = new Intent(context, H5PayDemoActivity.class);
//		Bundle extras = new Bundle();		/**
//		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//		 * 商户可以根据自己的需求来实现
//		 */
//		// url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//		extras.putString("url", "http://m.meituan.com");
//		intent.putExtras(extras);
//		((Activity)context).startActivity(intent);
//	}

    public interface AlipayCallback {
        public void paidComplete(int type, String msg, String tradeId);
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(key).append("=").append("\"").append(value).append("\"");
        }
        return builder.toString();
    }

}
