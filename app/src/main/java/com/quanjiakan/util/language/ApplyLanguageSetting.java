package com.quanjiakan.util.language;

/**
 * Created by Administrator on 2017/10/12.
 */

public interface ApplyLanguageSetting {
    /**
     * 切换语言时，能够刷新界面显示文字，固定的显示数据最好使用资源引用的方式提供
     */
    void refreshViewValues();
}
