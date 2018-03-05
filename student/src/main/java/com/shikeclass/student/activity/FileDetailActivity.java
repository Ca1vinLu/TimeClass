package com.shikeclass.student.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.shikeclass.student.R;
import com.shikeclass.student.adapter.FileAdapter;
import com.shikeclass.student.base.BaseActivity;

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