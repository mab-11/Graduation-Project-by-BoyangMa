package com.pxw.mapper;

import com.pxw.pojo.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pxw on 2022/4/16 19:17
 *
 * @author pxw
 */
@Mapper
public interface TeacherMapper {

    @Select("select * from teacher ")
    @ResultMap("TeacherResultMap")
    public List<Teacher> selectAll();

    @Select("select * from teacher where id=#{id} and password = #{password} ")
    @ResultMap("TeacherResultMap")
    public Teacher login(Teacher teacher);



    @Select("select * from teacher where id = #{id}")
    @ResultMap("TeacherResultMap")
    public Teacher selectById(Integer id);

    @Select("select * from teacher where t_name = #{teacherName}")
    @ResultMap("TeacherResultMap")
    public Teacher selectByName(String teacherName);

    @ResultMap("TeacherResultMap")
    public List<Teacher> selectByCondition(@Param("teacher") Teacher teacher);

    @Update("update teacher set id = #{teacher.id}, t_name=#{teacher.teacherName},password= #{password},remark = #{teacher.remark} where id = #{oldId}")
    public void update1(@Param("teacher") Teacher teacher,@Param("oldId") String oldId);

    // Cannot overload, method name is key, hashMap key is unique
    @Update("update teacher set t_name=#{teacherName},password= #{password},remark = #{remark}where id = #{id}")
    public void update2(Teacher teacher);


    @Insert("insert into teacher values(#{id},#{teacherName},#{password},#{remark})")
    public void add(Teacher teacher);

    public void deleteByIds(int[] ids);



}
