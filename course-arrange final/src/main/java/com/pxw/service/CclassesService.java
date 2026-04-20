package com.pxw.service;

import com.pxw.pojo.Cclasses;


public interface CclassesService {

    public String selectByCondition(Cclasses cclasses, Integer currentPage, Integer pageSize);

    public String update(Cclasses cclasses);

    public void deleteByIds(int[] ids);

    public String add(Cclasses cclasses);

}
