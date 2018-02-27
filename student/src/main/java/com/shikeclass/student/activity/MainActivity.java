package com.shikeclass.student.activity;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.shikeclass.student.R;
import com.shikeclass.student.adapter.ClassAdapter;
import com.shikeclass.student.base.BaseActivity;
import com.shikeclass.student.bean.ClassBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

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

    private ClassAdapter adapter;
    private int currentWeek = 1;
    private int currentWeekDay = 1;
    private final String startDay = "2018-03-05";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new ClassAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.item_no_class);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setRefreshing(true);
        List<ClassBean> data = new ArrayList<>();
        ClassBean classBean = new ClassBean("面向对象技术引论", "C2-202", 1, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("编译原理", "D1-201", 1, 7, 9);
        data.add(classBean);
        classBean = new ClassBean("编译原理实验", "待定", 1, 10, 17, 11, 13);
        data.add(classBean);
        classBean = new ClassBean("大学生就业指导与创业教育", "F2-203", 3, 2, 11, 3, 4);
        data.add(classBean);
        classBean = new ClassBean("计算机网络", "D1-204", 3, 7, 9);
        data.add(classBean);
        classBean = new ClassBean("计算机网络实验", "待定", 3, 6, 17, 11, 13);
        data.add(classBean);
        classBean = new ClassBean("软件工程创新实践", "待定", 4, 11, 15, 7, 10);
        data.add(classBean);
        classBean = new ClassBean("面向对象技术引论实践", "D1-401", 5, 5, 18, 7, 9, 1);
        data.add(classBean);

        Date startDay = TimeUtils.string2Date(this.startDay, new SimpleDateFormat("yyyy-MM-dd"));
        long spanDays = TimeUtils.getTimeSpan(startDay, new Date(System.currentTimeMillis()), TimeConstants.DAY);
        currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        currentWeek = (int) (spanDays / 7 + 1);

        List<ClassBean> todayClass = new ArrayList<>();
        for (ClassBean datum : data) {
            if (shouldHaveClass(datum))
                todayClass.add(datum);
        }
        adapter.setNewData(todayClass);

        swipeRefreshLayout.setRefreshing(false);
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
                initData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_class) {
            startActivity(ClassTableActivity.class);
        } else if (id == R.id.my_file) {
            startActivity(FileActivity.class);
        } else if (id == R.id.my_sign_in) {

        } else if (id == R.id.my_rank) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean shouldHaveClass(ClassBean bean) {
        if (bean.startWeek <= currentWeek && currentWeek <= bean.endWeek) {
            if (bean.isSingleOrDouble == 0 && currentWeekDay == convertWeekDay(bean.weekDay))
                return true;
            if (bean.isSingleOrDouble == 1 && currentWeek % 2 == 0)
                return false;
            else if (bean.isSingleOrDouble == 2 && currentWeek % 2 == 1)
                return false;

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
