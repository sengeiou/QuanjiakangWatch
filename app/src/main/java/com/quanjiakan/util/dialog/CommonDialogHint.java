package com.quanjiakan.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.util.common.UnitExchangeUtil;

/**
 * Created by Administrator on 2017/10/10.
 */

public class CommonDialogHint {
    /**
     *
     */
    public enum POSITION{
        CENTER,BOTTOM,TOP
    }
    private static CommonDialogHint instances;
    private CommonDialogHint(){

    }
    public static CommonDialogHint getInstance(){
        if(instances==null){
            instances = new CommonDialogHint();
        }
        return instances;
    }

    private Dialog dialog;


    /**
     ******************************************************************************************************************************************************************
     *
     * 如果存在多个Dialog显示调用，当第一个没通过交互消失时，后续调用处理将被忽略
     *
     */

    /**
     * 默认处于中间位置
     * @param context
     * @param hintMsg
     */
    public void showHint(Context context,String hintMsg){
        if(dialog!=null && dialog.isShowing()){
            /**
             * 已经存在的Dialog若不进行处理，则不处理后续的显示Dialog请求？
             */
            return;
        }
        dialog = new Dialog(context,R.style.AlbumDialogStyle);
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm_version2,null);
        /**
         * 设置指定的提示信息
         */
        TextView hintMsgContainer = (TextView) root.findViewById(R.id.tv_content);
        hintMsgContainer.setText(hintMsg);

        /**
         *  设置点击
         */
        RelativeLayout btn = (RelativeLayout) root.findViewById(R.id.rl_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        /**
         * 设置Dialog的相关属性
         */
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 300);
        lp.height = lp.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(root, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showHint(Context context, String hintMsg,final View.OnClickListener clickListener){
        if(dialog!=null && dialog.isShowing()){
            /**
             * 已经存在的Dialog若不进行处理，则不处理后续的显示Dialog请求？
             */
            return;
        }
        dialog = new Dialog(context,R.style.AlbumDialogStyle);
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm_version2,null);
        /**
         * 设置指定的提示信息
         */
        TextView hintMsgContainer = (TextView) root.findViewById(R.id.tv_content);
        hintMsgContainer.setText(hintMsg);

        /**
         *  设置点击
         */
        RelativeLayout btn = (RelativeLayout) root.findViewById(R.id.rl_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
                if(clickListener!=null){
                    clickListener.onClick(view);
                }
            }
        });
        /**
         * 设置Dialog的相关属性
         */
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 300);
        lp.height = lp.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(root, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     *
     * @param context
     * @param hintMsg
     * @param position
     */
    public void showHint(Context context,String hintMsg,POSITION position){
        if(dialog!=null && dialog.isShowing()){
            /**
             * 已经存在的Dialog若不进行处理，则不处理后续的显示Dialog请求？
             */
            return;
        }
        dialog = new Dialog(context,R.style.AlbumDialogStyle);
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm_version2,null);
        /**
         * 设置指定的提示信息
         */
        TextView hintMsgContainer = (TextView) root.findViewById(R.id.tv_content);
        hintMsgContainer.setText(hintMsg);

        /**
         *  设置点击
         */
        RelativeLayout btn = (RelativeLayout) root.findViewById(R.id.rl_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        /**
         * 设置Dialog的相关属性
         */
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 300);
        lp.height = lp.WRAP_CONTENT;
        switch (position){
            case CENTER:
                lp.gravity = Gravity.CENTER;
                break;
            case TOP:
                lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                break;
            case BOTTOM:
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
            default:
                lp.gravity = Gravity.CENTER;
                break;
        }
        dialog.setContentView(root, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 指定提示信息，位置，分割线的颜色
     *
     * @param context
     * @param hintMsg
     * @param position
     * @param colorInReource
     */
    public void showHint(Context context,String hintMsg,POSITION position,int colorInReource){
        if(dialog!=null && dialog.isShowing()){
            /**
             * 已经存在的Dialog若不进行处理，则不处理后续的显示Dialog请求？
             */
            return;
        }
        dialog = new Dialog(context,R.style.AlbumDialogStyle);
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm_version2,null);
        /**
         * 设置指定的提示信息
         */
        TextView hintMsgContainer = (TextView) root.findViewById(R.id.tv_content);
        hintMsgContainer.setText(hintMsg);

        /**
         * 设置分隔线的颜色
         */
        View divider = root.findViewById(R.id.divider);
        divider.setBackgroundColor(colorInReource);
        /**
         *  设置点击
         */
        RelativeLayout btn = (RelativeLayout) root.findViewById(R.id.rl_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        /**
         * 设置Dialog的相关属性
         */
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 300);
        lp.height = lp.WRAP_CONTENT;
        switch (position){
            case CENTER:
                lp.gravity = Gravity.CENTER;
                break;
            case TOP:
                lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                break;
            case BOTTOM:
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
            default:
                lp.gravity = Gravity.CENTER;
                break;
        }
        dialog.setContentView(root, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     ******************************************************************************************************************************************************************
     */

    /**
     * 这个不进行处理，每次调用都重新生成一个dialog
     *
     * @param context
     * @param hintMsg
     * @param position
     * @param colorInReource
     */
    public void showHintOneDialog(Context context,String hintMsg,POSITION position,int colorInReource){
        final Dialog dialog = new Dialog(context,R.style.AlbumDialogStyle);
        View root = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm_version2,null);
        /**
         * 设置指定的提示信息
         */
        TextView hintMsgContainer = (TextView) root.findViewById(R.id.tv_content);
        hintMsgContainer.setText(hintMsg);

        /**
         * 设置分隔线的颜色
         */
        View divider = root.findViewById(R.id.divider);
        divider.setBackgroundColor(colorInReource);
        /**
         *  设置点击
         */
        RelativeLayout btn = (RelativeLayout) root.findViewById(R.id.rl_confirm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        /**
         * 设置Dialog的相关属性
         */
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = UnitExchangeUtil.dip2px(context, 300);
        lp.height = lp.WRAP_CONTENT;
        switch (position){
            case CENTER:
                lp.gravity = Gravity.CENTER;
                break;
            case TOP:
                lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                break;
            case BOTTOM:
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                break;
            default:
                lp.gravity = Gravity.CENTER;
                break;
        }
        dialog.setContentView(root, lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
