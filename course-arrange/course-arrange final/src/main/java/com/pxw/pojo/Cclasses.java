package com.pxw.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

// Course class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cclasses {
    private Integer id;
    private String classesName;
    private Integer size;
    private Course course;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Cclasses{" +
                "id=" + id +
                ", classesName='" + classesName + '\'' +
                ", size=" + size +
                ", course=" + course +
                ", remark='" + remark + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cclasses cclasses = (Cclasses) o;

        if (classesName != null ? !classesName.equals(cclasses.classesName) : cclasses.classesName != null)
            return false;
        return course.getId() != null ? course.getId().equals(cclasses.course.getId()) : cclasses.course.getId() == null;
    }



}
