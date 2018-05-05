package com.shikeclass.app.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.R;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.ServerClassBean;
import com.shikeclass.app.eventbus.LoginEvent;
import com.shikeclass.app.network.JSONObjectCallback;
import com.shikeclass.app.network.JsonCallback;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.NetworkErrorUtils;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

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
    @BindView(R.id.spinner_type)
    NiceSpinner spinnerType;


    private String[] typeStr = {"学生", "教师"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        String stuName, stuId;
//        stuName = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_NAME, "");
//        stuId = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, "");
//        if (!TextUtils.isEmpty(stuName) && !TextUtils.isEmpty(stuId)) {
//            userId.setText(stuId);
//            userName.setText(stuName);
//        }

        int userType = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0);

        if (userType == 0) {
            userNameLayout.setHint("姓名");
            userIdLayout.setHint("学号");
            userId.setInputType(InputType.TYPE_CLASS_NUMBER);
            userIdLayout.setPasswordVisibilityToggleEnabled(false);
        } else {
            userNameLayout.setHint("教师工号");
            userIdLayout.setHint("密码");
            userIdLayout.setPasswordVisibilityToggleEnabled(true);
            userId.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        spinnerType.attachDataSource(Arrays.asList(typeStr));
        spinnerType.setSelectedIndex(userType);
    }

    @Override
    public void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdLayout.setErrorEnabled(false);
                userNameLayout.setErrorEnabled(false);
                String stuName, stuId;
                stuName = userName.getText().toString();
                stuId = userId.getText().toString();
                if (TextUtils.isEmpty(stuName)) {
                    if (spinnerType.getSelectedIndex() == 0)
                        showError(userNameLayout, "请输入姓名");
                    else
                        showError(userNameLayout, "请输入教师工号");
                } else if (TextUtils.isEmpty(stuId)) {
                    if (spinnerType.getSelectedIndex() == 0)
                        showError(userIdLayout, "请输入学号");
                    else
                        showError(userIdLayout, "请输入密码");
                } else {
                    login(stuName, stuId);
                }
            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    userNameLayout.setHint("姓名");
                    userIdLayout.setHint("学号");
                    userId.setInputType(InputType.TYPE_CLASS_NUMBER);
                    userIdLayout.setPasswordVisibilityToggleEnabled(false);
                } else if (position == 1) {
                    userNameLayout.setHint("教师工号");
                    userIdLayout.setHint("密码");
                    userIdLayout.setPasswordVisibilityToggleEnabled(true);
                    userId.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                userId.setText("");
                userName.setText("");
                userIdLayout.setErrorEnabled(false);
                userNameLayout.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().requestFocus();
    }

    private void login(final String stuName, final String stuId) {
        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("正在验证");
        dialog.show();
        if (spinnerType.getSelectedIndex() == 0) {
            OkGo.<List<ServerClassBean>>post(UrlUtils.studentLogin)
                    .tag(mActivity)
                    .params("student_id", stuId)
                    .params("student_name", stuName)
                    .execute(new JsonCallback<List<ServerClassBean>>(new TypeToken<List<ServerClassBean>>() {
                    }.getType()) {
                        @Override
                        public void onSuccess(Response<List<ServerClassBean>> response) {
                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_NAME, stuName);
                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_ID, stuId);
                            SharedPreUtil.putIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0);
                            SharedPreUtil.putBooleanValue(mActivity, CommonValue.SHA_IS_LOGIN, true);
                            Gson gson = new Gson();
                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_SERVER_CLASS_TABLE, gson.toJson(response.body()));
                            dialog.dismiss();
                            showToast("验证成功");
                            EventBus.getDefault().post(new LoginEvent(0, response.body()));
                            finish();
                        }

                        @Override
                        public void onError(Response<List<ServerClassBean>> response) {
                            super.onError(response);
                            if (NetworkErrorUtils.isNetworkError(response.getException()))
                                showToast("网络异常，验证失败");
                            else if (response.getException().getMessage().equals("100")) {
//                                showToast("学号或姓名不正确");
                                showError(userIdLayout, "学号或姓名不正确");
                            }
                            dialog.dismiss();
                        }
                    });
        } else {
            OkGo.<JSONObject>post(UrlUtils.teacherLogin)
                    .tag(mActivity)
                    .params("teacher_id", stuName)
                    .params("password", stuId)
                    .execute(new JSONObjectCallback() {
                        @Override
                        public void onSuccess(Response<JSONObject> response) {
                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_NAME, stuName);
//                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_STU_ID, stuId);
                            SharedPreUtil.putIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 1);
                            SharedPreUtil.putBooleanValue(mActivity, CommonValue.SHA_IS_LOGIN, true);
                            JSONObject teacher = response.body().optJSONObject("teacher");
                            SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_TEACHER_NAME, teacher.optString("teacher_name"));
                            Gson gson = new Gson();
                            List<ServerClassBean> data = gson.fromJson(response.body().optString("lesson"),
                                    new TypeToken<List<ServerClassBean>>() {
                                    }.getType());

                            dialog.dismiss();
                            showToast("验证成功");
                            EventBus.getDefault().post(new LoginEvent(1, data));
                            finish();
                        }

                        @Override
                        public void onError(Response<JSONObject> response) {
                            super.onError(response);
                            if (NetworkErrorUtils.isNetworkError(response.getException()))
                                showToast("网络异常，验证失败");
                            else if (response.getException().getMessage().equals("100")) {
//                                showToast("学号或姓名不正确");
                                showError(userIdLayout, "教师工号或密码不正确");
                            }
                            dialog.dismiss();
                        }
                    });
        }


    }
}
