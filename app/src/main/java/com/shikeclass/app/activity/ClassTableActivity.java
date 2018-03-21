package com.shikeclass.app.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.shikeclass.app.R;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.ClassBean;
import com.shikeclass.app.dialog.EditClassInfoDialog;
import com.shikeclass.app.eventbus.UpdateTableEvent;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.DataUtils;
import com.shikeclass.app.utils.DialogUtils;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.view.CustomToolbar;
import com.shikeclass.app.view.WeekTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    @BindView(R.id.toolbar)
    CustomToolbar toolbar;

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
    private List<ClassBean> data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_class_table;
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_class_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_help:
                DialogUtils.showDialog(mActivity, "帮助", "#长按相应课程可修改或删除课程");
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_class:
                EditClassInfoDialog dialog = EditClassInfoDialog.newInstance(null, new EditClassInfoDialog.EditCallback() {
                    @Override
                    public void onEdit(ClassBean bean) {
                        showToast("添加成功");
                        addData(bean);
                        refreshData();
                        Gson gson = new Gson();
                        SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_CLASS_TABLE, gson.toJson(data));
                        EventBus.getDefault().post(new UpdateTableEvent(1));
                    }

                    @Override
                    public void onDelete(ClassBean bean) {

                    }
                }, EditClassInfoDialog.ADD_MODE);
                dialog.show(getSupportFragmentManager(), "dialog_add_class_info");

        }

        return true;
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

        data = DataUtils.getClassTableData(mActivity);

        if (data == null) {
            DialogUtils.showDialog(mActivity, "提示", "请先导入课程，导入前请确保手机已连接至HQU", "导入", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(ImportClassActivity.class);
                }
            });
        } else {
            if (SharedPreUtil.getBooleanValue(mActivity, CommonValue.SHA_FIRST_USE, true)) {
                DialogUtils.showDialog(mActivity, "帮助", "#长按相应课程可修改或删除课程");
                SharedPreUtil.putBooleanValue(mActivity, CommonValue.SHA_FIRST_USE, false);
            }
            refreshData();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTableEvent(UpdateTableEvent event) {
        if (event.getMsg() == 0) {
            setWeek();
            getData();
        }
    }

    private void addData(ClassBean bean) {
        if (data == null)
            data = new ArrayList<>();
        data.add(bean);

        Collections.sort(data, new Comparator<ClassBean>() {
            @Override
            public int compare(ClassBean o1, ClassBean o2) {
                if (o1.weekDay > o2.weekDay)
                    return 1;
                else if (o1.weekDay < o2.weekDay)
                    return -1;
                else {
                    if (o1.startClass > o2.startClass)
                        return 1;
                    else if (o1.startClass < o2.startClass)
                        return -1;
                    else {
                        if (o1.startWeek > o2.startWeek)
                            return 1;
                        else if (o1.startWeek < o2.startWeek)
                            return -1;
                        else
                            return 0;

                    }
                }
            }
        });
    }


    private void addClassTextView(ClassBean bean) {
        addClassTextView(bean, bean.name, bean.address, bean.weekDay, bean.startClass, bean.endClass, bean.colorId);
    }

    private void addClassTextView(final ClassBean bean, String name, String address, int weekDay, int start, int end, int colorId) {
        AppCompatTextView textView = new AppCompatTextView(mActivity);
        textView.setText(name + "@" + address);
        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        textView.setBackgroundColor(colorId);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(dp2, dp6, dp2, dp6);
        textView.setTextSize(12);
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
        textView.setTag(bean);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditClassInfoDialog dialog = EditClassInfoDialog.newInstance(bean, new EditClassInfoDialog.EditCallback() {
                    @Override
                    public void onEdit(ClassBean bean) {
                        showToast("修改成功");
                        data.remove(bean);
                        addData(bean);
                        refreshData();
                        Gson gson = new Gson();
                        SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_CLASS_TABLE, gson.toJson(data));
                        EventBus.getDefault().post(new UpdateTableEvent(1));
                    }

                    @Override
                    public void onDelete(ClassBean bean) {
                        showToast("删除成功");
                        data.remove(bean);
                        refreshData();
                        Gson gson = new Gson();
                        SharedPreUtil.putStringValue(mActivity, CommonValue.SHA_CLASS_TABLE, gson.toJson(data));
                        EventBus.getDefault().post(new UpdateTableEvent(1));
                    }
                });
                dialog.show(getSupportFragmentManager(), "dialog_edit_class_info");
                return true;
            }
        });

        classContent.addView(textView);
    }

    private void refreshData() {
        if (classContent.getChildCount() != 0)
            classContent.removeAllViews();
        int size = data.size();
        int colorSize = colorList.size();
        ClassBean preClass = null, bean;
        for (int i = 0; i < size; i++) {
            bean = data.get(i);
            if (isConflictClass(preClass, bean))
                continue;
            else if (shouldHaveClass(bean))
                bean.colorId = colorList.get(i % colorSize);
            else bean.colorId = ContextCompat.getColor(mActivity, R.color.grey_7a7a7a);

            addClassTextView(bean);
            preClass = bean;
        }
    }

    private int convertWeekDay(int weekDay) {
        return (weekDay + 1) % 7;
    }

    private boolean shouldHaveClass(ClassBean bean) {
        if (isDuringTerm && bean.startWeek <= currentWeek && currentWeek <= bean.endWeek) {
            if (bean.isSingleOrDouble == 1 && currentWeek % 2 == 1)
                return true;
            else if (bean.isSingleOrDouble == 2 && currentWeek % 2 == 0)
                return true;
            else if (bean.isSingleOrDouble == 0)
                return true;

        }
        return false;
    }

    private boolean isConflictClass(ClassBean class1, ClassBean class2) {
        return class1 != null && class1.weekDay == class2.weekDay && class1.startClass == class2.startClass;
    }
}
