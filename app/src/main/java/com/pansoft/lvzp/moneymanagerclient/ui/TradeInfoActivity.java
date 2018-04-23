package com.pansoft.lvzp.moneymanagerclient.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.pansoft.lvzp.moneymanagerclient.BR;
import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.TkRefreshHelper;
import com.pansoft.lvzp.moneymanagerclient.base.Apl;
import com.pansoft.lvzp.moneymanagerclient.base.BaseActivity;
import com.pansoft.lvzp.moneymanagerclient.base.adapter.BindingViewHolder;
import com.pansoft.lvzp.moneymanagerclient.base.adapter.SimpleBindingAdapter;
import com.pansoft.lvzp.moneymanagerclient.bean.TradeItemBean;
import com.pansoft.lvzp.moneymanagerclient.databinding.ActivityTradeInfoBinding;
import com.pansoft.lvzp.moneymanagerclient.databinding.ItemLayoutTradeInfoBinding;
import com.pansoft.lvzp.moneymanagerclient.http.ApiUrl;
import com.pansoft.lvzp.moneymanagerclient.http.OkHttpClientManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeInfoActivity
        extends BaseActivity<ActivityTradeInfoBinding>
        implements TkRefreshHelper.OnLoadPagerListener {


    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TradeInfoActivity.class);
        context.startActivity(intent);
    }

    private SimpleBindingAdapter<TradeItemBean> mAdapter;
    private TkRefreshHelper<TradeItemBean> mTkRefreshHelper;
    private List<TradeItemBean> mListData = new ArrayList<>();


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
        mDataBinding.refreshLayout.startRefresh();
        mAdapter = new SimpleBindingAdapter<TradeItemBean>(R.layout.item_layout_trade_info, BR.tradeInfoBean) {
            @Override
            protected void bindingViews(BindingViewHolder<ViewDataBinding> holder, final int position, final TradeItemBean tradeItemBean) {
                super.bindingViews(holder, position, tradeItemBean);
                final ItemLayoutTradeInfoBinding binding = (ItemLayoutTradeInfoBinding) holder.getBinding();
                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        Map<String, Object> params = new ArrayMap<>();
                        params.put("oid", tradeItemBean.getOid());
                        OkHttpClientManager.getInstance().asyncPost(ApiUrl.DEL_TRADE_TRADE, params, new OkHttpClientManager.HttpResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                dismissProgressDialog();
                                binding.swipeLayout.smoothClose();
                                if (data) {
                                    mAdapter.removeItem(position);
                                }
                            }

                            @Override
                            public void onError(String msg) {
                                simpleError(msg);
                                binding.swipeLayout.smoothClose();
                            }
                        });
                    }
                });
            }
        };
        mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mTkRefreshHelper = new TkRefreshHelper<>(mDataBinding.refreshLayout);
        mTkRefreshHelper.buildSimpleVerticalLayout();
        mTkRefreshHelper.setOnLoadPagerListener(this);
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
                EditAndDetailPropertyActivity.actionStart(this, false, 1);
                break;
            case R.id.menu_item_income:
                EditAndDetailPropertyActivity.actionStart(this, true, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当加载页码适合的回调
     *
     * @param page
     */
    @Override
    public void onLoadPage(int page) {
        Map<String, Object> params = new ArrayMap<>();
        params.put("parentOid", Apl.getInstance().getUserOid());
        params.put("page", page);
        params.put("pageNum", 10);
        OkHttpClientManager.getInstance().asyncGetParams(ApiUrl.TRADE_INFO_LIST, params, new OkHttpClientManager.HttpResultCallback<List<TradeItemBean>>() {
            @Override
            public void onSuccess(final List<TradeItemBean> data) {
                List<TradeItemBean> itemBeans = JSON.parseArray(JSON.toJSONString(data), TradeItemBean.class);
                mListData.addAll(itemBeans);
                mAdapter.setupData(mListData);
                mAdapter.notifyDataSetChanged();
                mTkRefreshHelper.setupListData(mListData);
                mTkRefreshHelper.notifyRefreshFinish(itemBeans.size() < 10);
            }

            @Override
            public void onError(final String msg) {
                simpleError(msg);
                mTkRefreshHelper.notifyRefreshFinish(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    mDataBinding.refreshLayout.startRefresh();
                    break;
            }
        }
    }
}
