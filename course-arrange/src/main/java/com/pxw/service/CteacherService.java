package com.pxw.service;

import com.pxw.pojo.Course;
import com.pxw.pojo.Cteacher;
import com.pxw.pojo.Room;

/**
 * Created by pxw on 2022/4/15 20:30
 *
 * @author pxw
 */



public interface CteacherService {

    public String selectByCondition(Cteacher cteacher, Integer currentPage, Integer pageSize);

    public String update(Cteacher cteacher);

    public void deleteByIds(int[] ids);

    public String add(Cteacher[] cteachers);

    public String selectTeachreByCourse(Course course);

}
