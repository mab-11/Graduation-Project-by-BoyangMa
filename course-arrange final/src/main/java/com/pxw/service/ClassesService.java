package com.pxw.service;

import com.pxw.pojo.Classes;


public interface ClassesService {

    public String selectByCondition(Classes classes,Integer currentPage,Integer pageSize);

    public String update(Classes classes,boolean flag);

    public void deleteByIds(int[] ids);

    public String add(Classes classes);

}
