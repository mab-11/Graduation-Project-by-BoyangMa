package com.pxw.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by pxw on 2022/4/22 15:11
 *
 * @author pxw
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    private Integer id;
    private Cclasses cclasses;
    private Teacher teacher;
    private Integer startWeek;
    private Integer endWeek;
    private Integer weekNode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cclasses getCclasses() {
        return cclasses;
    }

    public void setCclasses(Cclasses cclasses) {
        this.cclasses = cclasses;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(Integer startWeek) {
        this.startWeek = startWeek;
    }



    public Integer getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(Integer endWeek) {
        this.endWeek = endWeek;
    }

    public Integer getWeekNode() {
        return weekNode;
    }

    public void setWeekNode(Integer weekNode) {
        this.weekNode = weekNode;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", cclasses=" + cclasses +
                ", teacher=" + teacher +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekNode=" + weekNode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (cclasses.getId() != null ? !cclasses.getId().equals(task.cclasses.getId()) : task.cclasses.getId() != null) return false;
        return weekNode != null ? weekNode.equals(task.weekNode) : task.weekNode == null;
    }

}
