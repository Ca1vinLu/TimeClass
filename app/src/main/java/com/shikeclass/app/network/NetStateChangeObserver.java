package com.shikeclass.app.network;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * Created by LYZ on 2017/8/9 0009.
 */

public interface NetStateChangeObserver {
    void onNetDisconnected();

    void onNetConnected(NetworkUtils.NetworkType networkType);
}
