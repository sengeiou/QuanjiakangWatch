package com.quanjiakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.db.entity.BindWatchInfoEntity;
import com.quanjiakan.util.common.UnitExchangeUtil;
import com.quanjiakan.view.MaterialBadgeTextView;
import com.quanjiakan.view.roundimage.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class DeviceContainerAdapter extends BaseAdapter {

    private List<BindWatchInfoEntity> devices;
    private Context mContext;

    private int selectedPosition;

    public DeviceContainerAdapter(Context context, List<BindWatchInfoEntity> data) {
        this.mContext = context;
        this.devices = data;
        selectedPosition = 0;
    }

    public void setDevices(List<BindWatchInfoEntity> data) {
        this.devices = data;
    }


    public List<BindWatchInfoEntity> getDevices() {
        return devices;
    }

    @Override
    public int getCount() {
        if (devices != null) {
            return devices.size();
        }
        return 0;
    }

    //TODO 标记当前选中的子项，突出显示
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_device_container, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //TODO 设置选中大小,以及位置
        if (position == selectedPosition) {
            setSelected(holder);
        } else {
            setUnSelected(holder);
        }

        //TODO 设置未读
        showVoiceNotice(holder, position);

        //TODO 判断是否为在线设备
        if("1".equals(devices.get(position).getOnline())){
            //TODO 设备在线
            holder.nameOnline.setVisibility(View.VISIBLE);
            holder.nameOnlineBg.setVisibility(View.VISIBLE);
            holder.nameNotOnline.setVisibility(View.GONE);
            //TODO 设置名字--需要根据是否在线进行变化
            if (devices.get(position).getName() != null && devices.get(position).getName().contains(ICommonData.ENCODE_DECODE_URL_PREFIX)) {
                try {
                    holder.nameOnline.setText(URLDecoder.decode(devices.get(position).getName(), ICommonData.ENCODE_DECODE_UTF_8));
                } catch (/*UnsupportedEncoding*/Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.nameOnline.setText(devices.get(position).getName());
            }

            holder.clickEffectView.setBackgroundResource(R.drawable.selector_map_watchlist_online_click_effect);
        }else{
            //TODO 设备不在线，设置不显示
            holder.nameOnline.setVisibility(View.GONE);
            holder.nameOnlineBg.setVisibility(View.GONE);
            holder.nameNotOnline.setVisibility(View.VISIBLE);
            //TODO 设置名字--需要根据是否在线进行变化
            if (devices.get(position).getName() != null && devices.get(position).getName().contains(ICommonData.ENCODE_DECODE_URL_PREFIX)) {
                try {
                    holder.nameNotOnline.setText(URLDecoder.decode(devices.get(position).getName(), ICommonData.ENCODE_DECODE_UTF_8));
                } catch (/*UnsupportedEncoding*/Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.nameNotOnline.setText(devices.get(position).getName());
            }

            holder.clickEffectView.setBackgroundResource(R.drawable.selector_map_watchlist_not_online_click_effect);
        }

        //TODO 显示头像---统一的
        if (devices != null && devices.get(position).getHeadImage() != null && devices.get(position).getHeadImage().toLowerCase().startsWith("http")) {
            Picasso.with(mContext).load(devices.get(position).getHeadImage()).placeholder(R.drawable.ic_launcher).into(holder.deviceImage);
        } else {
            if (ICommonData.DEVICE_TYPE_CHILD.equals(devices.get(position).getType())) {//TODO 儿童
                Picasso.with(mContext).load(R.drawable.device_type_holder_children).into(holder.deviceImage);
            } else if (ICommonData.DEVICE_TYPE_OLD.equals(devices.get(position).getType())) {
                Picasso.with(mContext).load(R.drawable.device_type_holder_old).into(holder.deviceImage);
            } else {
                Picasso.with(mContext).load(R.drawable.device_type_holder_old).into(holder.deviceImage);
            }
        }
        return convertView;
    }

    //TODO
    private void setSelected(ViewHolder holder){
        //TODO 设置头像位置，大小
        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.deviceImage.getLayoutParams();
        imageParams.width = UnitExchangeUtil.dip2px(mContext,60);
        imageParams.height = UnitExchangeUtil.dip2px(mContext,60);
        imageParams.setMargins(UnitExchangeUtil.dip2px(mContext,5),UnitExchangeUtil.dip2px(mContext,5),
                UnitExchangeUtil.dip2px(mContext,5),UnitExchangeUtil.dip2px(mContext,5));
        holder.deviceImage.setLayoutParams(imageParams);

        //TODO 设置未读消息的位置
        RelativeLayout.LayoutParams unreadParams = (RelativeLayout.LayoutParams) holder.unreadMsgNotice.getLayoutParams();
        unreadParams.width = UnitExchangeUtil.dip2px(mContext,5);
        unreadParams.height = UnitExchangeUtil.dip2px(mContext,5);
        unreadParams.setMargins(0,UnitExchangeUtil.dip2px(mContext,4),UnitExchangeUtil.dip2px(mContext,4),0);
        holder.unreadMsgNotice.setLayoutParams(unreadParams);
    }

    private void setUnSelected(ViewHolder holder){
        //TODO 设置头像位置，大小
        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.deviceImage.getLayoutParams();
        imageParams.width = UnitExchangeUtil.dip2px(mContext,50);
        imageParams.height = UnitExchangeUtil.dip2px(mContext,50);
        imageParams.setMargins(UnitExchangeUtil.dip2px(mContext,10),UnitExchangeUtil.dip2px(mContext,10),
                UnitExchangeUtil.dip2px(mContext,10),UnitExchangeUtil.dip2px(mContext,10));
        holder.deviceImage.setLayoutParams(imageParams);

        //TODO 设置未读消息的位置
        RelativeLayout.LayoutParams unreadParams = (RelativeLayout.LayoutParams) holder.unreadMsgNotice.getLayoutParams();
        unreadParams.width = UnitExchangeUtil.dip2px(mContext,5);
        unreadParams.height = UnitExchangeUtil.dip2px(mContext,5);
        unreadParams.setMargins(0,UnitExchangeUtil.dip2px(mContext,9),UnitExchangeUtil.dip2px(mContext,9),0);
        holder.unreadMsgNotice.setLayoutParams(unreadParams);
    }

    private void showVoiceNotice(ViewHolder holder, int position) {
        int value = devices.get(position).getUnreadMessageNumber();
        if (value > 0) {
            holder.unreadMsgNotice.setVisibility(View.VISIBLE);
            holder.unreadMsgNotice.setHighLightMode(true);
        } else {
            holder.unreadMsgNotice.setVisibility(View.GONE);
        }
    }

    static class ViewHolder {
        @BindView(R.id.deviceImage)
        RoundedImageView deviceImage;
        @BindView(R.id.nameNotOnline)
        TextView nameNotOnline;
        @BindView(R.id.nameOnlineBg)
        ImageView nameOnlineBg;
        @BindView(R.id.nameOnline)
        TextView nameOnline;
        @BindView(R.id.clickEffectView)
        View clickEffectView;
        @BindView(R.id.unreadMsgNotice)
        MaterialBadgeTextView unreadMsgNotice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
