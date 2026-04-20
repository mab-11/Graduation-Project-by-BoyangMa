package com.pxw.control;

import com.pxw.mapper.RoomMapper;
import com.pxw.pojo.Room;
import com.pxw.service.RoomService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody Room room, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        String jsonStr = roomService.selectByCondition(room, currentPage, pageSize);
        return jsonStr;
    }

    @RequestMapping("/update")
    public String update(@RequestBody Room room,@RequestParam("flag") boolean flag) {
        System.out.println(flag);
        return roomService.update(room,flag);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        roomService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody Room room){
          return roomService.add(room);
    }

}
