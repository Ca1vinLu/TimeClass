package com.shikeclass.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.shikeclass.app.utils.ChangeStatusUtils;
import com.shikeclass.app.utils.CommonValue;
import com.shikeclass.app.utils.SharedPreUtil;

import okhttp3.OkHttpClient;

/**
 * Created by LYZ on 2017/10/23 0023.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication application;

    public static final int DELAY_TIME = 180000;

    public static Application getInstance() {
        return application;
    }

    private int mActivityCount = 0;
    private Handler mHandler = new Handler();

    private Runnable mBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            ChangeStatusUtils.changeStatus(application, SharedPreUtil.getStringValue(application, CommonValue.SHA_LESSON_ID, ""), 2);
        }
    };

    private Runnable mForegroundRunnable = new Runnable() {
        @Override
        public void run() {
            ChangeStatusUtils.changeStatus(application, SharedPreUtil.getStringValue(application, CommonValue.SHA_LESSON_ID, ""), 1);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        registerActivityLifecycleCallbacks();
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
//        Realm.init(this);
        initOkGo();
    }

    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().init(this);
        OkGo.getInstance().setOkHttpClient(builder.build());
    }


    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivityCount++;
                if (mActivityCount == 1) {
//                    Toast.makeText(application, "进入前台", Toast.LENGTH_SHORT).show();
                    if (SharedPreUtil.getBooleanValue(activity, CommonValue.SHA_IS_LOGIN, false)) {
                        mHandler.post(mForegroundRunnable);
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivityCount--;

                PowerManager manager = (PowerManager) application.getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = manager != null && manager.isScreenOn();
                if (mActivityCount == 0 &&
                        SharedPreUtil.getBooleanValue(activity, CommonValue.SHA_IS_LOGIN, false) &&
                        !isScreenOn &&
                        SharedPreUtil.getBooleanValue(application, CommonValue.SHA_HAVING_CLASS, false)) {
                    Toast.makeText(application, "时课已进入后台，请在三分钟内返回，否则将进入暂离状态", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mBackgroundRunnable, DELAY_TIME);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
