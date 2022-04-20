package com.example.demo.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token验证
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private AdminMapper adminMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            System.out.println("未获取token; request url:"+request.getRequestURI());
            throw new CustomException("401", "未获取到token, 请重新登录");
        }
        Integer id = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        String type=JWT.decode(token).getClaim("type").asString();

        Account a;
        if(type.equals("admin")) a=adminMapper.selectById(id);
        else a=userMapper.selectById(id);

        if (a == null) {
            throw new CustomException("401", "token不合法");
        }

        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(a.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "token不合法");
        }
        return true;
    }
}
