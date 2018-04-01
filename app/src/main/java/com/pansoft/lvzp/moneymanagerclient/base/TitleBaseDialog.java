package com.pansoft.lvzp.moneymanagerclient.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.databinding.LayoutDialogTitleBaseBinding;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public abstract class TitleBaseDialog<CD extends ViewDataBinding> extends Dialog {

    protected CD mDataBinding;
    private LayoutDialogTitleBaseBinding mTitleBaseBinding;

    public TitleBaseDialog(@NonNull Context context) {
        super(context);
        View layoutView = getLayoutInflater().inflate(R.layout.layout_dialog_title_base, null);
        setContentView(layoutView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        mTitleBaseBinding = DataBindingUtil.bind(layoutView);
        View childLayoutView = getLayoutInflater().inflate(getChildLayoutId(), null);
        mTitleBaseBinding.flContentView.removeAllViews();
        mTitleBaseBinding.flContentView.addView(childLayoutView);
        mDataBinding = DataBindingUtil.bind(childLayoutView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics display = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.widthPixels; //设置宽度
        getWindow().setAttributes(lp);
    }

    protected abstract int getChildLayoutId();

    public void setTitleText(String titleText) {
        mTitleBaseBinding.tvTitle.setText(titleText);
    }

    public void setOnConfirmClickListener(View.OnClickListener listener) {
        mTitleBaseBinding.tvConfirm.setVisibility(View.VISIBLE);
        mTitleBaseBinding.tvConfirm.setOnClickListener(listener);
    }

    public void setOnCancelClickListener(View.OnClickListener li) {
        mTitleBaseBinding.tvCancel.setVisibility(View.VISIBLE);
        mTitleBaseBinding.tvCancel.setOnClickListener(li);
    }
}
