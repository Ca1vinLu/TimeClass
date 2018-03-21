package com.shikeclass.app.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikeclass.app.R;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.term_time)
    TextView termTime;
    @BindView(R.id.btn_adjust_term)
    LinearLayout btnAdjustTerm;
    @BindView(R.id.btn_about)
    LinearLayout btnAbout;

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
    }
}
