package com.pansoft.lvzp.moneymanagerclient.utils;

import android.widget.EditText;

/**
 * Created by lv_zhp on 2018/4/15.
 */
public class EditTextUtils {

    /**
     * 请求获取指定EditText的焦点
     *
     * @param view 指定的EditText控件
     */
    public static void reqsetGetFocus(EditText view) {
        view.setFocusable(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
    }

}
