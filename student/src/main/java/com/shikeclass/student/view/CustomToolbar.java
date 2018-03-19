package com.shikeclass.student.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.shikeclass.student.R;

/**
 * Created by LYZ on 2018/3/6 0006.
 */

public class CustomToolbar extends Toolbar {
    public CustomToolbar(final Context context) {
        this(context, null);
    }

    public CustomToolbar(final Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public CustomToolbar(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            DrawableCompat.setTint(navigationIcon, Color.WHITE);
//            setNavigationOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (context instanceof AppCompatActivity)
//                        ((AppCompatActivity) context).finish();
//                }
//            });
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
        String title = typedArray.getString(R.styleable.CustomToolbar_titleStr);
        if (title != null) {
            AppCompatTextView textView = new AppCompatTextView(context);
            textView.setText(title);
            textView.setTextColor(Color.WHITE);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextSize(20);
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(layoutParams);
            addView(textView);
        }

        typedArray.recycle();

//        ViewCompat.setElevation(this, 0);

    }
}
