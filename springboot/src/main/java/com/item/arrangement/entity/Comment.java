package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName ("comment")
@Data
public class Comment {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String type;
    private Integer authorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private String content;
    private Integer parentId;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String avatarSrc;
}
