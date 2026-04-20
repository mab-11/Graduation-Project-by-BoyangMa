package com.pxw.service;

import com.pxw.pojo.Room;

/**
 * Created by pxw on 2022/4/15 20:30
 *
 * @author pxw
 */



public interface RoomService {

    public String selectByCondition(Room room,Integer currentPage,Integer pageSize);

    public String update(Room room,boolean flag);

    public void deleteByIds(int[] ids);

    public String add(Room room);

}
