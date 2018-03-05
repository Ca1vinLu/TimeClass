package com.shikeclass.student.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shikeclass.student.R;
import com.shikeclass.student.base.BaseActivity;
import com.shikeclass.student.bean.ClassBean;
import com.shikeclass.student.eventbus.UpdateTableEvent;
import com.shikeclass.student.utils.CommonValue;
import com.shikeclass.student.utils.DataUtils;
import com.shikeclass.student.utils.DialogUtils;
import com.shikeclass.student.utils.SharedPreUtil;
import com.shikeclass.student.view.WeekTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ClassTableActivity extends BaseActivity {


    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.class_content)
    RelativeLayout classContent;
    @BindView(R.id.week_name)
    TextView weekName;
    @BindView(R.id.sunday)
    WeekTextView sunday;
    @BindView(R.id.monday)
    WeekTextView monday;
    @BindView(R.id.tuesday)
    WeekTextView tuesday;
    @BindView(R.id.wednesday)
    WeekTextView wednesday;
    @BindView(R.id.thursday)
    WeekTextView thursday;
    @BindView(R.id.friday)
    WeekTextView friday;
    @BindView(R.id.saturday)
    WeekTextView saturday;

    private int classWith;
    private int todayClassWidth;
    private int classHeight;
    private int centerTipHeight;
    private int lineHeight;
    private int dp6 = ConvertUtils.dp2px(6);
    private int dp2 = ConvertUtils.dp2px(2);
    private int currentWeek = 1;
    private int currentWeekDay = 1;
    private String startDay;
    private boolean isDuringTerm = true;

    private List<Integer> colorList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_class_table;
    }

    @Override
    public void initView() {
        setWeek();
        setDate();
        classContent.post(new Runnable() {
            @Override
            public void run() {
                classWith = classContent.getWidth() * 2 / 15;
                todayClassWidth = classContent.getWidth() / 5;
                classHeight = text1.getHeight() + text2.getHeight() + line.getHeight();
                centerTipHeight = text3.getHeight() + line.getHeight();
                lineHeight = line.getHeight();
                getData();
            }
        });
    }

    @Override
    public void initData() {

    }

    private void setWeek() {
        startDay = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_TERM_START_TIME, "2018-03-05");

        Date startDay = TimeUtils.string2Date(this.startDay, new SimpleDateFormat("yyyy-MM-dd"));
        int termWeeks = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_TERM_WEEKS, 20);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        calendar.add(Calendar.WEEK_OF_YEAR, termWeeks);
        calendar.add(Calendar.DATE, -1);
        Date endDay = calendar.getTime();
        Date today = new Date(System.currentTimeMillis());
        if (today.before(startDay) || today.after(endDay)) {
            isDuringTerm = false;
            weekName.setText("放假中");
            DialogUtils.showDialog(mActivity, "提示", "放假中，请及时校准下学期开学时间", "校准", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(AdjustTermActivity.class);
                }
            });
        } else {
            isDuringTerm = true;
            long spanDays = TimeUtils.getTimeSpan(startDay, new Date(System.currentTimeMillis()), TimeConstants.DAY);
            currentWeek = (int) (spanDays / 7 + 1);
            weekName.setText("第" + currentWeek + "周");
        }


    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -(currentWeekDay - 1));

        sunday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        monday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        tuesday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        wednesday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        thursday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        friday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 1);
        saturday.setData((calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE));

        switch (currentWeekDay) {
            case 1:
                ((LinearLayout.LayoutParams) sunday.getLayoutParams()).weight = 3;
                sunday.isToday();
                break;
            case 2:
                ((LinearLayout.LayoutParams) monday.getLayoutParams()).weight = 3;
                monday.isToday();
                break;
            case 3:
                ((LinearLayout.LayoutParams) tuesday.getLayoutParams()).weight = 3;
                tuesday.isToday();
                break;
            case 4:
                ((LinearLayout.LayoutParams) wednesday.getLayoutParams()).weight = 3;
                wednesday.isToday();
                break;
            case 5:
                ((LinearLayout.LayoutParams) thursday.getLayoutParams()).weight = 3;
                thursday.isToday();
                break;
            case 6:
                ((LinearLayout.LayoutParams) friday.getLayoutParams()).weight = 3;
                friday.isToday();
                break;
            case 7:
                ((LinearLayout.LayoutParams) saturday.getLayoutParams()).weight = 3;
                saturday.isToday();
                break;

        }
    }

    private void getData() {
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color1));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color2));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color3));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color4));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color5));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color6));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color7));

        List<ClassBean> data = DataUtils.getClassTableData(mActivity);

        if (data == null) {
            DialogUtils.showDialog(mActivity, "提示", "请先导入课程，导入前请确保手机已连接至HQU", "导入", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(ImportClassActivity.class);
                }
            });
            return;
        }

        int size = data.size();
        int colorSize = colorList.size();
        for (int i = 0; i < size; i++) {
            ClassBean bean = data.get(i);
            if (shouldHaveClass(bean))
                bean.colorId = colorList.get(i % colorSize);
            else bean.colorId = ContextCompat.getColor(mActivity, R.color.grey_7a7a7a);
            addClassTextView(bean);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(UpdateTableEvent event) {
        setWeek();
        getData();
    }


    private void addClassTextView(ClassBean bean) {
        addClassTextView(bean.name, bean.address, bean.weekDay, bean.startClass, bean.endClass, bean.colorId);
    }

    private void addClassTextView(String name, String address, int weekDay, int start, int end, int colorId) {
        TextView textView = new TextView(mActivity);
        textView.setText(name + "@" + address);
        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        textView.setBackgroundColor(colorId);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(dp2, dp6, dp2, dp6);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        RelativeLayout.LayoutParams layoutParams;
        int convertWeekDay = convertWeekDay(weekDay);
        if (currentWeekDay != convertWeekDay)
            layoutParams = new RelativeLayout.LayoutParams(classWith, classHeight * (end - start + 1) - lineHeight);
        else
            layoutParams = new RelativeLayout.LayoutParams(todayClassWidth, classHeight * (end - start + 1) - lineHeight);

        int marginTop = 0;

        if (start <= 4) {
            marginTop += (start - 1) * classHeight;
        } else if (start <= 10) {
            marginTop += (start - 3) * classHeight + centerTipHeight;
        } else {
            marginTop += (start - 3) * classHeight + centerTipHeight * 2;

        }
        if (convertWeekDay <= currentWeekDay)
            layoutParams.setMargins(classWith * (convertWeekDay - 1), marginTop, 0, 0);
        else
            layoutParams.setMargins(classWith * (convertWeekDay - 2) + todayClassWidth, marginTop, 0, 0);

        textView.setLayoutParams(layoutParams);

        classContent.addView(textView);
    }

    private int convertWeekDay(int weekDay) {
        return (weekDay + 1) % 7;
    }

    private boolean shouldHaveClass(ClassBean bean) {
        if (isDuringTerm && bean.startWeek <= currentWeek && currentWeek <= bean.endWeek) {
            if (bean.isSingleOrDouble == 0)
                return true;
            if (bean.isSingleOrDouble == 1 && currentWeek % 2 == 0)
                return false;
            else if (bean.isSingleOrDouble == 2 && currentWeek % 2 == 1)
                return false;

        }
        return false;
    }
}
