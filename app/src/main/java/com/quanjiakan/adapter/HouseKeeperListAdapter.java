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
import com.quanjiakan.constants.ICommonData;
import com.quanjiakan.constants.IParamsName;
import com.quanjiakan.net.retrofit.result_entity.GetHouseKeeperListEntity;
import com.quanjiakan.util.dialog.CommonDialogHint;
import com.quanjiakan.util.widget.RoundTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HouseKeeperListAdapter extends BaseAdapter {

    private Context context;
    private List<GetHouseKeeperListEntity.ListBean> mList;
    private LayoutInflater inflater;

    public HouseKeeperListAdapter(Context context, List<GetHouseKeeperListEntity.ListBean> mList) {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int p, View v, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.item_housekeeper, null);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.image);
            holder.tv_price = (TextView) v.findViewById(R.id.tv_price);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.tv_name_recommond = (TextView) v.findViewById(R.id.tv_name_recommond);
            holder.tv_baomu = (TextView) v.findViewById(R.id.tv_baomu);
            holder.tv_age = (TextView) v.findViewById(R.id.tv_age);
            holder.tv_from_region = (TextView) v.findViewById(R.id.tv_from_region);
            holder.tv_house_company = (TextView) v.findViewById(R.id.tv_house_company);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        bindData(holder, p);
        return v;
    }

    void bindData(ViewHolder holder, int pos) {

        try {
            final GetHouseKeeperListEntity.ListBean object = mList.get(pos);

            holder.tv_name.setText(object.getName());
            /**
             * 设置是否为系统推荐
             *
             */
            if (object.getLanguage() != null && object.getLanguage().length() > 0) {
                holder.tv_baomu.setText(object.getLanguage());
            } else {
                holder.tv_baomu.setText(context.getString(R.string.hint_house_keeper_item_language_default));
            }
            if (object.getPrice() != null && object.getPrice().length() > 0) {
                holder.tv_price.setText(object.getPrice() + context.getString(R.string.hint_house_keeper_item_price_unit));
            } else {
                holder.tv_price.setVisibility(View.GONE);
            }
            holder.tv_age.setText(object.getAge() + context.getString(R.string.hint_house_keeper_item_age_unit));
            /**
             * 身高
             */
            if (object.getHeight() != 0) {
                holder.tv_from_region.setText(object.getHeight() + context.getString(R.string.hint_house_keeper_item_height_unit));
            } else {
                holder.tv_from_region.setText("");
                holder.tv_from_region.setVisibility(View.GONE);
            }
            //
            if (object.getExperience() != null && object.getExperience().length() > 0) {
                holder.tv_house_company.setText(context.getString(R.string.hint_house_keeper_item_company) +
                        context.getString(R.string.hint_house_keeper_item_experience_prefix) + object.getExperience() +
                        context.getString(R.string.hint_house_keeper_detail_experience_postfix));
            } else {
                holder.tv_house_company.setText(context.getString(R.string.hint_house_keeper_item_company));
            }

            if (object.getImage() != null &&
                    object.getImage().length() > 0 &&
                    object.getImage().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)) {
                Picasso.with(context).load(object.getImage()).error(R.drawable.ic_patient).
                        fit().    //TODO 不使用fit方法会导致按照原图片的形态进行裁剪
                        placeholder(R.drawable.image_placeholder).
                        transform(new RoundTransform()).into(holder.image);
            } else {
                Picasso.with(context).load(R.drawable.ic_launcher).into(holder.image);
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (object.getImage() != null && object.getImage().toLowerCase().startsWith(ICommonData.HTTP_PREFIX)) {
                            Intent intent = new Intent(context, ImageViewerActivity.class);
                            intent.putExtra(IParamsName.PARAMS_URL, object.getImage());
                            context.startActivity(intent);
                        } else {
                            CommonDialogHint.getInstance().showHint(context, context.getString(R.string.hint_house_keeper_list_no_headimg));
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

    class ViewHolder {
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
