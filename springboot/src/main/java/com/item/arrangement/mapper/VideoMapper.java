package com.item.arrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.entity.Video;

public interface VideoMapper extends BaseMapper<Video> {
    /**
     * 分页查询
     * @param page new Page<>(pageNum,pageSize)
     * @param type "title":根据标题查询  否则按作者昵称查询
     * @param keyWords 对应type的关建字,若为空则查询所有
     * @param cid 收纳物品类别id
     * @param lid 收纳位置id
     * @return blogPage
     */
    Page<Video> findPage(Page<Video> page,String type,String keyWords,Integer cid,Integer lid);
}
