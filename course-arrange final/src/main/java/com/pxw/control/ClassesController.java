package com.pxw.control;

import com.pxw.pojo.Classes;
import com.pxw.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/classes")
public class ClassesController {

    @Autowired
    private ClassesService ClassesService;

    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Classes classes, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = ClassesService.selectByCondition(classes, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("/update")
    public String update(@RequestBody Classes classes,@RequestParam("flag") boolean flag) {
        System.out.println(flag);
        return ClassesService.update(classes,flag);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        ClassesService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Classes classes){
          return ClassesService.add(classes);
    }

}
