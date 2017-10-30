package com.quanjiakan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.common.image.ImageViewerActivity;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.util.widget.RoundTransform;
import com.quanjiakan.watch.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HouseKeeperListAdapter extends BaseAdapter {

	private Context context;
	private List<GetHouseKeeperListEntity.ListBean> mList;
	private LayoutInflater inflater;

	public HouseKeeperListAdapter(Context context, List<GetHouseKeeperListEntity.ListBean> mList){
		this.context = context;
		this.mList = mList;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0).getId();
	}

	@Override
	public View getView(int p, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(v == null){
			v = inflater.inflate(R.layout.item_housekeeper, null);
			holder = new ViewHolder();
			holder.image = (ImageView)v.findViewById(R.id.image);
			holder.tv_price = (TextView)v.findViewById(R.id.tv_price);
			holder.tv_name = (TextView)v.findViewById(R.id.tv_name);
			holder.tv_name_recommond = (TextView) v.findViewById(R.id.tv_name_recommond);
			holder.tv_baomu = (TextView)v.findViewById(R.id.tv_baomu);
			holder.tv_age = (TextView)v.findViewById(R.id.tv_age);
			holder.tv_from_region = (TextView)v.findViewById(R.id.tv_from_region);
			holder.tv_house_company = (TextView)v.findViewById(R.id.tv_house_company);
			v.setTag(holder);
		}else {
			holder = (ViewHolder)v.getTag();
		}
		bindData(holder, p);
		return v;
	}
	
	void bindData(ViewHolder holder,int pos){

		try {
			final GetHouseKeeperListEntity.ListBean object = mList.get(pos);

			holder.tv_name.setText(object.getName());
			/**
			 * 设置是否为系统推荐
			 *
			 */
			if(object.getLanguage()!=null && object.getLanguage().length()>0){
				holder.tv_baomu.setText(object.getLanguage());
			}else{
				holder.tv_baomu.setText(context.getString(R.string.hint_house_keeper_item_language_default));
			}
			if(object.getPrice()!=null && object.getPrice().length()>0){
				holder.tv_price.setText(object.getPrice()+context.getString(R.string.hint_house_keeper_item_price_unit));
			}else{
				holder.tv_price.setVisibility(View.GONE);
			}
			holder.tv_age.setText(object.getAge()+"岁");
			/**
			 * 身高
			 */
			if(object.getHeight()!=0){
				holder.tv_from_region.setText(object.getHeight()+"CM");
			}else{
				holder.tv_from_region.setText("");
				holder.tv_from_region.setVisibility(View.GONE);
			}
			//
			if(object.getExperience()!=null && object.getExperience().length()>0){
				holder.tv_house_company.setText("广州市巨硅信息科技有限公司" + " 从业"+object.getExperience()+"年");
			}else{
				holder.tv_house_company.setText("广州市巨硅信息科技有限公司");
			}
			if(object.getImage()!=null &&
					object.getImage().length()>0 &&
					object.getImage().toLowerCase().startsWith("http")){
				Picasso.with(context).load(object.getImage()).error(R.drawable.ic_patient).fit().
						transform(new RoundTransform()).into(holder.image);;
			}else{
				Picasso.with(context).load(R.drawable.ic_patient).into(holder.image);
			}

			holder.image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						if(object.getImage()!=null && object.getImage().toLowerCase().startsWith("http")){
							Intent intent = new Intent(context, ImageViewerActivity.class);
							intent.putExtra(IParamsName.PARAMS_URL,object.getImage());
							context.startActivity(intent);
						}else{
							BaseApplication.getInstances().toast(context,"该家政人员无头像!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	class ViewHolder{
		TextView tv_name;
		TextView tv_name_recommond;
		TextView tv_price;
		TextView tv_baomu;
		TextView tv_age;
		TextView tv_from_region;
		TextView tv_house_company;
		ImageView image;
	}
	
}
