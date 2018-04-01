package com.pansoft.lvzp.moneymanagerclient.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by lv_zhp on 2018/4/1.
 * 所有Activity的父类
 * <p>
 * 主要目的：
 * 1.初始化DataBinding对象，子类只需要传递进来对应的范型即可
 * 2.通用的加载框
 * 3.通用Toast
 * </p>
 */

public abstract class BaseActivity<D extends ViewDataBinding> extends AppCompatActivity {

    protected abstract int getLayoutId();

    protected D mDataBinding;

    private ProgressDialog mProgressDialog;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void showProgressDialog() {
        showProgressDialog(null);
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        if (TextUtils.isEmpty(message)) {
            message = "数据加载中...";
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    protected void simpleError(String errorMsg) {
        showToast(errorMsg);
        dismissProgressDialog();
    }

}
