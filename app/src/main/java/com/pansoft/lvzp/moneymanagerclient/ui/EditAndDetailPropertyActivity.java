package com.pansoft.lvzp.moneymanagerclient.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.pansoft.lvzp.moneymanagerclient.CashierInputFilter;
import com.pansoft.lvzp.moneymanagerclient.Constant;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.bean.MemberUserBean;
import com.pansoft.lvzp.moneymanagerclient.bean.TradeItemBean;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityEditAndDetailPropertyBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;
import com.pansoft.lvzp.moneymanagerclient.utils.EditTextUtils;
import com.pansoft.lvzp.moneymanagerclient.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 显示和修改资产信息的页面
 */
public class EditAndDetailPropertyActivity
        extends BaseActivity<ActivityEditAndDetailPropertyBinding>
        implements RadioGroup.OnCheckedChangeListener {


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, EditAndDetailPropertyActivity.class);
        context.startActivity(intent);
    }

    private DatePickerDialog mDatePickerDialog;
    private TradeItemBean mTradeBean;
    private ArrayMap<String, String> mMemberUser;
    private boolean isSelectOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_and_detail_property;
    }

    @Override
    protected void initViews() {
        openBackIcon();
        mDataBinding.rdoGroupIncomeTag.setOnCheckedChangeListener(this);
        mDataBinding.editInputMoney.setFilters(new InputFilter[]{new CashierInputFilter()});
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        getTradeBean().setDate(date);
                        mDataBinding.tvSelectDate.setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rdoBtn_other) {
            isSelectOther = true;
            mDataBinding.editInputOther.setVisibility(View.VISIBLE);
            EditTextUtils.reqsetGetFocus(mDataBinding.editInputOther);
        } else {
            TextView childView = findViewById(checkedId);
            mTradeBean.setConsumeTag(childView.getText().toString());
            isSelectOther = false;
            mDataBinding.editInputOther.setVisibility(View.INVISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (isSelectOther) {
                    if (TextUtils.isEmpty(mDataBinding.editInputOther.getText())) {
                        showToast("请填写收入来源的详细信息");
                        return;
                    }
                    mTradeBean.setConsumeTag(mDataBinding.editInputOther.getText().toString());
                }
                if (TextUtils.isEmpty(mDataBinding.editInputMoney.getText())) {
                    showToast("请填写您的收入金额");
                    return;
                }
                mTradeBean.setMoney(mDataBinding.editInputMoney.getText().toString());
                if (!TextUtils.isEmpty(mDataBinding.editInputRemarks.getText())) {
                    mTradeBean.setRemarks(mDataBinding.editInputRemarks.getText().toString());
                }
                mTradeBean.setType(1);
                updateTradeInfo();
                break;
            case R.id.ll_select_date:
                /*Calendar calendar = Calendar.getInstance();
                mDatePickerDialog.
                        getDatePicker().
                        updateDate(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));*/
                mDatePickerDialog.show();
                break;
            case R.id.ll_select_user_name:
                showProgressDialog();
                Map<String, Object> params = new ArrayMap<>();
                params.put("parentOid", Apl.getInstance().getUserOid());
                OkHttpClientManager.getInstance().asyncGetParams(ApiUrl.MEMBER_USER_LIST, params, new OkHttpClientManager.HttpResultCallback<List<MemberUserBean>>() {
                    @Override
                    public void onSuccess(final List<MemberUserBean> data) {
                        dismissProgressDialog();
                        List<MemberUserBean> memberUserList = JSON.parseArray(JSON.toJSONString(data), MemberUserBean.class);
                        mMemberUser = new ArrayMap<>();
                        for (MemberUserBean memberUserBean : memberUserList) {
                            //因为在添加新的用户时有对用户名进行查重，所有这里添加不用担心
                            mMemberUser.put(memberUserBean.getUserName(), memberUserBean.getOid());
                        }
                        showMemberUserSelectDialog();
                    }

                    @Override
                    public void onError(final String msg) {
                        simpleError(msg);
                    }
                });
                break;
        }
    }

    private void updateTradeInfo() {
        showProgressDialog();
        String userOid = (String) SharedPreferencesUtils.getParam(mContext, Constant.USER_LOGIN_OID, "");
        mTradeBean.setParentOid(userOid);
        OkHttpClientManager.
                getInstance().
                asyncPostJson(
                        ApiUrl.ADD_INCOME_RECORD,
                        JSON.toJSONString(mTradeBean),
                        new OkHttpClientManager.HttpResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                dismissProgressDialog();
                                if (data) {
                                    finish();
                                }
                            }

                            @Override
                            public void onError(String msg) {
                                simpleError(msg);
                            }
                        });
    }

    /**
     * 显示成员用户选择框
     */
    private void showMemberUserSelectDialog() {
        Set<String> memberUserNames = mMemberUser.keySet();
        new MaterialDialog.Builder(mContext)
                .title("请选择用户")
                .items(memberUserNames)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which < 0) {
                            return false;
                        }
                        mDataBinding.tvSelectName.setText(text);
                        getTradeBean().setUserOid(mMemberUser.get(text));
                        return true;
                    }
                })
                .positiveText("确认")
                .show();
    }

    private TradeItemBean getTradeBean() {
        if (mTradeBean == null) {
            mTradeBean = new TradeItemBean();
        }
        return mTradeBean;
    }
}
