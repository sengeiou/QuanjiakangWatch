package com.quanjiakan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.db.entity.HealthInquiryPatientDataEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/17.
 */

public class HealthInquriryPatientInfoAdapter extends BaseAdapter {
    private Context context;
    private List<HealthInquiryPatientDataEntity> dataEntityList;

    private int checkedPosition = -1;

    public HealthInquriryPatientInfoAdapter(Context context, List<HealthInquiryPatientDataEntity> dataEntityList) {
        this.context = context;
        this.dataEntityList = dataEntityList;
    }

    @Override
    public int getCount() {
        if (dataEntityList != null) {
            return dataEntityList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public HealthInquiryPatientDataEntity getByPosition(int position) {
        if (position > -1 && position < getCount() &&
                dataEntityList != null && dataEntityList.size() > 0) {
            return dataEntityList.get(position);
        } else {
            return null;
        }
    }

    public void clickPosition(int position) {
        if (position > -1 && position < getCount()) {
            checkedPosition = position;
        }
    }

    public int getCheckedPosition(){
        return checkedPosition;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        HealthInquiryPatientDataEntity entity = dataEntityList.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_patient_data, null, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.chk.setText(entity.getName() + context.getString(R.string.health_inquiry_patient_data_adapter_left) +
                entity.getGender() + context.getString(R.string.health_inquiry_patient_data_adapter_middle)+
                entity.getAge() + context.getString(R.string.health_inquiry_patient_data_adapter_right));
        if (checkedPosition == position) {
            Drawable drawable_n = context.getResources().getDrawable(R.drawable.deliver_address_selected);//deliver_address_unselected
            drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
            holder.chk.setCompoundDrawables(null, null, drawable_n, null);
        } else {
            Drawable drawable_n = context.getResources().getDrawable(R.drawable.deliver_address_unselected);//
            drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
            holder.chk.setCompoundDrawables(null, null, drawable_n, null);
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.chk)
        TextView chk;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
