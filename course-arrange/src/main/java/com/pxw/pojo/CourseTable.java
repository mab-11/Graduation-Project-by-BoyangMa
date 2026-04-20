package com.pxw.pojo;

/**
 * Created by pxw on 2022/4/24 13:12
 *
 * @author pxw
 */
public class CourseTable {
    private Task task;
    private Room room;
    private Timeslot timeslot;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public CourseTable(Task task, Room room, Timeslot timeslot) {
        this.task = task;
        this.room = room;
        this.timeslot = timeslot;
    }

    public CourseTable() {
    }


}
