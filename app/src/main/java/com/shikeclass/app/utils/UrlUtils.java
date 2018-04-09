package com.shikeclass.app.utils;

/**
 * Created by LYZ on 2017/10/4 0004.
 */

public class UrlUtils {

    public static final String ROOT_URL = "http://112.74.41.60";

    public static final String HQU_ROOT_URL = "http://10.4.12.22/CourseQuery";

    public static final String specialityClass = HQU_ROOT_URL + "/Common/SpecialityClass.aspx";

    public static final String classArrange = HQU_ROOT_URL + "/Arrange/ClassArrange.aspx";

    public static final String studentLogin = ROOT_URL + "/shikeya/api/student_login";

    public static final String teacherLogin = ROOT_URL + "/shikeya/api/teacher_login";

    public static final String getStuTodayLesson = ROOT_URL + "/shikeya/api/lesson_search";

    public static final String getStuSignWeek = ROOT_URL + "/shikeya/api/student_sign_week";

    public static final String getStuAllLesson = ROOT_URL + "/shikeya/api/lesson_search_all";

    public static final String getLessonFile = ROOT_URL + "/shikeya/api/file_search_condition";

    public static final String getStuFile = ROOT_URL + "/shikeya/api/file_search_sid";

    public static final String studentSign = ROOT_URL + "/shikeya/api/student_sign";

    public static final String teacherSign = ROOT_URL + "/shikeya/api/teacher_sign";

    public static final String teacherPassword = ROOT_URL + "/shikeya/api/teacher_password";

    public static final String getStuSignCondition = ROOT_URL + "/shikeya/api/find_student";

    public static final String getTeacherLesson = ROOT_URL + "/shikeya/api/teacher_lesson";

    public static final String changeStatus = ROOT_URL + "/shikeya/api/changeStatus";
}
