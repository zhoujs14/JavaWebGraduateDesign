package com.item.arrangement.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.item.arrangement.entity.Account;
import com.item.arrangement.entity.Admin;
import com.item.arrangement.entity.User;
import com.item.arrangement.mapper.AdminMapper;
import com.item.arrangement.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class TokenUtils {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    private static UserMapper staticUserMapper;

    private static AdminMapper staticAdminMapper;

    @PostConstruct
    public void init() {
        staticUserMapper = userMapper;
        staticAdminMapper = adminMapper;
    }

    /**
     * 生成token
     * @param account
     * @return
     */
    public static String genToken(Account account) {
        return JWT.create().withExpiresAt(DateUtil.offsetDay(new Date(), 1)).withAudience(account.getId().toString())
                .withClaim("type",account.getType()).sign(Algorithm.HMAC256(account.getPassword()));
    }

    public static Account getAccount(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            String aud = JWT.decode(token).getAudience().get(0);
            String type=JWT.decode(token).getClaim("type").toString();
            if(type.equals("admin")){
                return staticAdminMapper.selectById(Integer.valueOf(aud));
            }
            return staticUserMapper.selectById(Integer.valueOf(aud));
        }catch (Exception e){
            log.error("解析token失败", e);
            return null;
        }
    }

    /**
     * 获取token中的用户信息
     * @return
     */
    public static User getUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            String aud = JWT.decode(token).getAudience().get(0);
            Integer userId = Integer.valueOf(aud);
            return staticUserMapper.selectById(userId);
        } catch (Exception e) {
            log.error("解析token失败", e);
            return null;
        }
    }

    /**
     * 获取token中的管理员信息
     * @return
     */
    public static Admin getAdmin() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            String aud = JWT.decode(token).getAudience().get(0);
            Integer adminId = Integer.valueOf(aud);
            return staticAdminMapper.selectById(adminId);
        } catch (Exception e) {
            log.error("解析token失败", e);
            return null;
        }
    }
}
