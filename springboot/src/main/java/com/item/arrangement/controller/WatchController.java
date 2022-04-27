package com.item.arrangement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Blog;
import com.item.arrangement.entity.Video;
import com.item.arrangement.entity.Watch;
import com.item.arrangement.mapper.BlogMapper;
import com.item.arrangement.mapper.UserMapper;
import com.item.arrangement.mapper.VideoMapper;
import com.item.arrangement.mapper.WatchMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 浏览记录接口
 */
@RestController
@RequestMapping("/watch")
public class WatchController extends BaseController{

    @Resource
    WatchMapper watchMapper;

    @Resource
    BlogMapper blogMapper;

    @Resource
    VideoMapper videoMapper;

    @Resource
    UserMapper userMapper;

    //新增
    @PostMapping
    public Result<?> watch(@RequestBody Watch watch){
        if(getAccount().getType().equals("user")){
            Integer uid=getAccount().getId();
            watch.setUid(uid);
            watch.setTime(new Date());

            LambdaQueryWrapper<Watch> wrapper= Wrappers.<Watch>lambdaQuery();
            wrapper.eq(Watch::getUid,uid).eq(Watch::getContentId,watch.getContentId()).eq(Watch::getType,watch.getType());
            Watch oldRecord=watchMapper.selectOne(wrapper);


            //若已经存在该记录,更新
            if(oldRecord!=null) {
                watch.setId(oldRecord.getId());
                watchMapper.updateById(watch);
                return Result.success();
            }

            //否则新增
            watchMapper.insert(watch);
            //记录观看量
            if(watch.getType().equals("blog")) {
                Blog b=blogMapper.selectById(watch.getContentId());
                b.setWatched(b.getWatched()+1);
                blogMapper.updateById(b);
            }
            else {
                Video v=videoMapper.selectById(watch.getContentId());
                v.setWatched((v.getWatched()+1));
                videoMapper.updateById(v);
            }
            return Result.success();
        }
        else return Result.error("401","请先登录");
    }


    //查询当前用户历史记录
    @GetMapping
    public Result<?> getWatchList(){
        //查询登录用户记录
        Integer uid=getAccount().getId();
        LambdaQueryWrapper<Watch> wrapper= Wrappers.<Watch>lambdaQuery();
        wrapper.eq(Watch::getUid,uid).orderByDesc(Watch::getTime);
        List<Watch> watchHistory=watchMapper.selectList(wrapper);
        //拼接标题与封面地址
        for(Watch record:watchHistory){
            Integer cid=record.getContentId();
            if(record.getType().equals("blog")){
                record.setTitle(blogMapper.selectById(cid).getTitle());
                record.setCover(blogMapper.selectById(cid).getCover());
                record.setAuthorName(userMapper.selectById(blogMapper.selectById(cid).getAuthorId()).getNickName());
            }
            else {
                record.setTitle(videoMapper.selectById(cid).getTitle());
                record.setCover(videoMapper.selectById(cid).getCover());
                record.setAuthorName(userMapper.selectById(videoMapper.selectById(cid).getAuthorId()).getNickName());
            }
        }
        return Result.success(watchHistory);
    }
}
