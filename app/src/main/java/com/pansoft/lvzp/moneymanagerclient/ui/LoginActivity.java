package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.pansoft.lvzp.moneymanagerclient.Constant;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityLoginBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;
import com.pansoft.lvzp.moneymanagerclient.utils.SharedPreferencesUtils;
import com.pansoft.lvzp.moneymanagerclient.widget.ConfigBaseUrlDialog;

import java.util.Map;

/**
 * 登陆的页面
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private ConfigBaseUrlDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.activity_name_login);
        }
        buildConfigDialog();
    }

    private void buildConfigDialog() {
        if (mDialog == null) {
            mDialog = new ConfigBaseUrlDialog(this);
            mDialog.setOnConfirmClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String inputIp = mDialog.getInputIp();
                    if (TextUtils.isEmpty(inputIp)) {
                        showToast("请输入ip地址后确认");
                        return;
                    }
                    Apl.getInstance().setBaseUrl(inputIp);
                    showProgressDialog();
                    OkHttpClientManager.getInstance().asyncGet(ApiUrl.BASE_CHECK, new OkHttpClientManager.HttpResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {
                            dismissProgressDialog();
                            if (data) {
                                mDialog.dismiss();
                                showToast(getString(R.string.connect_sul));
                                SharedPreferencesUtils.setParam(mContext, Constant.SERVICE_HOST_KEY, inputIp);
                                String userOid = (String) SharedPreferencesUtils.getParam(mContext, Constant.USER_LOGIN_OID, "");
                                if (!TextUtils.isEmpty(userOid)) {
                                    Apl.getInstance().setUserOid(userOid);
                                    MainActivity.actionStart(mContext);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            simpleError(getString(R.string.not_found_ip_service) + "；错误信息：" + msg);
                            Apl.getInstance().setBaseUrl(null);
                        }
                    });
                }
            });
        }
        mDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = mDataBinding.editInputUsername.getText().toString();
                String password = mDataBinding.editInputPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    showToast(getString(R.string.edit_hint_input_username));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.edit_hint_input_password));
                    return;
                }
                Map<String, Object> params = new ArrayMap<>();
                params.put("username", username);
                params.put("password", password);
                showProgressDialog();
                OkHttpClientManager.getInstance().asyncPost(ApiUrl.LOGIN, params, new OkHttpClientManager.HttpResultCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        SharedPreferencesUtils.setParam(mContext, Constant.USER_LOGIN_OID, data);
                        dismissProgressDialog();
                        showToast("登录成功");
                        MainActivity.actionStart(mContext);
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        simpleError(msg);
                    }
                });
                break;
            case R.id.btn_register:
                RegisterActivity.actionStart(mContext);
                break;
        }
    }
}
