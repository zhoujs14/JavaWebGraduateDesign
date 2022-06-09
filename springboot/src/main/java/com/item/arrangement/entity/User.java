package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.item.arrangement.common.Result;
import lombok.Data;

@TableName ("user")
@Data
public class User extends Account {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String gender;
    private String nickName;
    private Integer age;
    private String avatarSrc;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private String type="user";

    @TableField(exist = false)
    private String oldPwd;

    @TableField(exist = false)
    private String newPwd;

    public static Result<?> validate(User u){
        if(u.getUsername()==null||u.getUsername().length()<2||u.getUsername().length()>18) return Result.error("-1","非法用户名");
        if(u.getPassword()==null||u.getPassword().length()<6||u.getPassword().length()>20) return Result.error("-1","非法密码");
        if(u.getNickName()==null||u.getNickName().length()<2||u.getNickName().length()>12) return Result.error("-1","非法昵称");
        if(u.getAge()!=null&&(u.getAge()>200||u.getAge()<0)) return Result.error("-1","非法年龄");
        return Result.success();
    }
}
