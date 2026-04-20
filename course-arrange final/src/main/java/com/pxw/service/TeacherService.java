package com.pxw.service;

import com.pxw.pojo.Room;
import com.pxw.pojo.Teacher;



public interface TeacherService {


    public Teacher login(Teacher teacher);

    public String selectByCondition(Teacher teacher, Integer currentPage, Integer pageSize);

    public String update(Teacher teacher,String oldId);

    public void deleteByIds(int[] ids);

    public String add(Teacher teacher);

}
