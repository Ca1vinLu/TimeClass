package com.shikeclass.app.student.activity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.TextView;

import com.shikeclass.app.student.R;
import com.shikeclass.app.student.base.BaseActivity;

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

    }

    @Override
    public void initListener() {

    }
}
