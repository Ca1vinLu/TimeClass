package com.shikeclass.app.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikeclass.app.R;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.eventbus.SignOutEvent;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.term_time)
    TextView termTime;
    @BindView(R.id.btn_adjust_term)
    LinearLayout btnAdjustTerm;
    @BindView(R.id.btn_about)
    LinearLayout btnAbout;
    @BindView(R.id.btn_sign_out)
    LinearLayout btnSignOut;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String termStartDay = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_TERM_START_TIME, "2018-03-05");
        int termWeeks = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_TERM_WEEKS, 20);
        termTime.setText(termStartDay + "   " + termWeeks + "周");
    }

    @Override
    public void initListener() {
        btnAdjustTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AdjustTermActivity.class);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.putBooleanValue(mActivity, CommonValue.SHA_IS_LOGIN, false);
                EventBus.getDefault().post(new SignOutEvent());
                finish();
            }
        });
    }
}
