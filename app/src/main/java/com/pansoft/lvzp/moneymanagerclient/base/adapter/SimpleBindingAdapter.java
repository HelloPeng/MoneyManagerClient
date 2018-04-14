package com.pansoft.lvzp.moneymanagerclient.base.adapter;

import android.databinding.ViewDataBinding;

/**
 * 作者：吕振鹏
 * 创建时间：08月30日
 * 时间：15:49
 * 版本：v1.0.0
 * 类描述：普通对数据绑定Adapter
 * 修改时间：
 */

public class SimpleBindingAdapter<T> extends BindingBaseRecycleAdapter<T, ViewDataBinding> {

    private int mVariableId;

    public SimpleBindingAdapter(int resLayoutId, int variableId) {
        super(resLayoutId);
        mVariableId = variableId;
    }

    @Override
    protected void bindingViews(BindingViewHolder<ViewDataBinding> holder, int position, T t) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(mVariableId, t);
        binding.executePendingBindings();
    }
}
