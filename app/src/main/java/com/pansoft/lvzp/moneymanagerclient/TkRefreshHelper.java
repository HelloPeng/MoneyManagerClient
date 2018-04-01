package com.pansoft.lvzp.moneymanagerclient;

import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

/**
 * 作者：吕振鹏
 * E-mail:lvzhenpeng@pansoft.com
 * 创建时间：2018年03月20日
 * 时间：13:53
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */

public class TkRefreshHelper<T> extends RefreshListenerAdapter {

    private int mPager = 1;
    private TwinklingRefreshLayout mRefreshLayout;
    private OnLoadPagerListener mOnLoadPagerListener;
    private List<T> mListData;
    private boolean isRefresh;
    private boolean isEnd;

    public TkRefreshHelper(TwinklingRefreshLayout refreshLayout) {
        this.mRefreshLayout = refreshLayout;
    }

    public void setOnLoadPagerListener(OnLoadPagerListener listener) {
        mOnLoadPagerListener = listener;
    }

    public void buildSimpleVerticalLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setMaxHeadHeight(120);
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setFloatRefresh(true);
        mRefreshLayout.setOverScrollBottomShow(true);
    }

    @Override
    public void onRefresh(TwinklingRefreshLayout refreshLayout) {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                mPager = 1;
                if (mListData != null)
                    mListData.clear();
                mOnLoadPagerListener.onLoadPager(mPager);
            }
        }, 400);
    }

    @Override
    public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = false;
                if (isEnd) {
                    Snackbar.make(refreshLayout, "已加载全部内容", Toast.LENGTH_SHORT).show();
                    refreshLayout.finishLoadmore();
                    return;
                }
                mPager++;
                mOnLoadPagerListener.onLoadPager(mPager);
            }
        }, 400);
    }

    public void setupListData(List<T> listData) {
        mListData = listData;
    }

    public void notifyRefreshFinish(boolean isEnd) {
        this.isEnd = isEnd;
        if (isRefresh) {
            mRefreshLayout.finishRefreshing();
        } else
            mRefreshLayout.finishLoadmore();
    }

    public interface OnLoadPagerListener {
        /**
         * 当加载页码适合的回调
         *
         * @param pager
         */
        void onLoadPager(int pager);
    }

}

