package com.pxw.util;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pxw on 2022/4/29 15:38
 *
 * @author pxw
 */

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // 1. Judge whether session has user
        HttpSession session = req.getSession();
        Object user = session.getAttribute("userName");
        System.out.println(user);
        // 2. Judge whether user is null
        if (user != null) {
            System.out.println("!!!");
            // Already logged in, pass
            chain.doFilter(request, response);
        } else {

            // Judge whether accessed resource path is related to login/register
            // 1, Store login and register related resource paths in array
            String[] urls = {
                    "login.html",
                    "t/login.html",
                    "imgs/",
                    "css/",
                    "js/",
                    "element-ui/",
                    "user/login",
                    "user/checkCode"};
            // 2, Get current accessed resource path
            String url = req.getRequestURL().toString();
            // 3, Iterate through array, get each resource path that needs to pass
            for (String u : urls) {
                // 4, Judge whether current accessed resource path string contains the resource path string that needs to pass
                if (url.contains(u)) {
                    System.out.println(url);
                    // Found, pass
                    chain.doFilter(request, response);
                    //break;
                    return;
                }
            }
            // Not logged in, store prompt message, redirect to login page
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/login.html?1");
        }







    }
}
