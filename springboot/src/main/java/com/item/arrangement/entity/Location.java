package com.item.arrangement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName ("location")
@Data
public class Location {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
}
