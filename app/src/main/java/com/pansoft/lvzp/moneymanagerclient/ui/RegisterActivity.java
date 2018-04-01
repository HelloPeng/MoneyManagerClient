package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityRegisterBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;

import java.util.Map;

/**
 * 注册的页面
 */
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_register:
                String username = mDataBinding.editInputRegisterUsername.getText().toString();
                String password = mDataBinding.editInputRegisterPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    showToast(getString(R.string.edit_hint_input_username));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.edit_hint_input_username));
                    return;
                }
                String againPassword = mDataBinding.editInputRegisterPasswordAgain.getText().toString();
                if (!password.equals(againPassword)) {
                    showToast(getString(R.string.input_password_difference));
                    return;
                }
                Map<String,String> params = new ArrayMap<>();
                params.put("username",username);
                params.put("password",password);
                showProgressDialog();
                OkHttpClientManager.getInstance().asyncPost(ApiUrl.REGISTER, params, new OkHttpClientManager.HttpResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        dismissProgressDialog();
                        showToast("恭喜您注册成功");
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        simpleError(msg);
                    }
                });
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
