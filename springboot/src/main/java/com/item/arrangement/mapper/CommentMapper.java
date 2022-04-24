package com.item.arrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.entity.Comment;

public interface CommentMapper extends BaseMapper<Comment> {
    Page<Comment> findBlogPage(Page<Comment> page,Integer blogId);
    Page<Comment> findVideoPage(Page<Comment> page,Integer videoId);
}
