package com.pxw.service;

import com.pxw.mapper.UserMapper;
import com.pxw.pojo.User;

import java.util.List;

public interface UserService {

    public User login(String userName,String password);

    public String selectByCondition(User user,Integer currentPage,Integer pageSize);

    public String add(User user);

    public String disableByIds(int[] ids);

    public String update(User user,boolean flag);

    public String deleteById(User user);

}
