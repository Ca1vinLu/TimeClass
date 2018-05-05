package com.shikeclass.app.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;
import com.shikeclass.app.bean.FileBean;

import java.text.DecimalFormat;

/**
 * Created by LYZ on 2017/11/30 0030.
 */

public class FileAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {

    private DecimalFormat format = new DecimalFormat("0.00");

    public FileAdapter() {
        super(R.layout.item_file);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        helper.setText(R.id.file_name, item.file_name);
        if (item.file_size < 1024)
            helper.setText(R.id.file_size, item.file_size + "KB");
        else
            helper.setText(R.id.file_size, format.format(item.file_size / 1024.0) + "MB");

    }
}
