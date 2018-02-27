package com.shikeclass.student.bean;

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

    public String name;
    public String address;
    public int weekDay;
    public int startWeek = 1;
    public int endWeek = 18;
    public int startClass;
    public int endClass;
    public int colorId;
    public int isSingleOrDouble = 0;//1-Single  2-Double
}
