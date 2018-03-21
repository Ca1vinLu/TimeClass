package com.shikeclass.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shikeclass.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LYZ on 2018/2/27 0027.
 */

public class WeekTextView extends RelativeLayout {


    @BindView(R.id.week_name)
    TextView weekName;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.bottom_line)
    View bottomLine;


    private Context context;

    public WeekTextView(Context context) {
        this(context, null);
    }

    public WeekTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.textview_week, this);
        ButterKnife.bind(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeekTextView);
        try {
            if (!TextUtils.isEmpty(typedArray.getString(R.styleable.WeekTextView_weekName)))
                weekName.setText(typedArray.getString(R.styleable.WeekTextView_weekName));
        } finally {
            typedArray.recycle();

        }
    }


    public void isToday() {
        weekName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        date.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        weekName.setTypeface(weekName.getTypeface(), Typeface.BOLD);
        date.setTypeface(date.getTypeface(), Typeface.BOLD);
        bottomLine.setVisibility(VISIBLE);
    }


    public void setData(String weekName, String date) {
        this.weekName.setText(weekName);
        this.date.setText(date);
    }

    public void setData(String date) {
        this.date.setText(date);
    }
}
