package com.pxw.pojo;

/**
 * Created by pxw on 2022/4/21 14:32
 *
 * @author pxw
 */
// Course-teacher relationship entity class
public class Cteacher {
    private Integer id;
    private Teacher teacher;
    private Course course;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Cteacher{" +
                "id=" + id +
                ", teacher=" + teacher +
                ", course=" + course +
                ", remark='" + remark + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cteacher cteacher = (Cteacher) o;

        if (!teacher.getId().equals(cteacher.teacher.getId())) return false;
        return course.getId().equals(cteacher.course.getId());
    }

}
