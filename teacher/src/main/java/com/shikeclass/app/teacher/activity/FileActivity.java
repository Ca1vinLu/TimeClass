package com.shikeclass.app.teacher.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.shikeclass.app.teacher.R;
import com.shikeclass.app.teacher.adapter.FolderAdapter;
import com.shikeclass.app.teacher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FileActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private FolderAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file;
    }

    @Override
    public void initView() {
        adapter = new FolderAdapter();
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));

    }

    @Override
    public void initData() {

        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add(i);
        }

        adapter.setNewData(data);
    }

    @Override
    public void initListener() {

    }
}
