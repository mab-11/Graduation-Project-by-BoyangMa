package com.pxw.control;

import com.pxw.pojo.Course;
import com.pxw.pojo.Cteacher;
import com.pxw.pojo.Room;
import com.pxw.service.CteacherService;
import com.pxw.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pxw on 2022/4/15 20:26
 *
 * @author pxw
 */


// ResponseBody returns string - or response body
@RestController
@RequestMapping("/cteacher")
public class CteacherController {

    @Autowired
    private CteacherService cteacherService;

    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Cteacher cteacher, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = cteacherService.selectByCondition(cteacher, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("selectTeacher")
    public String selectTeachreByCourse(@RequestBody Course course){
        return cteacherService.selectTeachreByCourse(course);
    }

    @RequestMapping("/update")
    public String update(@RequestBody Cteacher cteacher) {
        return cteacherService.update(cteacher);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        cteacherService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Cteacher[] cteachers ){
          return cteacherService.add(cteachers);
    }

}
