package com.pxw.service;

import com.pxw.pojo.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by pxw on 2022/4/22 16:03
 *
 * @author pxw
 */

public interface TaskService {

    public String selectAll( Integer currentPage,  Integer pageSize);

    public String selectByCondition(Task task , Integer currentPage, Integer pageSize);

    public void deleteByIds(int[] ids);

    public String update(Task task);

    public String add(Task task);


}
