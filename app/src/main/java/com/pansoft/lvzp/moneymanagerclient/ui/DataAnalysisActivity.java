package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.widget.RadioGroup;

import com.pansoft.lvzp.moneymanagerclient.Constant;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityDataAnalysisBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;
import com.pansoft.lvzp.moneymanagerclient.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tech.linjiang.suitlines.Unit;

public class DataAnalysisActivity
        extends BaseActivity<ActivityDataAnalysisBinding>
        implements RadioGroup.OnCheckedChangeListener {


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DataAnalysisActivity.class);
        context.startActivity(intent);
    }

    private int mType = 1;
    private int mMonth = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_analysis;
    }

    @Override
    protected void initViews() {
        openBackIcon();
        mDataBinding.rdoGroupType.setOnCheckedChangeListener(this);
        mDataBinding.rdoGroupDate.setOnCheckedChangeListener(this);
        loadTradeData();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdoBtn_three:
                mMonth = 3;
                break;
            case R.id.rdoBtn_six:
                mMonth = 6;
                break;
            case R.id.rdoBtn_year:
                mMonth = 12;
                break;
            case R.id.rdoBtn_defray:
                mType = 0;
                break;
            case R.id.rdoBtn_income:
                mType = 1;
                break;
        }
        loadTradeData();
    }

    private void loadTradeData() {
        showProgressDialog();
        Map<String, Object> params = new ArrayMap<>();
        String userOid = (String) SharedPreferencesUtils.getParam(mContext, Constant.USER_LOGIN_OID, "");
        params.put("parentOid", userOid);
        params.put("type", mType);
        params.put("month", mMonth);
        OkHttpClientManager.getInstance()
                .asyncPost(ApiUrl.TRADE_ANALYSIS_MONTH
                        , params
                        , new OkHttpClientManager.HttpResultCallback<Map<String, String>>() {
                            @Override
                            public void onSuccess(Map<String, String> data) {
                                dismissProgressDialog();
                                mDataBinding.suitlines.setXySize(8);
                                List<Unit> lines = new ArrayList<>();
                                Set<String> monthSet = data.keySet();
                                for (String month : monthSet) {
                                    lines.add(new Unit(Float.parseFloat(data.get(month)), month + "月"));
                                }
                                mDataBinding.suitlines.feedWithAnim(lines);
                            }

                            @Override
                            public void onError(String msg) {
                                simpleError(msg);
                            }
                        });
    }
}
