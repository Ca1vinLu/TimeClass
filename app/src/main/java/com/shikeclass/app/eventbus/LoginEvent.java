package com.shikeclass.app.eventbus;

import com.shikeclass.app.bean.ServerClassBean;

import java.util.List;

/**
 * Created by LYZ on 2018/3/18 0018.
 */

public class LoginEvent {
    private int type;
    private List<ServerClassBean> data;

    public LoginEvent(int type) {
        this.type = type;
    }

    public LoginEvent(int type, List<ServerClassBean> data) {
        this.type = type;
        this.data = data;
    }

    public List<ServerClassBean> getData() {
        return data;
    }

    public int getType() {
        return type;
    }
}
