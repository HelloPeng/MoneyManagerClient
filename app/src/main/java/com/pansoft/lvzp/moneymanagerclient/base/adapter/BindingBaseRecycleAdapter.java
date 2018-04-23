package com.pansoft.lvzp.moneymanagerclient.base.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：吕振鹏
 * 创建时间：08月30日
 * 时间：15:38
 * 版本：v1.0.0
 * 类描述：抽象的RecycleView适配器
 * 修改时间：
 */

public abstract class BindingBaseRecycleAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<BindingViewHolder<D>> {


    private List<T> mListData;
    private int mResLayoutId;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BindingBaseRecycleAdapter(@LayoutRes int resLayoutId) {
        mResLayoutId = resLayoutId;
    }

    public void setupData(List<T> listData) {
        mListData = listData;
    }

    public void addItem(T itemBean) {
        addItem(itemBean, 0);
    }

    public T removeItem(T itemBean) {
        if (mListData.contains(itemBean)) {
            int indexOf = indexOf(itemBean);
            return removeItem(indexOf);
        }
        return null;
    }

    public T removeItem(int position) {
        if (mListData.size() > position) {
            T itemData = mListData.remove(position);
            notifyItemRemoved(position);
            return itemData;
        }
        return null;
    }

    public void addItem(T itemBean, int position) {
        if (itemBean == null || mListData == null)
            return;
        mListData.add(position, itemBean);
        notifyItemInserted(position);
    }

    public int indexOf(T itemBean) {
        return mListData.indexOf(itemBean);
    }

    /**
     * 获取列表的数据
     * 获取的这个数据并不是
     *
     * @return
     */
    public List<T> getListData() {
        if (mListData == null || mListData.isEmpty())
            return null;
        return new ArrayList<>(mListData);
    }

    public void setOnItemCLickListener(OnItemClickListener lickListener) {
        mOnItemClickListener = lickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public void cleanData() {
        if (mListData != null) {
            mListData.clear();
            mListData = null;
        }
    }

    public T getItem(int position) {
        if (position >= mListData.size())
            return null;
        return mListData == null ? null : mListData.get(position);
    }

    @Override
    public BindingViewHolder<D> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        D binding = DataBindingUtil.bind(inflater.inflate(mResLayoutId, parent, false));
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<D> holder, int position) {
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.setOnItemLongClickListener(mOnItemLongClickListener);
        bindingViews(holder, position, getItem(position));
    }

    protected abstract void bindingViews(BindingViewHolder<D> holder, int position, T itemBean);

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    public interface OnItemClickListener {
        /**
         * [RecyclerView]的条目点击事件
         */
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    public interface OnItemLongClickListener {
        /**
         * [RecyclerView]的条目点击事件
         */
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

}
