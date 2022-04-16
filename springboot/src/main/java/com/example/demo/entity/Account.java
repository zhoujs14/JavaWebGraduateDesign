package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Account {
    private Integer id;
    private String username;
    private String password;
    @TableField(exist = false)
    private String type;
}
