package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.item.arrangement.common.Result;
import lombok.Data;

/**
 * 管理员
 */
@TableName ("admin")
@Data
public class Admin extends Account {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer level;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private String type="admin";

    public static Result<?> validate(Admin a){
        if(a.getUsername()==null||a.getUsername().length()<2||a.getUsername().length()>18) return Result.error("-1","用户名长度在2~18个字符间");
        if(a.getPassword()==null||a.getPassword().length()<6||a.getPassword().length()>20) return Result.error("-1","密码长度在2~20个字符间");
        return Result.success();
    }
}
