package com.shikeclass.app.student.activity;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shikeclass.app.R;
import com.shikeclass.app.student.base.BaseActivity;
import com.shikeclass.app.student.bean.ClassBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyClassActivity extends BaseActivity {


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

    private int classWith;
    private int classHeight;
    private int centerTipHeight;
    private int lineHeight;
    private int dp6 = ConvertUtils.dp2px(6);
    private int dp2 = ConvertUtils.dp2px(2);

    private List<Integer> colorList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_class;
    }

    @Override
    public void initView() {
        classContent.post(new Runnable() {
            @Override
            public void run() {
                classWith = classContent.getWidth() / 7;
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

    private void getData() {
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color1));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color2));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color3));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color4));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color5));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color6));
        colorList.add(ContextCompat.getColor(mActivity, R.color.class_color7));

        List<ClassBean> data = new ArrayList<>();
        ClassBean classBean = new ClassBean("Unix/Linux程序环境", "D2-201", 1, 1, 2);
        data.add(classBean);
        classBean = new ClassBean("软件工程创新实践", "B108", 1, 11, 13);
        data.add(classBean);
        classBean = new ClassBean("软件工程创新实践", "B108", 1, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("数据库系统概论", "F2-202", 1, 7, 9);
        data.add(classBean);
        classBean = new ClassBean("计算机组成原理", "C5-201", 2, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("数据库系统概论实验", "A329", 3, 1, 4);
        data.add(classBean);
        classBean = new ClassBean("操作系统实验", "A428", 3, 11, 13);
        data.add(classBean);
        classBean = new ClassBean("操作系统", "D1-401", 3, 7, 9);
        data.add(classBean);
        classBean = new ClassBean("软件质量保证与测试", "E1-301", 4, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("计算机图形学", "C1-101", 4, 7, 9);
        data.add(classBean);
        classBean = new ClassBean("计算机组成原理实验", "A330", 4, 11, 13);
        data.add(classBean);
        classBean = new ClassBean("数据挖掘", "C1-403", 5, 1, 2);
        data.add(classBean);
        classBean = new ClassBean("计算机组成原理", "C5-201", 5, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("数据挖掘实验", "A434", 5, 9, 10);
        data.add(classBean);


        int size = data.size();
        int colorSize = colorList.size();
        for (int i = 0; i < size; i++) {
            ClassBean bean = data.get(i);
            bean.colorId = colorList.get(i % colorSize);
            addClassTextView(bean);
        }
    }

    @Override
    public void initListener() {

    }

    private void addClassTextView(ClassBean bean) {
        addClassTextView(bean.name, bean.address, bean.week, bean.start, bean.end, bean.colorId);
    }

    private void addClassTextView(String name, String address, int week, int start, int end, int colorId) {
        TextView textView = new TextView(mActivity);
        textView.setText(name + "@" + address);
        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        textView.setBackgroundColor(colorId);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setPadding(dp2, dp6, dp2, dp6);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(classWith, classHeight * (end - start + 1) - lineHeight);
        int marginTop = 0;

        if (start <= 4) {
            marginTop += (start - 1) * classHeight;
        } else if (start <= 10) {
            marginTop += (start - 3) * classHeight + centerTipHeight;
        } else {
            marginTop += (start - 3) * classHeight + centerTipHeight * 2;

        }
        layoutParams.setMargins(classWith * (week - 1), marginTop, 0, 0);

        textView.setLayoutParams(layoutParams);

        classContent.addView(textView);
    }
}
