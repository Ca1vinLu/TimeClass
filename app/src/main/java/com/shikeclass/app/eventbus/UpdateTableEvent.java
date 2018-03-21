package com.shikeclass.app.eventbus;

/**
 * Created by LYZ on 2018/2/28 0028.
 */

public class UpdateTableEvent {
    private int msg = 0;

    public UpdateTableEvent(int msg) {
        this.msg = msg;
    }

    public UpdateTableEvent() {
    }


    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
