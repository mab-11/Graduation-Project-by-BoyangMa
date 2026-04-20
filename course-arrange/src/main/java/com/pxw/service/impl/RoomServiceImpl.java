package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.RoomMapper;
import com.pxw.pojo.Room;
import com.pxw.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pxw on 2022/4/15 20:31
 *
 * @author pxw
 */

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public String selectByCondition(Room room, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Room> rooms = roomMapper.selectByCondition(room);
        // Pagination info
        PageInfo<Room> roomPageInfo = new PageInfo<>(rooms);

        return JSON.toJSONString(roomPageInfo);
    }

    /**
     *
     * @param room
     * @param flag Whether name is modified
     * @return
     */
    @Override
    public String update(Room room, boolean flag) {
        // Name modified, judge whether duplicate name
        if (flag){
            if (roomMapper.selectByName(room.getRoomName()) != null) {
                return "fail";
            } else {
                roomMapper.update(room);
                return "success";
            }
        }else {
            roomMapper.update(room);
            return "success";
        }



    }

    @Override
    public void deleteByIds(int[] ids) {
        roomMapper.deleteByIds(ids);
    }

    @Override
    public String add(Room NewRoom) {

        if (roomMapper.selectByName(NewRoom.getRoomName()) != null) {
            return "false";
        } else {
            roomMapper.add(NewRoom);
            return "success";
        }

    }

}
