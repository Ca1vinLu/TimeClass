package com.shikeclass.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.shikeclass.app.R;
import com.shikeclass.app.adapter.ServerClassAdapter;
import com.shikeclass.app.base.BaseActivity;
import com.shikeclass.app.bean.ClassBean;
import com.shikeclass.app.bean.ServerClassBean;
import com.shikeclass.app.eventbus.LoginEvent;
import com.shikeclass.app.eventbus.SignOutEvent;
import com.shikeclass.app.network.JsonCallback;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.DialogUtils;
import com.shikeclass.app.utils.SharedPreUtil;
import com.shikeclass.app.utils.UrlUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    CircleImageView headImage;
    TextView studentName;
    TextView studentId;
    TextView login;

    private ServerClassAdapter adapter;
    private int currentWeek = 1;
    private int currentWeekDay = 1;
    private String startDay;
    private boolean isDuringTerm = true;
    private long clickBackTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        headImage = headerView.findViewById(R.id.head_image);
        studentName = headerView.findViewById(R.id.student_name);
        studentId = headerView.findViewById(R.id.student_id);
        login = headerView.findViewById(R.id.login);

        adapter = new ServerClassAdapter(SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0));
        adapter.bindToRecyclerView(recyclerView);

        swipeRefreshLayout.setColorSchemeResources(R.color.class_color1, R.color.class_color2, R.color.class_color3, R.color.class_color4);

        if (SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0) == 0)
            navigationView.inflateMenu(R.menu.activity_main_drawer_stu);
        else
            navigationView.inflateMenu(R.menu.activity_main_drawer_teacher);
    }

    @Override
    public void initData() {
        initTime();
        getUserInfo();
        if (SharedPreUtil.getBooleanValue(mActivity, CommonValue.SHA_IS_LOGIN, false))
            getServerData();
    }

    @Override
    public void initListener() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServerData();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUpdateTable(UpdateTableEvent event) {
//        getClassData();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(LoginEvent event) {

        navigationView.getMenu().clear();
        if (SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0) == 0)
            navigationView.inflateMenu(R.menu.activity_main_drawer_stu);
        else
            navigationView.inflateMenu(R.menu.activity_main_drawer_teacher);

        getUserInfo();
        adapter.setUserType(event.getType());
        getServerData(event.getData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(SignOutEvent event) {

        login.setVisibility(View.VISIBLE);
        studentName.setVisibility(View.GONE);
        studentId.setVisibility(View.GONE);

        adapter.setNewData(null);
    }

    private void getUserInfo() {

        if (SharedPreUtil.getBooleanValue(mActivity, CommonValue.SHA_IS_LOGIN, false)) {
            if (SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0) == 0) {
                String stuName, stuId;
                stuName = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_NAME, "");
                stuId = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, "");
                studentId.setText(stuId);
                studentName.setText(stuName);
                login.setVisibility(View.GONE);
                studentName.setVisibility(View.VISIBLE);
                studentId.setVisibility(View.VISIBLE);
            } else {
                String stuName;
                stuName = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_NAME, "");
                studentName.setText(stuName);
                login.setVisibility(View.GONE);
                studentName.setVisibility(View.VISIBLE);
                studentId.setVisibility(View.INVISIBLE);
            }
        } else {
            login.setVisibility(View.VISIBLE);
            studentName.setVisibility(View.GONE);
            studentId.setVisibility(View.GONE);
        }


    }

    private void getServerData() {
        swipeRefreshLayout.setRefreshing(true);
        PostRequest<List<ServerClassBean>> request;
        if (SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_LOGIN_TYPE, 0) == 0)
            request = OkGo.<List<ServerClassBean>>post(UrlUtils.getStuTodayLesson)
                    .tag(this)
                    .params("student_id", SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_STU_ID, ""));
        else request = OkGo.<List<ServerClassBean>>post(UrlUtils.getTeacherLesson)
                .tag(this)
                .params("teacher_name", SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_TEACHER_NAME, ""));

        request.execute(new JsonCallback<List<ServerClassBean>>(new TypeToken<List<ServerClassBean>>() {
        }.getType()) {
            @Override
            public void onSuccess(Response<List<ServerClassBean>> response) {
                getServerData(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Response<List<ServerClassBean>> response) {
                super.onError(response);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


    private void getServerData(List<ServerClassBean> serverData) {
        adapter.setNewData(serverData);
    }


//    private void getClassData() {
//        swipeRefreshLayout.setRefreshing(true);
//        List<ClassBean> data = DataUtils.getClassTableData(mActivity);
//
//        if (data == null) {
//            DialogUtils.showDialog(mActivity, "提示", "请先导入课程，导入前请确保手机已连接至HQU", "导入", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    startActivity(ImportClassActivity.class);
//                }
//            });
//            swipeRefreshLayout.setRefreshing(false);
//            adapter.setEmptyView(R.layout.item_no_import_class);
//            return;
//        }
//
//
//        List<ClassBean> todayClass = new ArrayList<>();
//        for (ClassBean datum : data) {
//            if (shouldHaveClass(datum))
//                todayClass.add(datum);
//        }
//        adapter.setNewData(todayClass);
//
//        swipeRefreshLayout.setRefreshing(false);
//    }

    private void initTime() {
        startDay = SharedPreUtil.getStringValue(mActivity, CommonValue.SHA_TERM_START_TIME, "2018-03-05");
        Date startDay = TimeUtils.string2Date(this.startDay, new SimpleDateFormat("yyyy-MM-dd"));
        int termWeeks = SharedPreUtil.getIntValue(mActivity, CommonValue.SHA_TERM_WEEKS, 20);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        calendar.add(Calendar.WEEK_OF_YEAR, termWeeks);
        calendar.add(Calendar.DATE, -1);
        Date endDay = calendar.getTime();
        Date today = new Date(System.currentTimeMillis());
        if (today.before(startDay) || today.after(endDay)) {
            adapter.setEmptyView(R.layout.item_on_holiday);
            isDuringTerm = false;
            DialogUtils.showDialog(mActivity, "提示", "放假中，请及时校准下学期开学时间", "校准", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(AdjustTermActivity.class);
                }
            });
        } else {
            isDuringTerm = true;
            adapter.setEmptyView(R.layout.item_no_class);
            long spanDays = TimeUtils.getTimeSpan(startDay, new Date(System.currentTimeMillis()), TimeConstants.DAY);
            currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            currentWeek = (int) (spanDays / 7 + 1);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - clickBackTime > 1500) {
                Snackbar.make(recyclerView, "再按一次退出应用程序", Snackbar.LENGTH_SHORT).show();
                clickBackTime = System.currentTimeMillis();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(SettingActivity.class);
            return true;
        } else if (id == R.id.action_help) {
            DialogUtils.showDialog(mActivity, getString(R.string.main_help));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.my_class) {
            startActivity(ClassTableActivity.class);
        } else if (id == R.id.my_file) {
            startActivity(FolderActivity.class);
        } else if (id == R.id.my_sign_in) {
            intent = new Intent(mActivity, SignInListActivity.class);
            intent.putExtra("week", currentWeek);
            startActivity(intent);
        } else if (id == R.id.import_class) {
            startActivity(ImportClassActivity.class);
        } else if (id == R.id.setting)
            startActivity(SettingActivity.class);
        else if (id == R.id.sign_in_list) {
            intent = new Intent(mActivity, SignInListActivity.class);
            intent.putExtra("week", currentWeek);
            intent.putExtra("teacher", true);
            startActivity(intent);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean shouldHaveClass(ClassBean bean) {
        if (isDuringTerm && bean.startWeek <= currentWeek && currentWeek <= bean.endWeek && currentWeekDay == convertWeekDay(bean.weekDay)) {
            if (bean.isSingleOrDouble == 0)
                return true;
            else if (bean.isSingleOrDouble == 1 && currentWeek % 2 == 1)
                return true;
            else if (bean.isSingleOrDouble == 2 && currentWeek % 2 == 0)
                return true;
        }
        return false;
    }

    private int convertWeekDay(int weekDay) {
        return (weekDay + 1) % 7;
    }


    private void initShortcuts() {
        ShortcutManager shortcutManager = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutManager = getSystemService(ShortcutManager.class);


            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Web site")
                    .setLongLabel("Open the web site")
                    .setIcon(Icon.createWithResource(mActivity, R.drawable.icon_class))
                    .setIntent(new Intent(mActivity, ClassTableActivity.class))
                    .build();

            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
        }
    }
}
