package com.shikeclass.app.teacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.shikeclass.app.teacher.network.NetStateChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class NetWorkReceiver extends BroadcastReceiver {


    private static class InstanceHolder {
        private static final NetWorkReceiver INSTANCE = new NetWorkReceiver();
    }

    private List<NetStateChangeObserver> mObservers = new ArrayList<>();

    public NetWorkReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//
//            //获得ConnectivityManager对象
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            //获取ConnectivityManager对象对应的NetworkInfo对象
//            //获取WIFI连接的信息
//            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            //获取移动数据连接的信息
//            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if (!wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//                DialogUtils.showLoginDialog(context);
//            }
//            //API大于23时使用下面的方式进行网络监听
//        } else {
//            //获得ConnectivityManager对象
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//           connMgr.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
//               @Override public void onAvailable(Network network) {
//                   super.onAvailable(network);
//               }
//
//               @Override
//               public void onLost(Network network) {
//                   super.onLost(network);
//               }
//           });
//        }

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType();
            notifyObservers(networkType);
        }
    }


    /**
     * 注册网络监听
     */
    public static void registerReceiver(@NonNull Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
    }

    /**
     * 取消网络监听
     */
    public static void unregisterReceiver(@NonNull Context context) {
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }

    /**
     * 注册网络变化Observer
     */
    public static void registerObserver(NetStateChangeObserver observer) {
        if (observer == null)
            return;
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    /**
     * 取消网络变化Observer的注册
     */
    public static void unregisterObserver(NetStateChangeObserver observer) {
        if (observer == null)
            return;
        if (InstanceHolder.INSTANCE.mObservers == null)
            return;
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    /**
     * 通知所有的Observer网络状态变化
     */
    private void notifyObservers(NetworkUtils.NetworkType networkType) {
        if (networkType == NetworkUtils.NetworkType.NETWORK_NO) {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetDisconnected();
            }
        } else {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetConnected(networkType);
            }
        }
    }


}
