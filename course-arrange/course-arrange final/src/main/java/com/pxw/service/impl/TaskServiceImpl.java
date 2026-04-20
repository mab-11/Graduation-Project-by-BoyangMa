package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.TaskMapper;
import com.pxw.pojo.Task;
import com.pxw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pxw on 2022/4/22 16:04
 *
 * @author pxw
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public String selectAll(Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = taskMapper.selectAll();

        // Pagination info
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        // Avoid duplicate reference $ref, frontend no data display
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String selectByCondition(Task task, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = taskMapper.selectByCondition(task);

        // Pagination info
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        // Avoid duplicate reference $ref, frontend no data display
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public void deleteByIds(int[] ids) {
        taskMapper.deleteByIds(ids);
    }

    @Override
    public String update(Task newTask) {
        Task oldTask = taskMapper.selectById(newTask.getId());
        System.out.println(oldTask);
        // Judge whether modified
        if (!newTask.equals(oldTask)) {
            // Judge whether duplicate
            if (taskMapper.selectByCondition2(newTask) != null) {
                return "fail";
            }
        }
        taskMapper.update(newTask);
        return "success";
    }

    @Override
    public String add(Task task) {
        // Judge whether duplicate
        if (taskMapper.selectByCondition2(task) != null) {
            return "fail";
        }
        taskMapper.add(task);
        return "success";
    }

}
