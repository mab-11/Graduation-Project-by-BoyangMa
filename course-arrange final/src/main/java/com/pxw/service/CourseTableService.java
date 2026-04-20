package com.pxw.service;

import com.pxw.pojo.CourseTable;
import com.pxw.pojo.Task;

/**
 * Created by pxw on 2022/4/22 16:03
 *
 * @author pxw
 */

public interface CourseTableService {

    public String selectAll( Integer currentPage,  Integer pageSize);

    public String selectByCondition(CourseTable courseTable, Integer currentPage, Integer pageSize);

    public String selectEmptyRoom(CourseTable courseTable,Integer currentPage,Integer pageSize);

    public String selectTable(CourseTable courseTable);

    public String selectTask(Task task,Integer currentPage,Integer pageSize);


    public void deleteByIds(int[] ids);

    public String update(CourseTable courseTable);

    public String add(CourseTable courseTable);

    public String GA();

    /**
     * Basic course scheduling algorithm (keep original implementation)
     */
    public String GABasic();

    /**
     * Run experiment comparison
     */
    public String runExperiment();


}
