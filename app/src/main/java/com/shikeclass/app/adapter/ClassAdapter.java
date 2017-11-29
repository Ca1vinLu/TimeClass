package com.shikeclass.app.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;
import com.shikeclass.app.activity.MyClassActivity;

/**
 * Created by LYZ on 2017/11/26 0026.
 */

public class ClassAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public ClassAdapter() {
        super(R.layout.item_class);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MyClassActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }
}
