package com.shikeclass.app.bean;

/**
 * Created by LYZ on 2018/3/27 0027.
 */

public class ServerClassBean {

    public String lesson_id;
    public String lesson_name;
    public String class_id;
    public String teacher_name;
    public String code;
    public long start_time;
    public long end_time;
    public String student_id;
    public String student_name;
    public Object code_time;
    public String exist;

    //0-还未到上课时间  1-即将上课  2-正在上课  3-已下课
    public int status = 0;


    public String getLesson_id() {
        return lesson_id;
    }

    public String getLesson_name() {
        return lesson_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getCode() {
        return code;
    }

    public long getStart_time() {
        return start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public Object getCode_time() {
        return code_time;
    }

    public String getExist() {
        return exist == null ? "0" : exist;
    }
}
