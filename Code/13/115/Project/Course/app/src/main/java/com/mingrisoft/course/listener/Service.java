package com.mingrisoft.course.listener;

/**
 * Author LYJ
 * Created on 2016/12/28.
 * Time 15:53
 */

public interface Service {
    //用于展示列表数据
    String first_url = "http://www.mingrisoft.com/Index/API/getAllCourse/rows/7";
    //获取页面详请
    String second_url = "http://www.mingrisoft.com/Index/API/getSystemCourseInfo/course_id/"+"%s"+"/user_id/"+"%s";
}
