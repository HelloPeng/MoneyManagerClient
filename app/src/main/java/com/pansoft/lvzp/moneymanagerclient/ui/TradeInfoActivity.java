package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityTradeInfoBinding;

public class TradeInfoActivity extends BaseActivity<ActivityTradeInfoBinding> {

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TradeInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_info;
    }

    @Override
    protected void initViews() {
        openBackIcon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trade_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_defray:

                break;
            case R.id.menu_item_income:
                EditAndDetailPropertyActivity.actionStart(mContext);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
