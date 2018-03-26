package com.shikeclass.app.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shikeclass.app.R;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.eventbus.UpdateTableEvent;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.DialogUtils;
import com.shikeclass.app.utils.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;

public class AdjustTermActivity extends BaseActivity {


    @BindView(R.id.term_time)
    TextView termTime;
    @BindView(R.id.btn_adjust_term)
    LinearLayout btnAdjustTerm;
    @BindView(R.id.term_week)
    TextInputEditText termWeek;
    @BindView(R.id.term_week_layout)
    TextInputLayout termWeekLayout;
    @BindView(R.id.btn_adjust)
    TextView btnAdjust;

    @Override
    public int getLayoutId() {
        return R.layout.activity_adjust_term;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String termStartDay = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_TERM_START_TIME, "2018-03-05");
        termTime.setText(termStartDay);

    }

    @Override
    public void initListener() {
        btnAdjustTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                        termTime.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termWeekLayout.setErrorEnabled(false);
                if (TextUtils.isEmpty(termWeek.getText().toString().trim())) {
                    showError(termWeekLayout, "请输入周数");
                    return;
                } else if (Integer.valueOf(termWeek.getText().toString().trim()) <= 0) {
                    showError(termWeekLayout, "周数最小为1");
                    return;
                }
                SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_TERM_START_TIME, termTime.getText().toString());
                SharedPreUtil.putIntValue(mActivity, CommonValue.SHA_TERM_WEEKS, Integer.parseInt(termWeek.getText().toString()));
                DialogUtils.showDialog(mActivity, "提示", "校准成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new UpdateTableEvent());
                        finish();
                    }
                });
            }
        });
    }

    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().requestFocus();
    }
}
