package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pansoft.lvzp.moneymanagerclient.Constant;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityMainBinding;
import com.pansoft.lvzp.moneymanagerclient.utils.SharedPreferencesUtils;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_menu_user_manager:
                break;
            case R.id.ll_menu_trade_info:
                break;
            case R.id.ll_menu_data_analysis:
                break;
            case R.id.ll_menu_exit:
                new AlertDialog.Builder(mContext)
                        .setTitle("温馨提示")
                        .setMessage("您是否要退出当前登录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferencesUtils.setParam(mContext, Constant.USER_LOGIN_OID, null);
                                LoginActivity.actionStart(mContext);
                                finish();
                            }
                        }).create().show();
                break;
        }
    }

}
