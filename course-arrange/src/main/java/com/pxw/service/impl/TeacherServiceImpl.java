package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.RoomMapper;
import com.pxw.mapper.TeacherMapper;
import com.pxw.pojo.Room;
import com.pxw.pojo.Teacher;
import com.pxw.service.RoomService;
import com.pxw.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Teacher login(Teacher teacher) {
        return teacherMapper.login(teacher);
    }

    @Override
    public String selectByCondition(Teacher teacher, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Teacher> teachers = teacherMapper.selectByCondition(teacher);
        // Pagination info
        PageInfo<Teacher> teacherPageInfo = new PageInfo<>(teachers);

        return JSON.toJSONString(teacherPageInfo);
    }

    @Override
    public String update(Teacher teacher, String oldId) {
        // Name modified, judge whether modified ID already exists
        if (! "null".equals(oldId)) {
            if (teacherMapper.selectById(teacher.getId()) !=null ) {
                return "fail";
            }else {
                // Mapper interface cannot be overloaded
                teacherMapper.update1(teacher,oldId);
                return "success";
            }
        }
        teacherMapper.update2(teacher);
        return "success";
    }

    @Override
    public void deleteByIds(int[] ids) {
        teacherMapper.deleteByIds(ids);
    }

    @Override
    public String add(Teacher teacher) {
        // Judge whether ID conflict
        if( teacherMapper.selectById(teacher.getId()) !=null){
            return "fail";
        }
        teacherMapper.add(teacher);
        return "success";
    }


}
