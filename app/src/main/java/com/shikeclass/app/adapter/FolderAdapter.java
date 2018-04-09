package com.shikeclass.app.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shikeclass.app.R;
import com.shikeclass.app.activity.FileActivity;
import com.shikeclass.app.bean.FolderBean;

import java.util.ArrayList;

/**
 * Created by LYZ on 2017/11/30 0030.
 */

public class FolderAdapter extends BaseQuickAdapter<FolderBean, BaseViewHolder> {
    public FolderAdapter() {
        super(R.layout.item_folder);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FolderBean bean = mData.get(position);
                Intent intent = new Intent(mContext, FileActivity.class);
                intent.putExtra("lessonName", bean.folderName);
                intent.putExtra("files", (ArrayList<? extends Parcelable>) bean.fileBeanList);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, FolderBean item) {
        helper.setText(R.id.folder_name, item.folderName);
        helper.setText(R.id.file_num, "共" + String.valueOf(item.getFileNum()) + "个文件");

    }
}
