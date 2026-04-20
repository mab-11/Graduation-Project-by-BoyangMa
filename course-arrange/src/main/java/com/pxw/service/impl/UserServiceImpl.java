package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.UserMapper;
import com.pxw.pojo.User;
import com.pxw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String userName,String password) {
        User user = userMapper.select(userName, password);
        return user;
    }

    @Override
    public  String selectByCondition(User user ,Integer currentPage,Integer pageSize) {
        // Pagination fuzzy query settings
        PageHelper.startPage(currentPage,pageSize);
        List<User> users = userMapper.selectByCondition(user);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        // userPageInfo.getList();
        // userPageInfo.getTotal();
        // Frontend should take list attribute and total attribute
        String pageInfo = JSON.toJSONString(userPageInfo);
        return pageInfo;
    }

    @Override
    public String add(User newUser) {
        // Query whether username already exists
        User user = userMapper.selectByName(newUser.getUserName());
        if (user!=null){
            return "fail";
        }else {
            // Add
            userMapper.add(newUser);
            return "success";
        }
    }

    @Override
    public String disableByIds(int[] ids) {
        userMapper.disableByIds(ids);
        return "success";
    }

    @Override
    public String update(User user ,@RequestParam("flag") boolean flag) {
        // Name modified, judge whether it is duplicate
        if (flag){
            if (userMapper.selectByName(user.getUserName()) != null) {
                return "fail";
            } else {
                userMapper.update(user);
                return "success";
            }
        }else {
            userMapper.update(user);
            return "success";
        }

    }

    @Override
    public String deleteById(User user) {
        Integer id = user.getId();
        userMapper.deleteById(id);
        return "success";
    }


}
