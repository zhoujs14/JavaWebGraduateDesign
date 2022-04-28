package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName ("star")
@Data
public class Star{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer contentId;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private String cover;

    @TableField(exist = false)
    private String authorName;
}
