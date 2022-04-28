package com.item.arrangement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.entity.Star;

public interface StarMapper extends BaseMapper<Star> {
    /**
     * 分页查询
     * @param page new Page<>(pageNum,pageSize)
     * @param type "title":根据标题查询  否则按作者昵称查询
     * @param keyWords 对应type的关建字,若为空则查询所有
     * @param cid 收纳物品类别id
     * @param lid 收纳位置id
     * @return blogPage
     */
    Page<Star> findVideoPage(Page<Star> page,Integer uid, String type, String keyWords, Integer cid, Integer lid);
    /**
     * 分页查询
     * @param page new Page<>(pageNum,pageSize)
     * @param type "title":根据标题查询  否则按作者昵称查询
     * @param keyWords 对应type的关建字,若为空则查询所有
     * @param cid 收纳物品类别id
     * @param lid 收纳位置id
     * @return blogPage
     */
    Page<Star> findBlogPage(Page<Star> page,Integer uid, String type, String keyWords, Integer cid, Integer lid);
}
