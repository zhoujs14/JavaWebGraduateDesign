package com.item.arrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.entity.Post;

public interface PostMapper extends BaseMapper<Post> {
    /**
     * 分页查询
     * @param page new Page<>(pageNum,pageSize)
     * @param type "title":根据标题查询  否则按作者昵称查询
     * @param keyWords 对应type的关建字,若为空则查询所有
     * @return postPage
     */
    Page<Post> findPage(Page<Post> page, String type, String keyWords,String tag);
}
