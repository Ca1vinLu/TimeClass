package com.shikeclass.student.activity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shikeclass.student.R;
import com.shikeclass.student.base.BaseActivity;
import com.shikeclass.student.eventbus.LoginEvent;
import com.shikeclass.student.utils.CommonValue;
import com.shikeclass.student.utils.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.user_name)
    TextInputEditText userName;
    @BindView(R.id.user_name_layout)
    TextInputLayout userNameLayout;
    @BindView(R.id.user_id)
    TextInputEditText userId;
    @BindView(R.id.user_id_layout)
    TextInputLayout userIdLayout;
    @BindView(R.id.btn_login)
    TextView btnLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String stuName, stuId;
        stuName = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_NAME, "");
        stuId = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, "");
        if (!TextUtils.isEmpty(stuName) && !TextUtils.isEmpty(stuId)) {
            userId.setText(stuId);
            userName.setText(stuName);
        }
    }

    @Override
    public void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stuName, stuId;
                stuName = userName.getText().toString();
                stuId = userId.getText().toString();
                if (TextUtils.isEmpty(stuId)) {
                    showToast("学号不能为空");
                } else if (TextUtils.isEmpty(stuName)) {
                    showToast("姓名不能为空");
                } else {
                    SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_NAME, stuName);
                    SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_ID, stuId);
                    showToast("验证成功");
                    EventBus.getDefault().post(new LoginEvent());
                    finish();
                }
            }
        });
    }
}
