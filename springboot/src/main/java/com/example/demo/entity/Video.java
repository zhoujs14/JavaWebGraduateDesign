package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName ("video")
@Data
public class Video{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer authorId;
    private Integer cateId;
    private Integer locationId;
    private String uuid;
    private String title;
    private String brief;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private Integer likesCount;
    private Integer watched;
    private String cover;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String cateName;

    @TableField(exist = false)
    private String locationName;
}
