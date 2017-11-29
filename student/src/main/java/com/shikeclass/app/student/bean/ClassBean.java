package com.shikeclass.app.student.bean;

/**
 * Created by LYZ on 2017/11/29 0029.
 */

public class ClassBean {
    public ClassBean(String name, String address, int week, int start, int end) {
        this.name = name;
        this.address = address;
        this.week = week;
        this.start = start;
        this.end = end;
    }

    public ClassBean() {
    }

    public String name;
    public String address;
    public int week;
    public int start;
    public int end;
    public int colorId;
}
