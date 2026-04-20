package com.pxw.pojo;

import lombok.Data;

/**
 * Course type entity class
 */
@Data
public class CourseType {
    private Integer id;
    private Integer courseId;
    private String typeName;  // Type name: lecture, lab, PE, practice, etc.

    public CourseType() {}

    public CourseType(Integer courseId, String typeName) {
        this.courseId = courseId;
        this.typeName = typeName;
    }
}
