package com.pxw.control;

import com.pxw.pojo.Cclasses;
import com.pxw.pojo.Classes;
import com.pxw.service.CclassesService;
import com.pxw.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cclasses")
public class CclassesController {

    @Autowired
    private CclassesService cclassesService;

    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Cclasses cclasses, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = cclassesService.selectByCondition(cclasses, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("/update")
    public String update(@RequestBody Cclasses cclasses) {
        return cclassesService.update(cclasses);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        cclassesService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Cclasses cclasses){
          return cclassesService.add(cclasses);
    }

}
