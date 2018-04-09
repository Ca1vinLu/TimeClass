package com.shikeclass.app.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;
import com.shikeclass.app.activity.ClassTableActivity;
import com.shikeclass.app.activity.FolderActivity;
import com.shikeclass.app.bean.ClassBean;

/**
 * Created by LYZ on 2017/11/26 0026.
 */

public class ClassAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {
    private String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private String[] startTime = {"8:00", "9:00", "10:10", "11:10", "", "", "14:30", "15:30", "16:30", "17:30", "19:10", "20:10", "21:10"};
    private String[] endTime = {"8:50", "9:50", "11:00", "12:00", "", "", "15:20", "16:20", "17:20", "18:20", "20:00", "21:00", "22:00"};

    public ClassAdapter() {
        super(R.layout.item_class);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ClassTableActivity.class);
                mContext.startActivity(intent);
            }
        });
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_file:
                        intent = new Intent(mContext, FolderActivity.class);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassBean item) {
        helper.setText(R.id.class_name, item.name);
        String time = weeks[item.weekDay] + " " + startTime[item.startClass - 1] + "-" + endTime[item.endClass - 1];
        helper.setText(R.id.class_time, time);
        helper.setText(R.id.class_place, item.address);
        helper.setText(R.id.class_file, item.name + " 课件");
        helper.addOnClickListener(R.id.btn_file);
    }
}
