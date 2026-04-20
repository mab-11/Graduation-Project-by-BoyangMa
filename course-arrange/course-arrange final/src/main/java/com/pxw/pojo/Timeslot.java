package com.pxw.pojo;

/**
 * Created by pxw on 2022/4/24 13:15
 *
 * @author pxw
 */
public class Timeslot {
    private Integer id;
    private String timeslot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
                "id=" + id +
                ", timeslot='" + timeslot + '\'' +
                '}';
    }
}
