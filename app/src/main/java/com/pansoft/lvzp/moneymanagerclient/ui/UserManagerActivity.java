package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.pansoft.lvzp.moneymanagerclient.BR;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.TkRefreshHelper;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.base.adapter.SimpleBindingAdapter;
import com.pansoft.lvzp.moneymanagerclient.bean.MemberUserBean;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityUserManagerBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;
import com.pansoft.lvzp.moneymanagerclient.widget.AddMemberUserDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManagerActivity
        extends BaseActivity<ActivityUserManagerBinding>
        implements TkRefreshHelper.OnLoadPagerListener {


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserManagerActivity.class);
        context.startActivity(intent);
    }

    private List<MemberUserBean> mListData = new ArrayList<>();
    private TkRefreshHelper<MemberUserBean> mRefreshHelper;
    private SimpleBindingAdapter<MemberUserBean> mAdapter;
    private AddMemberUserDialog mAddMemberUserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_manager;
    }

    @Override
    protected void initViews() {
        openBackIcon();
        initRefreshLayout();
        mDataBinding.refreshLayout.startRefresh();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_member_user:
                initAddMemberUserDialog();
                mAddMemberUserDialog.show();
                break;
        }
    }

    @Override
    public void onLoadPage(int page) {
        Map<String, Object> params = new ArrayMap<>();
        params.put("parentOid", Apl.getInstance().getUserOid());
        params.put("page", page);
        params.put("pageNum", 10);
        OkHttpClientManager.getInstance().asyncGetParams(ApiUrl.MEMBER_USER_LIST, params, new OkHttpClientManager.HttpResultCallback<List<MemberUserBean>>() {
            @Override
            public void onSuccess(final List<MemberUserBean> data) {
                List<MemberUserBean> fastList = JSON.parseArray(JSON.toJSONString(data), MemberUserBean.class);
                mListData.addAll(fastList);
                mAdapter.setupData(mListData);
                mAdapter.notifyDataSetChanged();
                mRefreshHelper.setupListData(mListData);
                mRefreshHelper.notifyRefreshFinish(fastList.size() < 10);
            }

            @Override
            public void onError(final String msg) {
                simpleError(msg);
                mRefreshHelper.notifyRefreshFinish(false);
            }
        });
    }

    private void initAddMemberUserDialog() {
        if (mAddMemberUserDialog == null) {
            mAddMemberUserDialog = new AddMemberUserDialog(mContext);
            mAddMemberUserDialog.setOnConfirmClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userNo = mAddMemberUserDialog.getUserNo();
                    String userName = mAddMemberUserDialog.getUserName();
                    String userPhone = mAddMemberUserDialog.getUserPhone();
                    if (TextUtils.isEmpty(userNo) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPhone)) {
                        showToast("请将信息填写完整");
                        return;
                    }
                    MemberUserBean userBean = new MemberUserBean();
                    userBean.setUserName(userName);
                    userBean.setUserNo(userNo);
                    userBean.setUserPhone(userPhone);
                    userBean.setParentOid(Apl.getInstance().getUserOid());
                    showProgressDialog();
                    OkHttpClientManager.getInstance().asyncPostJson(ApiUrl.ADD_MEMBER_USER, userBean, new OkHttpClientManager.HttpResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissProgressDialog();
                                    mAddMemberUserDialog.resetInputMessage();
                                    mAddMemberUserDialog.dismiss();
                                    showToast("添加成功");
                                    mDataBinding.refreshLayout.startRefresh();
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            simpleError(msg);
                        }
                    });
                }
            });
        }
    }

    private void initRefreshLayout() {
        mAdapter = new SimpleBindingAdapter<>(R.layout.item_layout_user, BR.memberUserBean);
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshHelper = new TkRefreshHelper<>(mDataBinding.refreshLayout);
        mRefreshHelper.buildSimpleVerticalLayout();
        mRefreshHelper.setOnLoadPagerListener(this);
    }

}
