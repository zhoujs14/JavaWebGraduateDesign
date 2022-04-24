package com.item.arrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.entity.Video;

public interface VideoMapper extends BaseMapper<Video> {
    //分页查询,连接作者昵称
    Page<Video> findPage(Page<Video> page);
}
