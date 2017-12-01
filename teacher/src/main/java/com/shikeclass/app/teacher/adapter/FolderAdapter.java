package com.shikeclass.app.teacher.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.teacher.R;
import com.shikeclass.app.teacher.activity.FileDetailActivity;

/**
 * Created by LYZ on 2017/11/30 0030.
 */

public class FolderAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public FolderAdapter() {
        super(R.layout.item_folder);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, FileDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }
}
