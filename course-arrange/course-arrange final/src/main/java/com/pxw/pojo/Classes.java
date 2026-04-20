package com.pxw.pojo;


public class Classes {
    private Integer id;
    private String classesName;
    private Integer size;
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

    public void setClassesName(String className) {
        this.classesName = className;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "id=" + id +
                ", className='" + classesName + '\'' +
                ", size=" + size +
                ", remark='" + remark + '\'' +
                '}';
    }

}
