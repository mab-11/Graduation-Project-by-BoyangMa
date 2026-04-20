package com.pxw.control;

import com.pxw.pojo.Task;
import com.pxw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pxw on 2022/4/22 16:02
 *
 * @author pxw
 */


@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("selectAll")
    public String selectAll(@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return taskService.selectAll(currentPage,pageSize);
    }

    @RequestMapping("selectByCondition")
    public String selectByCondition(@RequestBody Task task, @RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return taskService.selectByCondition(task,currentPage,pageSize);
    }

    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        taskService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Task task){
        return taskService.add(task);
    }

    @RequestMapping("update")
    public String update(@RequestBody Task task){
        return taskService.update(task);
    }



}
