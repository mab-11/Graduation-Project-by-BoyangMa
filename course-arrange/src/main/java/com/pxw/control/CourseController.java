package com.pxw.control;

import com.pxw.pojo.Course;
import com.pxw.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Course course, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = courseService.selectByCondition(course, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("/update")
    public String update(@RequestBody Course course,@RequestParam("flag") boolean flag) {
        System.out.println(flag);
        return courseService.update(course,flag);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        courseService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Course course){
          return courseService.add(course);
    }

}
