package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
}
