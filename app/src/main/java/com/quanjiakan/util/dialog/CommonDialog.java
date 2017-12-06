package com.quanjiakan.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pingantong.main.R;
import com.quanjiakan.activity.base.BaseActivity;
import com.quanjiakan.activity.base.BaseApplication;
import com.quanjiakan.activity.common.login.SigninActivity_mvp;
import com.quanjiakan.util.common.UnitExchangeUtil;

import java.util.List;

public class CommonDialog {
	
	private static CommonDialog instance;
	private View dialogView;

	public static CommonDialog getInstance() {
		if (instance == null) {
			instance = new CommonDialog();
		}
		return instance;
	}

	private CommonDialog() {

	}

	private void initDialog(Context context) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = new Dialog(context, R.style.dialog_loading);
	}

	private Dialog mDialog;
	/*
	* 获取默认的对话框*/
	public Dialog getDialog(Context context) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		this.dialogView = view;
		LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;
		mDialog.setContentView(view, lp);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		return mDialog;
	}

	public Dialog getLogoutDialog(final Context context) {
		final Dialog dialog = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(context.getResources().getString(R.string.jmui_user_logout_dialog_title));
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(context.getResources().getString(R.string.jmui_user_logout_dialog_message));

		TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
		btn_cancel.setVisibility(View.INVISIBLE);

		TextView btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
		btn_confirm.setVisibility(View.VISIBLE);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
                Intent intent = new Intent(context, SigninActivity_mvp.class);
				context.startActivity(intent);
                BaseApplication.getInstances().onLogout((BaseActivity) context);
				((Activity)context).finish();
			}
		});

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 300);
		lp.height = lp.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		dialog.setContentView(view, lp);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return dialog;
	}

	public Dialog getCardDialog(Context context) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = new Dialog(context,R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		this.dialogView = view;
		LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = context.getResources().getDisplayMetrics().widthPixels;
		lp.height = lp.MATCH_PARENT;
		lp.gravity = Gravity.BOTTOM;
		mDialog.setContentView(view, lp);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		return mDialog;
	}

	/*
	* 获取默认的对话框*/
	public Dialog getUnShowDialog(Context context) {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = new Dialog(context, R.style.dialog_loading);
		return mDialog;
	}

	public Dialog getCommonConfirmDialog(Context context, String title, String content, final OnClickListener confirm) {
//		initDialog(context);
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null);
		this.dialogView = view;
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(content);

		TextView btn_cancel =  (TextView) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialogTemp.dismiss();
			}
		});

		TextView btn_confirm =  (TextView) view.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialogTemp.dismiss();
				if(confirm!=null){
					confirm.onClick(v);
				}
			}
		});


		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;

		mDialogTemp.setContentView(view, lp);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public Dialog getCommonConfirmDialog(Context context, String title, String content,
										 String cancelText, String confirmText, final OnClickListener confirm) {
//		initDialog(context);
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null);
		this.dialogView = view;
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(content);

		TextView btn_cancel =  (TextView) view.findViewById(R.id.btn_cancel);
		btn_cancel.setText(cancelText);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialogTemp.dismiss();
			}
		});

		TextView btn_confirm =  (TextView) view.findViewById(R.id.btn_confirm);
		btn_confirm.setText(confirmText);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialogTemp.dismiss();
				if(confirm!=null){
					confirm.onClick(v);
				}
			}
		});


		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;

		mDialogTemp.setContentView(view, lp);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public Dialog getCommonConfirmDialog(Context context, String title, String content,
										 String cancelText, boolean isShowCancel, String confirmText, boolean isShowConfirm, final OnClickListener confirm) {
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null);
		this.dialogView = view;
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(content);

		TextView btn_cancel =  (TextView) view.findViewById(R.id.btn_cancel);
		if(isShowCancel){
			btn_cancel.setVisibility(View.VISIBLE);
		}else{
			btn_cancel.setVisibility(View.GONE);
		}
		btn_cancel.setText(cancelText);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialogTemp.dismiss();
			}
		});

		TextView btn_confirm =  (TextView) view.findViewById(R.id.btn_confirm);
		if(isShowConfirm){
			btn_confirm.setVisibility(View.VISIBLE);
		}else{
			btn_confirm.setVisibility(View.GONE);
		}
		btn_confirm.setText(confirmText);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialogTemp.dismiss();
				if(confirm!=null){
					confirm.onClick(v);
				}
			}
		});


		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;

		mDialogTemp.setContentView(view, lp);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public Dialog getCommonConfirmDialog(Context context, String title, String content,
										 String cancelText, boolean isShowCancel, final OnClickListener cancel,
										 String confirmText, boolean isShowConfirm, final OnClickListener confirm) {
//		initDialog(context);
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null);
		this.dialogView = view;
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(content);

		TextView btn_cancel =  (TextView) view.findViewById(R.id.btn_cancel);
		if(isShowCancel){
			btn_cancel.setVisibility(View.VISIBLE);
		}else{
			btn_cancel.setVisibility(View.GONE);
		}
		btn_cancel.setText(cancelText);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialogTemp.dismiss();
				if(cancel!=null){
					cancel.onClick(view);
				}
			}
		});

		TextView btn_confirm =  (TextView) view.findViewById(R.id.btn_confirm);
		if(isShowConfirm){
			btn_confirm.setVisibility(View.VISIBLE);
		}else{
			btn_confirm.setVisibility(View.GONE);
		}
		btn_confirm.setText(confirmText);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialogTemp.dismiss();
				if(confirm!=null){
					confirm.onClick(v);
				}
			}
		});


		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;

		mDialogTemp.setContentView(view, lp);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public Dialog getCommonModifyDialog(Context context, String title, String hint, final IDialogCallback confirm) {
//		initDialog(context);
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_modify, null);
		this.dialogView = view;

		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		final EditText tv_content = (EditText) view.findViewById(R.id.tv_content);
		tv_content.setHint(hint);

		TextView btn_cancel =  (TextView) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mDialogTemp.dismiss();
			}
		});

		TextView btn_confirm =  (TextView) view.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialogTemp.dismiss();
				confirm.getString(tv_content.getText().toString());
			}
		});

		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;

		mDialogTemp.setContentView(view, lp);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public void resizeDialog(Activity context, Dialog dialog, View contentView){
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 300);
		lp.height = lp.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;

	}

	/*
	* 获取对话框+标题
	* */
	public Dialog getDialog(Context context, String message) {
		initDialog(context);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		this.dialogView = view;
		TextView tv = (TextView) view.findViewById(R.id.tv);
		tv.setText(message);
		tv.setVisibility(View.VISIBLE);
		LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = UnitExchangeUtil.dip2px(context, 80);
		lp.height = UnitExchangeUtil.dip2px(context, 80);
		lp.gravity = Gravity.CENTER;
		mDialog.setContentView(view, lp);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
		return mDialog;
	}

	/*
	* 获取对话框列表
	* */
	public Dialog getListDialogDefineHeight(Context context, List<String> strings, String title,
											final MyDialogCallback callback, final Object object) {
		initDialog(context);

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView) view.findViewById(R.id.listview);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_simple_textview, strings);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				callback.onItemClick(arg2, object);
				mDialog.dismiss();
			}
		});
		LayoutParams lp = mDialog.getWindow().getAttributes();
		lp.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		lp.height = context.getResources().getDisplayMetrics().widthPixels;
		lp.gravity = Gravity.CENTER;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.getWindow().setLayout(lp.width, lp.height);
		mDialog.show();
		return mDialog;
	}	

	/*
	* 获取录音对话框
	* */
	public Dialog getVoiceRecordingDialog(Context context, String title,
										  final MyDialogCallback callback, final Object object) {
//		initDialog(context);
		final Dialog mDialogTemp = new Dialog(context, R.style.dialog_loading);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_recording, null);
		TextView tv_stop = (TextView)view.findViewById(R.id.tv_stop);
		LayoutParams lp = mDialogTemp.getWindow().getAttributes();
		lp.width = context.getResources().getDisplayMetrics().widthPixels * 3 / 4;
		lp.height = context.getResources().getDisplayMetrics().widthPixels;
		lp.gravity = Gravity.CENTER;
		tv_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				callback.onItemClick(0, null);
				mDialogTemp.dismiss();
			}
		});
		mDialogTemp.setContentView(view);
		mDialogTemp.setCanceledOnTouchOutside(false);
		mDialogTemp.getWindow().setLayout(lp.width, lp.height);
		mDialogTemp.show();
		return mDialogTemp;
	}

	public View getDialogView() {
		return this.dialogView;
	}

	public interface MyDialogCallback {
		public void onItemClick(int position, Object object);
	}

}
