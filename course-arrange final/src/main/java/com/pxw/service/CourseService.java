package com.pxw.service;

import com.pxw.pojo.Course;


public interface CourseService {

    public String selectByCondition(Course course,Integer currentPage,Integer pageSize);

    public String update(Course course,boolean flag);

    public void deleteByIds(int[] ids);

    public String add(Course course);

}
