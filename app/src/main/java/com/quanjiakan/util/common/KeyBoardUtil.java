package com.quanjiakan.util.common;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.quanjiakan.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/11/15.
 */

public class KeyBoardUtil {

    private InputMethodManager imm;

    public KeyBoardUtil(){

    }

    public void showSoftInput(BaseActivity context, View view){
//        isOpenSoftInput();
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(imm==null){
            imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput(BaseActivity context,View view){
//        isOpenSoftInput();
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(imm==null){
            imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isOpenSoftInput(BaseActivity context){
        if(imm==null){
            imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        boolean bool = imm.isActive();
        if(bool){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return bool;
    }
}
