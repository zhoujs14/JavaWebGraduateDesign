package com.item.arrangement.controller;

import com.auth0.jwt.JWT;
import com.item.arrangement.entity.Account;
import com.item.arrangement.entity.Admin;
import com.item.arrangement.entity.User;
import com.item.arrangement.mapper.AdminMapper;
import com.item.arrangement.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据token获取用户信息
     * @return admin
     */
    public Admin getAdmin() {
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        Integer adminId = Integer.valueOf(aud);
        return adminMapper.selectById(adminId);
    }

    public User getUser() {
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        Integer userId = Integer.valueOf(aud);
        return userMapper.selectById(userId);
    }

    public Account getAccount(){
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        String type=JWT.decode(token).getClaim("type").asString();
        if(type.equals("admin"))
            return adminMapper.selectById(Integer.valueOf(aud));
        return userMapper.selectById(Integer.valueOf(aud));
    }
}
