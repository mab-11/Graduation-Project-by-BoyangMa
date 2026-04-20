package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CteacherMapper;
import com.pxw.pojo.Course;
import com.pxw.pojo.Cteacher;
import com.pxw.pojo.Teacher;
import com.pxw.service.CteacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CteacherServiceImpl implements CteacherService {

    @Autowired
    private CteacherMapper cteacherMapper;

    @Override
    public String selectByCondition(Cteacher cteacher, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Cteacher> cteachers = cteacherMapper.selectByCondition(cteacher);
        // Pagination info
        PageInfo<Cteacher> cteacherPageInfo = new PageInfo<>(cteachers);

        return JSON.toJSONString(cteacherPageInfo);
    }


    @Override
    public String update(Cteacher newCteacher) {

        // Judge whether modified
        Cteacher oldCteacher = cteacherMapper.selectById(newCteacher.getId());
        System.out.println(oldCteacher);
        System.out.println(newCteacher);
        if (! oldCteacher.equals(newCteacher)){
            // Already modified, judge whether duplicate
            if (cteacherMapper.selectByCondition(newCteacher).size()!=0){
                return "fail";
            }
        }
        cteacherMapper.update(newCteacher);
        return "success";
    }

    @Override
    public void deleteByIds(int[] ids) {
        cteacherMapper.deleteByIds(ids);
    }

    @Override
    public String add(Cteacher[] cteachers) {
        for(Cteacher cteacher:cteachers){
            // Judge whether course already exists
            if (cteacherMapper.selectByCondition(cteacher).size() != 0 ){
                return "fail";
            }
        }
        // If does not exist, then add
        for(Cteacher cteacher:cteachers){
            cteacherMapper.add(cteacher);
        }
        return "success";
    }

    @Override
    public String selectTeachreByCourse(Course course) {
        List<Teacher> teachers = cteacherMapper.selectTeachreByCourse(course);
        String jsonString = JSON.toJSONString(teachers);

        return jsonString ;
    }

}
