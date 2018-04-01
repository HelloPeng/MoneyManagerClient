package com.pansoft.lvzp.moneymanagerclient.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.pansoft.lvzp.moneymanagerclient.R;
import com.pansoft.lvzp.moneymanagerclient.base.TitleBaseDialog;
import com.pansoft.lvzp.moneymanagerclient.databinding.LayoutDialogAddMemberUserBinding;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class AddMemberUserDialog extends TitleBaseDialog<LayoutDialogAddMemberUserBinding> {

    public AddMemberUserDialog(@NonNull Context context) {
        super(context);
        setTitleText("添加新用户");
        setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void resetInputMessage() {
        mDataBinding.editNo.setText("");
        mDataBinding.editName.setText("");
        mDataBinding.editPhone.setText("");
    }

    @Override
    protected int getChildLayoutId() {
        return R.layout.layout_dialog_add_member_user;
    }

    public String getUserNo() {
        return mDataBinding.editNo.getText().toString();
    }

    public String getUserName() {
        return mDataBinding.editName.getText().toString();
    }

    public String getUserPhone() {
        return mDataBinding.editPhone.getText().toString();
    }
}
