package com.pxw.mapper;

import com.pxw.pojo.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pxw on 2022/4/15 16:29
 *
 * @author pxw
 */

@Mapper
public interface RoomMapper {

    @Select("select * from room")
    @ResultMap("RoomResultMap")
    public List<Room> selectAll();

    @Select("select * from room where id = #{id}")
    @ResultMap("RoomResultMap")
    public Room selectById(Integer id);

    @Select("select * from room where r_name = #{roomName}")
    @ResultMap("RoomResultMap")
    public Room selectByName(String roomName);

    @ResultMap("RoomResultMap")
    public List<Room> selectByCondition(@Param("room") Room room);

    @Update("update room set r_name=#{roomName},capacity = #{capacity},remark = #{remark} where id = #{id}")
    public void update(Room room);


    @Insert("insert into room values(null,#{roomName},#{capacity},#{remark})")
    public void add(Room room);

    public void deleteByIds(int[] ids);



}
