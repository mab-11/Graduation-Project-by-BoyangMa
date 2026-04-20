package com.pxw.control;

import com.pxw.pojo.User;
import com.pxw.service.UserService;
import com.pxw.util.CheckCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Login
    @RequestMapping("/login")
    public String login(@RequestBody User user, @RequestParam(required = false) String checkCode, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();
        session.setAttribute("checkCodeGen", "DEV");

        User result = userService.login(user.getUserName(), user.getPassword());
        if (result != null){
            session.setAttribute("userName",result.getUserName());
            return "success" ;
        }
        return "Username or password error";

    }

    // Verification code generation
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Generate verification code and return
        String checkCodeGen = CheckCodeUtil.generateVerifyCode(4);

        // Store in session
        HttpSession session = request.getSession();
        session.setAttribute("checkCodeGen",checkCodeGen);

        System.out.println(session.getId());
        // Output image
        ServletOutputStream os = response.getOutputStream();
        CheckCodeUtil.outputImage(100,40,os,checkCodeGen);

    }

    // Get login status
    @RequestMapping("/checkLogin")
    public String checkLogin(HttpSession session){
        return (String)session.getAttribute("userName");
    }

    // Logout
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        // Clear session information
        session.setAttribute("userName",null);
        return "success";
    }

    // Query
    @RequestMapping("/selectByCondition")
    public String selectByCondition(@RequestBody  User user ,@RequestParam("currentPage") Integer currentPage,@RequestParam("pageSize") Integer pageSize){
        // Call service to query all
        String  pageInfo = userService.selectByCondition(user,currentPage,pageSize);
        return pageInfo;
    }

    // Add user
    @RequestMapping("/add")
    public String add(@RequestBody User user){
        return userService.add(user);
    }

    // Batch disable
    @RequestMapping("/disableByIds")
    public String disableByIds(@RequestBody int[] ids){
        return userService.disableByIds(ids);
    }

    // Update
    @RequestMapping("update")
    public String update(@RequestBody User user,@RequestParam("flag") boolean flag){
        return userService.update(user,flag);
    }

    // Delete
    @RequestMapping("deleteById")
    public String delete(@RequestBody User user){
        return userService.deleteById(user);
    }

}
