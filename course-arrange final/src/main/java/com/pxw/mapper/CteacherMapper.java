package com.pxw.mapper;

import com.pxw.pojo.Course;
import com.pxw.pojo.Cteacher;
import com.pxw.pojo.Room;
import com.pxw.pojo.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by pxw on 2022/4/15 16:29
 *
 * @author pxw
 */

@Mapper
public interface CteacherMapper {

    @Select("select * from teacher_course")
    @ResultMap("CteacherResultMap")
    public List<Cteacher> selectAll();

    @Select("select * from teacher_course where id = #{id}")
    @ResultMap("CteacherResultMap2")
    public Cteacher selectById(Integer id);


    @ResultMap("CteacherResultMap")
    public List<Cteacher> selectByCondition(Cteacher cteacher);

    @Update("update teacher_course set teacher_id=#{teacher.id},course_id = #{course.id},remark = #{remark} where id = #{id}")
    public void update(Cteacher cteacher);


    @Insert("insert into teacher_course values(null,#{course.id},#{teacher.id},#{remark})")
    public void add(Cteacher cteacher);

    public void deleteByIds(int[] ids);



    public List<Teacher> selectTeachreByCourse(Course course);



}
