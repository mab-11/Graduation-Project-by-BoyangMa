package com.pxw.mapper;

import com.pxw.pojo.Cclasses;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CclassesMapper {

    @Select("select * from classes_course")
    @ResultMap("CclassesResultMap")
    public List<Cclasses> selectAll();

    @Select("select * from classes_course where cc_id = #{id}")
    @ResultMap("CclassesResultMap2")
    public Cclasses selectById(Integer id);

    @Select("select * from classes_course where cc_name = #{classesName} ")
    @ResultMap("CclassesResultMap")
    public  List<Cclasses> selectByName(String classesName);

    @ResultMap("CclassesResultMap")
    public List<Cclasses> selectByCondition(Cclasses cclasses);

    @Update("update classes_course set cc_name=#{classesName},course_id = #{course.id},cc_size = #{size},cc_remark = #{remark} where cc_id = #{id}")
    public void update(Cclasses cclasses);


    @Insert("insert into classes_course values(null,#{classesName},#{course.id},#{size},#{remark})")
    public void add(Cclasses cclasses);

    public void deleteByIds(int[] ids);



}
