package com.pxw.pojo.GA;

import com.pxw.pojo.*;


import java.util.HashMap;
import java.util.List;

public class Bootstrap {
    private HashMap<Integer,Room> rooms;
    private HashMap<Integer,Timeslot> timeslots;
    // Teaching tasks
    private HashMap<Integer,Task> tasks;
    // Course scheduling result
    private CourseTable tables[];

    private int numTables = 0;

    // Initialize new timetable - create object


    public Bootstrap(HashMap<Integer,Room> rooms, HashMap<Integer,Timeslot> timeslots, HashMap<Integer,Task> tasks) {
        this.rooms = rooms;
        this.timeslots = timeslots;
        this.tasks = tasks;
    }

    public Bootstrap(Bootstrap cloneable ) {
        this.rooms = cloneable.getRooms();
        this.timeslots = cloneable.getTimeslots();
        this.tasks = cloneable.getTasks();
    }

    public Bootstrap() {
        this.rooms = new HashMap<Integer,Room>();
        this.tasks = new HashMap<Integer,Task>();
        this.timeslots = new HashMap<Integer,Timeslot>();
    }

    public HashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        for (Room room : rooms){
            this.rooms.put(room.getId(),room);
        }
    }

    public HashMap<Integer, Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        for (Timeslot timeslot : timeslots){
            // Only add working hours to scheduling time
            if ((timeslot.getId()-1)%5 !=4 && timeslot.getId()<26 ){
                this.timeslots.put(timeslot.getId(),timeslot);
            }
        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    // Get all learning groups
    public Task[] getTasksAsArray(){
        return (Task[]) this.tasks.values().toArray(new Task[this.tasks.size()]);
    }

    public void setTasks(List<Task> tasks) {
        for (Task task :tasks){
            this.tasks.put(task.getId(),task);
        }
    }

    // Get random room
    public Room getRandomRoom(){
        Object[] roomsArray = this.rooms.values().toArray();
        Room room = (Room) roomsArray[(int) (Math.random() * roomsArray.length)];
        return room;
    }

    // Get room by specified id
    public Room getRoom(int roomId){
        if(!this.rooms.containsKey(roomId)){
            System.out.println("Rooms doesn't contain key" +roomId);
        }
        return (Room) this.rooms.get(roomId);
    }

    // Get teaching task by specified id
    public Task getTask(int taskId){
        return (Task) this.tasks.get(taskId);
    }


    // Get random timeslot
    public Timeslot getRandomTimeslot(){
        Object[] timeslotArray = timeslots.values().toArray();
        Timeslot timelot =(Timeslot) timeslotArray[(int) (Math.random() * timeslotArray.length)];
        return timelot;
    }

    // Get teaching task array
    public CourseTable[] getTables(){
        return this.tables;
    }

    // Get number of teaching tasks that need to be scheduled
    public  int getNumTables(){
        if(this.numTables >0){
            return numTables;
        }

        numTables = this.tasks.size();

        return this.numTables;
    }

    // Calculate conflicts, return conflict count
    public int calcClashes(){
        int clashes =0;

        for(CourseTable tableA : this.tables){
            // Check room capacity
            int roomCapacity = this.getRoom(tableA.getRoom().getId()).getCapacity();
            int groupSize = this.getTask(tableA.getTask().getId()).getCclasses().getSize();

            if (roomCapacity<groupSize){
                clashes++;
            }

            // Check if room is already used
            for (CourseTable tableB :this.tables){
                if (tableA.getRoom().getId().equals(tableB.getRoom().getId())
                        && tableA.getTimeslot().getId().equals(tableB.getTimeslot().getId())
                        && ! tableA.getTask().getId().equals(tableB.getTask().getId())){
                    clashes++;
                    break;
                }
            }

            // Check if teacher is available
            for(CourseTable tableB : this.tables){
                if(tableA.getTask().getTeacher().getId().equals(tableB.getTask().getTeacher().getId())
                        && tableA.getTimeslot().getId().equals(tableB.getTimeslot().getId())
                        && !tableA.getTask().getId().equals(tableB.getTask().getId())){
                    clashes++;
                    break;
                }
            }

            // Check if class is available
            for(CourseTable tableB : this.tables){
                boolean flag = false;
                String[] names = tableA.getTask().getCclasses().getClassesName().split(",");
                String classesNameB = tableB.getTask().getCclasses().getClassesName();
                for (String classesNameA :names){
                    // Class contains means same individual class
                    if (classesNameB.contains(classesNameA)){
                        flag = true;
                        break;
                    }
                }
                if( flag && tableA.getTimeslot().getId().equals(tableB.getTimeslot().getId())
                        && !tableA.getTask().getId().equals(tableB.getTask().getId())){
                    clashes++;
                    break;
                }
            }


        }
        return clashes;

    }




    // Use chromosome encoding to create timetable
    public void createTable(Individual individual){
        // Initialize timetable
        CourseTable[] tables = new CourseTable[this.getNumTables()];

        // Get individual's chromosome
        int[] chromsome = individual.getChromsome();
        int chromesomePos = 0;
        int tablesIndex = 0;

        // Initialize, assign randomly
        for (Task task : this.getTasksAsArray()) {

            tables[tablesIndex] = new CourseTable();
            // Set teaching task
            tables[tablesIndex].setTask(task);

            // Set timeslot
            Timeslot timeslot = new Timeslot();
            timeslot.setId(chromsome[chromesomePos]);
            tables[tablesIndex].setTimeslot(timeslot);
            chromesomePos++;

            // Set room
            Room room = new Room();
            room.setId(chromsome[chromesomePos]);
            tables[tablesIndex].setRoom(room);
            chromesomePos++;

            tablesIndex++;
        }

        // Assign timetable
        this.tables = tables;
    }


}
