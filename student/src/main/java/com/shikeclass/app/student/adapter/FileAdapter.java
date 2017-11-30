package com.shikeclass.app.student.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;

/**
 * Created by LYZ on 2017/11/30 0030.
 */

public class FileAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public FileAdapter() {
        super(R.layout.item_file);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }
}
