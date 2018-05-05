package com.shikeclass.app.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;
import com.shikeclass.app.bean.SignBean;

/**
 * Created by LYZ on 2018/3/27 0027.
 */

public class SignAdapter extends BaseQuickAdapter<SignBean, BaseViewHolder> {
    private int userType;

    public SignAdapter(int userType) {
        super(R.layout.item_sign_stu);
        this.userType = userType;
    }


    @Override
    protected void convert(BaseViewHolder helper, SignBean item) {
        if (userType == 0)
            helper.setText(R.id.lesson_name, item.lesson_name);
        else
            helper.setText(R.id.lesson_name, item.student_name);
        helper.setText(R.id.student_id, item.student_id);

        switch (item.exist) {
            case "0":
                helper.setText(R.id.sign_state, "未\n签\n到");
                ((TextView) helper.getView(R.id.sign_state)).setTextSize(14);
                helper.setBackgroundColor(R.id.sign_state, ContextCompat.getColor(mContext, R.color.yellow_ffcc00));
                break;
            case "1":
                helper.setText(R.id.sign_state, "正\n常");
                ((TextView) helper.getView(R.id.sign_state)).setTextSize(16);
                helper.setBackgroundColor(R.id.sign_state, ContextCompat.getColor(mContext, R.color.green_22ac38));
                break;
            case "2":
                helper.setText(R.id.sign_state, "暂\n离");
                ((TextView) helper.getView(R.id.sign_state)).setTextSize(16);
                helper.setBackgroundColor(R.id.sign_state, ContextCompat.getColor(mContext, R.color.yellow_ffcc00));
                break;
            case "3":
                helper.setText(R.id.sign_state, "旷\n课");
                ((TextView) helper.getView(R.id.sign_state)).setTextSize(16);
                helper.setBackgroundColor(R.id.sign_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
        }


    }
}
