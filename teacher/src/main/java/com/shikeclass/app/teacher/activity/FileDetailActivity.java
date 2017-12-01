package com.shikeclass.app.teacher.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.shikeclass.app.teacher.R;
import com.shikeclass.app.teacher.adapter.FileAdapter;
import com.shikeclass.app.teacher.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FileDetailActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private FileAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file_detail;
    }

    @Override
    public void initView() {
        adapter = new FileAdapter();
        adapter.bindToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initData() {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            data.add(i);
        }

        adapter.setNewData(data);
    }

    @Override
    public void initListener() {

    }
}