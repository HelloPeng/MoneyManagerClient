package com.pansoft.lvzp.moneymanagerclient.base.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：吕振鹏
 * 创建时间：08月30日
 * 时间：15:40
 * 版本：v1.0.0
 * 类描述：数据绑定的ViewHolder
 * 修改时间：
 */

public class BindingViewHolder<D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private BindingBaseRecycleAdapter.OnItemClickListener mOnItemClickListener;
    private BindingBaseRecycleAdapter.OnItemLongClickListener mOnItemLongClickListener;
    private D mBinding;

    public BindingViewHolder(final D binding) {
        super(binding.getRoot());
        mBinding = binding;
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(
                            BindingViewHolder.this,
                            binding.getRoot(),
                            getLayoutPosition());
            }
        });
        binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null)
                    mOnItemLongClickListener.onItemLongClick(
                            BindingViewHolder.this,
                            binding.getRoot(),
                            getLayoutPosition());
                return mOnItemClickListener != null;
            }
        });
    }

    public D getBinding() {
        return mBinding;
    }

    public void setOnItemClickListener(BindingBaseRecycleAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(BindingBaseRecycleAdapter.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

}
