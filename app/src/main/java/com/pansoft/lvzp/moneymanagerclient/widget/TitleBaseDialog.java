package com.pansoft.lvzp.moneymanagerclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class TitleBaseDialog extends Dialog {

    public TitleBaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics display = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.widthPixels * 0.8); //设置宽度
        getWindow().setAttributes(lp);
    }
}
