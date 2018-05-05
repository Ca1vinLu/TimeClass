package com.shikeclass.app.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.shikeclass.app.R;
import com.shikeclass.app.adapter.SignAdapter;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.SignBean;
import com.shikeclass.app.network.JsonCallback;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SignInListActivity extends BaseActivity {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.spinner_week)
    NiceSpinner spinnerWeek;
    @BindView(R.id.btn_export_list)
    TextView btnExportList;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private SignAdapter adapter;
    private int week;
    private int userType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_in_list;
    }

    @Override
    public void initView() {
        userType = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0);
        adapter = new SignAdapter(userType);
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        adapter.setEmptyView(R.layout.item_no_sign);
        swipeRefreshLayout.setColorSchemeResources(R.color.class_color1,
                R.color.class_color2,
                R.color.class_color3,
                R.color.class_color4);

        if (userType == 0)
            btnExportList.setVisibility(View.GONE);
        else
            btnExportList.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {

        week = getIntent().getIntExtra("week", 1);

        int weekNum = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_TERM_WEEKS, 20);
        List<String> weeks = new ArrayList<>();
        for (int i = 0; i < weekNum; i++) {
            weeks.add(String.valueOf(i + 1));
        }
        spinnerWeek.attachDataSource(weeks);
        spinnerWeek.setSelectedIndex(week - 1);

        getSignData();

    }

    @Override
    public void initListener() {
        spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                week = position + 1;
                getSignData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSignData();
            }
        });
    }

    private void getSignData() {
        swipeRefreshLayout.setRefreshing(true);
        if (userType == 0)
            getStudentSignData();
        else getTeacherSignData();
    }

    private void getStudentSignData() {
        OkGo.<List<SignBean>>post(UrlUtils.getStuSignWeek)
                .tag(this)
                .params("student_id", SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, ""))
                .params("week", week)
                .execute(new JsonCallback<List<SignBean>>(new TypeToken<List<SignBean>>() {
                }.getType()) {
                    @Override
                    public void onSuccess(Response<List<SignBean>> response) {
                        adapter.setNewData(response.body());
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Response<List<SignBean>> response) {
                        super.onError(response);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void getTeacherSignData() {
        OkGo.<List<SignBean>>post(UrlUtils.getStuSignCondition)
                .tag(this)
                .params("lesson_id", getIntent().getStringExtra("lesson_id"))
                .execute(new JsonCallback<List<SignBean>>(new TypeToken<List<SignBean>>() {
                }.getType()) {
                    @Override
                    public void onSuccess(Response<List<SignBean>> response) {
                        adapter.setNewData(response.body());
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Response<List<SignBean>> response) {
                        super.onError(response);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
