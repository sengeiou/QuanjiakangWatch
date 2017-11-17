package com.quanjiakan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pingantong.main.R;
import com.quanjiakan.net.retrofit.result_entity.PostLastTenMessage;
import com.quanjiakan.util.common.SerializeToObjectUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/15.
 */

public class HealthInquiryProblemListAdapter extends BaseAdapter {

    private Context context;
    private List<PostLastTenMessage.ListBean> data;

    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatTime;

    public HealthInquiryProblemListAdapter(Context context, List<PostLastTenMessage.ListBean> data) {
        this.context = context;
        this.data = data;

        formatDate = new SimpleDateFormat(context.getString(R.string.health_inquiry_history_format_date));
        formatTime = new SimpleDateFormat(context.getString(R.string.health_inquiry_history_format_time));
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean isReplied(int position){
        return data.get(position).isReply();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        PostLastTenMessage.ListBean entity = data.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_conversation, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.titleLine.setVisibility(View.GONE);
        //TODO 主题-----当前消息
        holder.lastMsg.setText(entity.getTitle());
        //TODO 是否回复
        holder.conversationStatusFlag.setVisibility(View.GONE);//TODO 若有必要，这里可以放开，未回复的消息将会进行提示
        if(entity.isReply()){
            holder.conversationStatus.setText(context.getString(R.string.health_inquiry_history_replied));
            holder.conversationStatusFlag.setBackgroundResource(R.drawable.selector_conversation_cycle_green);
        }else{
            holder.conversationStatus.setText(context.getString(R.string.health_inquiry_history_not_replied));
            holder.conversationStatusFlag.setBackgroundResource(R.drawable.selector_conversation_cycle_red);
        }
        setInfo(entity,holder);

        return view;
    }

    public void setInfo(PostLastTenMessage.ListBean entity,ViewHolder holder){
        if(holder==null){
            return;
        }
        if(entity==null){
            holder.conversationTime.setVisibility(View.GONE);
            holder.lastInfo.setVisibility(View.GONE);
            return;
        }else{
            holder.conversationTime.setVisibility(View.VISIBLE);
            holder.lastInfo.setVisibility(View.VISIBLE);
        }
        //TODO 设置会话时间
        long time = entity.getCreateMillisecond();
        if (60000 < (System.currentTimeMillis() - time)) {
            holder.conversationTime.setText(formatTime.format(time));
        }else{
            holder.conversationTime.setText(context.getString(R.string.health_inquiry_history_right_now));
        }
        if(entity.getPatient()!=null && entity.getPatient().length()>0){
            holder.lastInfo.setVisibility(View.VISIBLE);
            PatientInfo info = (PatientInfo) SerializeToObjectUtil.getInstances().jsonToObject(entity.getPatient(),new TypeToken<PatientInfo>(){}.getType());
            holder.lastInfo.setText(formatDate.format(time)+context.getString(R.string.health_inquiry_history_divider)+
            info.getName()+context.getString(R.string.health_inquiry_history_divider)+
                    info.getSex()+context.getString(R.string.health_inquiry_history_divider)+
                    info.getAge()+context.getString(R.string.health_inquiry_history_divider)+
                    context.getString(R.string.health_inquiry_history_charge_type_free));
        }else{
            holder.lastInfo.setVisibility(View.GONE);
            return;
        }
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.title_line)
        LinearLayout titleLine;
        @BindView(R.id.last_msg)
        TextView lastMsg;
        @BindView(R.id.last_info)
        TextView lastInfo;
        @BindView(R.id.conversation_time)
        TextView conversationTime;
        @BindView(R.id.conversation_status_flag)
        View conversationStatusFlag;
        @BindView(R.id.conversation_status)
        TextView conversationStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class PatientInfo{

        /**
         * age : 22
         * sex : 男
         * name : 李勇
         * type : patient_meta
         */

        private String age;
        private String sex;
        private String name;
        private String type;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String Name) {
            this.name = Name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
