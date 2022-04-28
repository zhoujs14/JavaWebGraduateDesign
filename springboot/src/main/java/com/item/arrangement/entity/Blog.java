package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName ("blog")
@Data
public class Blog {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer authorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private Integer cateId;
    private Integer likesCount;
    private Integer watched;
    private String brief;
    private String cover;
    private Integer locationId;
    private Integer star;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String cateName;

    @TableField(exist = false)
    private String locationName;
}
