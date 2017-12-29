package com.quanjiakan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pingantong.main.R;
import com.quanjiakan.activity.common.image.ImageViewerActivity;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.entity.HouseKeeperOrderDetailEntity;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/7.
 */

public class HouseKeeperOrderListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HouseKeeperOrderDetailEntity> mData;

    public HouseKeeperOrderListAdapter(Context context, ArrayList<HouseKeeperOrderDetailEntity> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setmData(ArrayList<HouseKeeperOrderDetailEntity> mData) {
        this.mData = mData;
    }

    public ArrayList<HouseKeeperOrderDetailEntity> getmData() {
        return mData;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_housekeeper_order, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder,position);
        return convertView;
    }

    protected void bindData(final ViewHolder holder,final int pos){
        try{
            final HouseKeeperOrderDetailEntity object = mData.get(pos);
            holder.tvBaomu.setText("广州市巨硅信息科技有限公司");
            holder.tvName.setText(object.getHousekeeperName());
            holder.tvPrice.setText(object.getPrice()+"元/月");
            holder.tvHouseCompany.setText("从业"+object.getExperience()+"年");
            Picasso.with(context).load(object.getImage()).transform(new CircleTransformation()).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(object.getImage()!=null && object.getImage().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
                        Intent intent = new Intent(context, ImageViewerActivity.class);
                        intent.putExtra(IParamsName.PARAMS_URL,object.getImage());
                        context.startActivity(intent);
                    }else{
                        CommonDialogHint.getInstance().showHint(context,context.getString(R.string.hint_house_keeper_list_no_headimg));
                    }
                }
            });
            holder.tvBookDateValue.setText(getDate(object.getBegindate(), object.getEnddate()));
            holder.tvOrderId.setText(object.getOrderid());
            int status = Integer.parseInt(object.getStatus());
            if(status == 1){
                //未付款
                holder.tvStatus.setText("未付款");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvCancel.setVisibility(View.GONE);
            }else if (status == 2) {
                //已付款
                holder.tvStatus.setText("已付款");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.maincolor));
                holder.tvCancel.setVisibility(View.GONE);
            }else{
                //未付款
                holder.tvStatus.setText("未付款");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                holder.tvCancel.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected String getDate(String begin, String end){
        String begin_date = begin.substring(5, 10);
        String end_date = end.substring(5, 10);
        return begin_date + " 到 " + end_date;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_baomu)
        TextView tvBaomu;
        @BindView(R.id.tv_book_date_value)
        TextView tvBookDateValue;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.layout_tags)
        RelativeLayout layoutTags;
        @BindView(R.id.tv_house_company)
        TextView tvHouseCompany;
        @BindView(R.id.tv_line)
        TextView tvLine;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_order_id)
        TextView tvOrderId;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
