package com.pxw.mapper;

import com.pxw.pojo.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pxw on 2022/4/22 15:39
 *
 * @author pxw
 */

@Mapper
public interface TaskMapper {


    public List<Task> selectAll();

    public Task selectById(int id);

    // Query by each name fuzzy
    public List<Task> selectByCondition(Task task);
    // Query by each id exact
    public Task selectByCondition2(Task task);

    public void deleteByIds(int[] ids);


    @Update("update task set cc_id = #{cclasses.id},teacher_id = #{teacher.id}," +
            "start_week = #{startWeek},end_week = #{endWeek},week_node = #{weekNode}" +
            " where id = #{id}" )
    public void update(Task task);

    @Insert("insert into task values(null,#{cclasses.id},#{teacher.id} ,#{startWeek},#{endWeek}, #{weekNode})")
    public void add(Task task);

}
