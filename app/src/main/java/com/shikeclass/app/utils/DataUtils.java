package com.shikeclass.app.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shikeclass.app.bean.ClassBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LYZ on 2018/2/27 0027.
 */

public class DataUtils {

    private static final String TAG = "DataUtils";

    public static List<ClassBean> generateClassTableData() {
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

        Gson gson = new Gson();
        String json = gson.toJson(data);
        Log.d(TAG, "generateClassTableData: " + json);

        return data;
    }


    public static List<ClassBean> getClassTableData(Context context) {
        String localData = SharedPreUtil.getStringValue(context, CommonValue.SHA_CLASS_TABLE, "[]");
        if (localData.equals("[]"))
            return null;
        else {
            Gson gson = new Gson();
            return gson.fromJson(localData, new TypeToken<List<ClassBean>>() {
            }.getType());
        }
    }
}
