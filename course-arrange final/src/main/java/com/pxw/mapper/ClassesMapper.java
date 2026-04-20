package com.pxw.mapper;

import com.pxw.pojo.Classes;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ClassesMapper {

    @Select("select * from classes")
    @ResultMap("ClassesResultMap")
    public List<Classes> selectAll();

    @Select("select * from classes where id = #{id}")
    @ResultMap("ClassesResultMap")
    public Classes selectById(Integer id);

    @Select("select * from classes where c_name = #{classesName}")
    @ResultMap("ClassesResultMap")
    public Classes selectByName(String classesName);

    @ResultMap("ClassesResultMap")
    public List<Classes> selectByCondition(@Param("classes") Classes aclasses);

    @Update("update classes set c_name=#{classesName},size = #{size},remark = #{remark} where id = #{id}")
    public void update(Classes aclasses);


    @Insert("insert into classes values(null,#{size},#{classesName},#{remark})")
    public void add(Classes aclasses);

    public void deleteByIds(int[] ids);



}
