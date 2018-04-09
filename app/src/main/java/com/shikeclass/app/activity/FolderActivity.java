package com.shikeclass.app.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.R;
import com.shikeclass.app.adapter.FolderAdapter;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.FileBean;
import com.shikeclass.app.bean.FolderBean;
import com.shikeclass.app.network.JsonCallback;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class FolderActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private FolderAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_folder;
    }

    @Override
    public void initView() {
        adapter = new FolderAdapter();
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        adapter.setEmptyView(R.layout.item_no_folder);

    }

    @Override
    public void initData() {
        OkGo.<List<FileBean>>post(UrlUtils.getStuFile)
                .tag(this)
                .params("sid", SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, ""))
                .execute(new JsonCallback<List<FileBean>>(new TypeToken<List<FileBean>>() {
                }.getType()) {
                    @Override
                    public void onSuccess(Response<List<FileBean>> response) {
                        HashSet<String> folderNames = new HashSet<>();
                        List<FolderBean> folders = new ArrayList<>();
                        for (FileBean fileBean : response.body()) {
                            if (folderNames.contains(fileBean.lesson_name)) {
                                for (FolderBean folder : folders) {
                                    if (folder.folderName.equals(fileBean.lesson_name))
                                        folder.fileBeanList.add(fileBean);
                                }
                            } else {
                                FolderBean folderBean = new FolderBean();
                                folderBean.folderName = fileBean.lesson_name;
                                folderBean.fileBeanList = new ArrayList<>();
                                folderBean.fileBeanList.add(fileBean);
                                folders.add(folderBean);
                                folderNames.add(fileBean.lesson_name);
                            }
                        }

                        adapter.setNewData(folders);

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
