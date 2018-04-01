package com.pansoft.lvzp.moneymanagerclient.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.pansoft.lvzp.moneymanagerclient.Constant;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.databinding.LayoutDialogConfigBaseUrlBinding;
import com.pansoft.lvzp.moneymanagerclient.utils.SharedPreferencesUtils;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class ConfigBaseUrlDialog extends Dialog {

    private LayoutDialogConfigBaseUrlBinding mDataBinding;
    private View.OnClickListener mOnConfirmClickListener;

    public ConfigBaseUrlDialog(@NonNull Context context) {
        super(context);
        View layoutView = getLayoutInflater().inflate(R.layout.layout_dialog_config_base_url, null);
        setContentView(layoutView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        mDataBinding =
                DataBindingUtil.bind(layoutView);
        String hostIp = (String) SharedPreferencesUtils.getParam(context, Constant.SERVICE_HOST_KEY, "192.168.2.149");
        if (!TextUtils.isEmpty(hostIp)) {
            mDataBinding.editInputIp.setText(hostIp);
            mDataBinding.editInputIp.setSelection(hostIp.length());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mOnConfirmClickListener != null)
            mDataBinding.tvConfirm.setOnClickListener(mOnConfirmClickListener);
    }

    public void setOnConfirmClickListener(View.OnClickListener listener) {
        mOnConfirmClickListener = listener;
    }

    public String getInputIp() {
        return mDataBinding.editInputIp.getText().toString();
    }

}
