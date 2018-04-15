package com.pansoft.lvzp.moneymanagerclient.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.text.TextUtils;
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


    /**
     * @param context
     * @param isIncome 是否为收入
     */
    public static void actionStart(Activity activity, boolean isIncome, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, EditAndDetailPropertyActivity.class);
        intent.putExtra("is_income", isIncome);
        activity.startActivityForResult(intent, requestCode);
    }

    private boolean isIncome;
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
        isIncome = getIntent().getBooleanExtra("is_income", false);
        mDataBinding.setIsIncome(isIncome);
        openBackIcon();
        mDataBinding.rdoGroupIncomeTag.setOnCheckedChangeListener(this);
        mDataBinding.rdoGroupDefrayTag.setOnCheckedChangeListener(this);
        mDataBinding.editInputMoney.setFilters(new InputFilter[]{new CashierInputFilter()});
        Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + format(String.valueOf(month + 1)) + "-" + String.valueOf(dayOfMonth);
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
        if (checkedId == R.id.rdoBtn_income_other || checkedId == R.id.rdoBtn_defray_other) {
            isSelectOther = true;
            mDataBinding.editInputOther.setVisibility(View.VISIBLE);
            EditTextUtils.reqsetGetFocus(mDataBinding.editInputOther);
        } else {
            TextView childView = findViewById(checkedId);
            getTradeBean().setConsumeTag(childView.getText().toString());
            isSelectOther = false;
            mDataBinding.editInputOther.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("DefaultLocale")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (isSelectOther) {
                    if (TextUtils.isEmpty(mDataBinding.editInputOther.getText())) {
                        showToast("请填写其他类型的详细信息");
                        return;
                    }
                    mTradeBean.setConsumeTag(mDataBinding.editInputOther.getText().toString());
                }
                if (TextUtils.isEmpty(mDataBinding.editInputMoney.getText())) {
                    showToast("请填写具体的金额");
                    return;
                }
                String money = mDataBinding.editInputMoney.getText().toString();
                mTradeBean.setMoney(String.format("%.2f", Float.parseFloat(money)));
                if (!TextUtils.isEmpty(mDataBinding.editInputRemarks.getText())) {
                    mTradeBean.setRemarks(mDataBinding.editInputRemarks.getText().toString());
                }
                String userOid = (String) SharedPreferencesUtils.getParam(mContext, Constant.USER_LOGIN_OID, "");
                mTradeBean.setParentOid(userOid);
                if (isIncome) {
                    mTradeBean.setType(1);
                } else {
                    mTradeBean.setType(0);
                }

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
        OkHttpClientManager.
                getInstance().
                asyncPostJson(
                        ApiUrl.ADD_TRADE_RECORD,
                        mTradeBean,
                        new OkHttpClientManager.HttpResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                dismissProgressDialog();
                                if (data) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(String msg) {
                                simpleError(msg);
                            }
                        });
    }

    private String format(String data) {
        if (data.length() == 1) {
            data = "0" + data;
        }
        return data;
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
