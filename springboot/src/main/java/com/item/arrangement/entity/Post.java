package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@TableName ("post")
@Data
public class Post {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer authorId;
    private Integer parentId;
    private Integer repostCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private String tag; //讨论贴标签

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String authorAvatar; //作者头像

    @TableField(exist = false)
    private List<Post> children; //回帖列表
}
