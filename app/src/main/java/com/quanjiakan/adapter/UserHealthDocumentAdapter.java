package com.quanjiakan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.common.image.ImageViewerActivity;
import com.quanjiakan.adapter.holder.UserHealthDocumentHolder;
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.retrofit.result_entity.subentity.UserHealthDocumentEntity;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class UserHealthDocumentAdapter extends BaseAdapter {

    private List<UserHealthDocumentEntity> data;
    private Context context;

    public UserHealthDocumentAdapter(Context context, List<UserHealthDocumentEntity> data){
        this.context = context;
        this.data = data;
    }

    public List<UserHealthDocumentEntity> getData() {
        return data;
    }

    public void setData(List<UserHealthDocumentEntity> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data!=null){
            return data.size();
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
        UserHealthDocumentHolder holder;
        if(convertView==null){
            holder = new UserHealthDocumentHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_health_document,null);
            holder.div = convertView.findViewById(R.id.div);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.intro_content = (TextView) convertView.findViewById(R.id.intro_content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else{
            holder= (UserHealthDocumentHolder) convertView.getTag();
        }
        final UserHealthDocumentEntity entity = data.get(position);
        if(position==data.size()-1){
            holder.div.setVisibility(View.VISIBLE);
        }else{
            holder.div.setVisibility(View.VISIBLE);
        }
        if(entity!=null &&entity.getMedicalRecord()!=null && entity.getMedicalRecord().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)){
            Picasso.with(context).load(entity.getMedicalRecord()).into(holder.image);
            holder.image.setBackgroundResource(R.drawable.transparent_background);//transparent_background  case_pic_border
        }else{
            holder.image.setImageResource(R.drawable.picture_not_found);
            holder.image.setBackgroundResource(R.drawable.transparent_background);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entity!=null &&entity.getMedicalRecord()!=null && entity.getMedicalRecord().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)) {
                    Intent intent = new Intent(context, ImageViewerActivity.class);
                    intent.putExtra(IParamsName.PARAMS_URL, entity.getMedicalRecord());
                    context.startActivity(intent);
                }else{
                    CommonDialogHint.getInstance().showHint(context,context.getString(R.string.error_common_invalid_picture));
                }
            }
        });
        if(entity!=null && entity.getMedicalName()!=null && entity.getMedicalName().length()>0){
            holder.intro_content.setText(""+entity.getMedicalName());
        }else{
            holder.intro_content.setText("");
        }
        if(entity!=null && entity.getCreatetime()!=null){
            holder.time.setText(context.getString(R.string.setting_user_health_document_publish_time_prefix)+entity.getCreatetimeFormat());
        }else{
            holder.time.setText("");
        }
        return convertView;
    }
}
