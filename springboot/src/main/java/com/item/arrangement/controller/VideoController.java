package com.item.arrangement.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Video;
import com.item.arrangement.mapper.VideoMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 视频接口
 */
@RestController
@RequestMapping("/video")
public class VideoController extends BaseController{

    @Resource
    VideoMapper videoMapper;

    //新增视频
    @PostMapping
    public Result<?> save(@RequestBody Video video){
        if(getAccount().getType().equals("user")){
            video.setAuthorId(getAccount().getId());
        }
        video.setTime(new Date()); //设置创建时间
        videoMapper.insert(video);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Video video){
        videoMapper.updateById(video);
        return Result.success();
    }

    //删除博客
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        videoMapper.deleteById(id);
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字
        LambdaQueryWrapper<Video> wrapper= Wrappers.<Video>lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(Video::getTitle,keyWords); //输入不为空才使用like模糊查询
        Page<Video> videoPage= videoMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(videoPage);
    }
}
