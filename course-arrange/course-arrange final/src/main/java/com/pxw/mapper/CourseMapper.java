package com.pxw.mapper;

import com.pxw.pojo.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface CourseMapper {

    @Select("select * from course")
    @ResultMap("CourseResultMap")
    public List<Course> selectAll();

    @Select("select * from course where c_id = #{id}")
    @ResultMap("CourseResultMap")
    public Course selectById(Integer id);

    @Select("select * from course where c_name = #{courseName}")
    @ResultMap("CourseResultMap")
    public Course selectByName(String courseName);

    @Select("select * from course where c_code = #{code}")
    @ResultMap("CourseResultMap")
    public Course selectByCode(String code);

    @ResultMap("CourseResultMap")
    public List<Course> selectByCondition(@Param("course") Course course);

    @Update("update course set c_name=#{courseName},c_code = #{code},c_remark = #{remark} where c_id = #{id}")
    public void update(Course course);


    @Insert("insert into course values(null,#{code},#{courseName},#{remark})")
    public void add(Course course);

    public void deleteByIds(int[] ids);



}
