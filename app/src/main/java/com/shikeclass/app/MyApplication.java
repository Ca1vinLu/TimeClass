package com.shikeclass.app;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;

import okhttp3.OkHttpClient;

/**
 * Created by LYZ on 2017/10/23 0023.
 */

public class MyApplication extends Application {
    private static MyApplication application;

    public static Application getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);

        initOkGo();
    }

    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        OkGo.getInstance().init(this);
        OkGo.getInstance().setOkHttpClient(builder.build());
    }
}
