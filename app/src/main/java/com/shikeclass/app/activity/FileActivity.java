package com.shikeclass.app.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.R;
import com.shikeclass.app.adapter.FileAdapter;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.FileBean;
import com.shikeclass.app.network.JsonCallback;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;
import com.shikeclass.app.view.MyToolbar;

import java.util.List;

import butterknife.BindView;

public class FileActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    MyToolbar toolbar;

    private FileAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_file;
    }

    @Override
    public void initView() {
        adapter = new FileAdapter();
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        adapter.setEmptyView(R.layout.item_no_file);
    }

    @Override
    public void initData() {
        toolbar.setTitle(getIntent().getStringExtra("lessonName"));
        String lessonId = getIntent().getStringExtra("lessonId");
        List<FileBean> data = getIntent().getParcelableArrayListExtra("files");
        if (data != null)
            adapter.setNewData(data);
        else
            OkGo.<List<FileBean>>post(UrlUtils.getLessonFile)
                    .tag(this)
                    .params("lessonid", lessonId)
                    .params("studentid", SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, ""))
                    .execute(new JsonCallback<List<FileBean>>(new TypeToken<List<FileBean>>() {
                    }.getType()) {
                        @Override
                        public void onSuccess(Response<List<FileBean>> response) {
                            adapter.setNewData(response.body());
                        }

                        @Override
                        public void onError(Response<List<FileBean>> response) {
                            super.onError(response);
                        }
                    });

    }

    @Override
    public void initListener() {

    }
}