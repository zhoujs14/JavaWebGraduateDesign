package com.item.arrangement.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Blog;
import com.item.arrangement.entity.Video;
import com.item.arrangement.entity.Likes;
import com.item.arrangement.mapper.BlogMapper;
import com.item.arrangement.mapper.VideoMapper;
import com.item.arrangement.mapper.LikesMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 点赞接口
 */
@RestController
@RequestMapping("/likes")
public class LikesController extends BaseController{

    @Resource
    LikesMapper likesMapper;

    @Resource
    BlogMapper blogMapper;

    @Resource
    VideoMapper videoMapper;

    //新增
    @PostMapping
    public Result<Integer> like(@RequestBody Likes likes){
        if(getAccount().getType().equals("user")){
            Integer uid=getAccount().getId();
            likes.setUid(uid);
            likes.setTime(new Date());

            LambdaQueryWrapper<Likes> wrapper= Wrappers.<Likes>lambdaQuery();
            wrapper.eq(Likes::getUid,uid).eq(Likes::getContentId,likes.getContentId()).eq(Likes::getType,likes.getType());
            Likes oldRecord=likesMapper.selectOne(wrapper);


            //若已经存在该记录,取消点赞
            if(oldRecord!=null) {
                likesMapper.deleteById(oldRecord.getId());
                //更新点赞数
                if(likes.getType().equals("blog")) {
                    Blog b=blogMapper.selectById(likes.getContentId());
                    b.setLikesCount(b.getLikesCount()-1);
                    blogMapper.updateById(b);
                }
                else {
                    Video v=videoMapper.selectById(likes.getContentId());
                    v.setLikesCount((v.getLikesCount()-1));
                    videoMapper.updateById(v);
                }
                return Result.success(-1);
            }

            //否则新增
            else{
                likesMapper.insert(likes);
                //记录点赞数
                if(likes.getType().equals("blog")) {
                    Blog b=blogMapper.selectById(likes.getContentId());
                    b.setLikesCount(b.getLikesCount()+1);
                    blogMapper.updateById(b);
                }
                else {
                    Video v=videoMapper.selectById(likes.getContentId());
                    v.setLikesCount((v.getLikesCount()+1));
                    videoMapper.updateById(v);
                }
                return Result.success(1);
            }
        }
        else return Result.error("401","请先登录");
    }


    //查询是否点赞
    @GetMapping
    public Result<Integer> queryIsLike(@RequestParam String type,@RequestParam Integer cid)
    {
        //查询登录用户记录
        Integer uid=getAccount().getId();
        LambdaQueryWrapper<Likes> wrapper= Wrappers.<Likes>lambdaQuery();
        wrapper.eq(Likes::getUid,uid).eq(Likes::getType,type).eq(Likes::getContentId,cid);
        if(likesMapper.selectOne(wrapper)!=null){
            return Result.success(1);
        }
        else return Result.success(-1);
    }
}
