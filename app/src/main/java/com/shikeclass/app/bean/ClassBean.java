package com.shikeclass.app.bean;

import java.io.Serializable;

/**
 * Created by LYZ on 2017/11/29 0029.
 */

public class ClassBean implements Serializable {
    public ClassBean(String name, String address, int weekDay, int startClass, int endClass) {
        this.name = name;
        this.address = address;
        this.weekDay = weekDay;
        this.startClass = startClass;
        this.endClass = endClass;
    }

    public ClassBean(String name, String address, int weekDay, int startWeek, int endWeek, int startClass, int endClass) {
        this.name = name;
        this.address = address;
        this.weekDay = weekDay;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.startClass = startClass;
        this.endClass = endClass;
    }

    public ClassBean(String name, String address, int weekDay, int startWeek, int endWeek, int startClass, int endClass, int isSingleOrDouble) {
        this.name = name;
        this.address = address;
        this.weekDay = weekDay;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.startClass = startClass;
        this.endClass = endClass;
        this.isSingleOrDouble = isSingleOrDouble;
    }

    public ClassBean() {
    }

    @Override
    public String toString() {
        return "ClassBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", weekDay=" + weekDay +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", startClass=" + startClass +
                ", endClass=" + endClass +
                ", isSingleOrDouble=" + isSingleOrDouble +
                '}';
    }


    public String name;
    public String address = "待定";
    public int weekDay;
    public int startWeek = 1;
    public int endWeek = 18;
    public int startClass;
    public int endClass;
    public int colorId;
    public int isSingleOrDouble = 0;//1-Single  2-Double
}
