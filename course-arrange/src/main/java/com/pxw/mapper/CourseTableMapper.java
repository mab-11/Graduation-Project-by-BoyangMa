package com.pxw.mapper;

import com.pxw.pojo.CourseTable;
import com.pxw.pojo.Task;
import com.pxw.pojo.Timeslot;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pxw on 2022/4/24 13:18
 *
 * @author pxw
 */


@Mapper
public interface CourseTableMapper {



    public List<CourseTable> selectByCondition(CourseTable courseTable);

    public List<CourseTable> selectByCondition2(@Param("ccName") String classesName,@Param("timeId") Integer timeId,@Param("teacherId") Integer teacherId);

    public List<CourseTable> selectAll();

    public List<CourseTable> selectEmptyRoom(CourseTable courseTable);

    public List<CourseTable> selectTable(CourseTable courseTable);

    public List<Task> selectTask(Task task);

    @Select("select * from timeslot")
    @ResultMap("TimeslotResultMap")
    public List<Timeslot> selectAllTimeslot();

    @Update("update course_table set room_id =#{room.id},timeslot_id = #{timeslot.id} where task_id = #{task.id}")
    public void update(CourseTable courseTable);

    public void deleteByIds(int[] ids);

    public void deleteAll();

    public void addCourseTables(CourseTable[] tables);


    @Insert("insert into course_table values( #{task.id}, #{room.id}, #{timeslot.id} )")
    public void addCourseTable(CourseTable table);


}
