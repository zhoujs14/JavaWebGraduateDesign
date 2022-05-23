package com.item.arrangement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Blog;
import com.item.arrangement.entity.Star;
import com.item.arrangement.entity.Video;
import com.item.arrangement.mapper.BlogMapper;
import com.item.arrangement.mapper.StarMapper;
import com.item.arrangement.mapper.UserMapper;
import com.item.arrangement.mapper.VideoMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 收藏接口
 */
@RestController
@RequestMapping("/star")
public class StarController extends BaseController{

    @Resource
    StarMapper starMapper;

    @Resource
    BlogMapper blogMapper;

    @Resource
    VideoMapper videoMapper;

    @Resource
    UserMapper userMapper;

    //新增
    @PostMapping
    public Result<Integer> like(@RequestBody Star star){
        if(getAccount().getType().equals("user")){
            Integer uid=getAccount().getId();
            star.setUid(uid);
            star.setTime(new Date());

            LambdaQueryWrapper<Star> wrapper= Wrappers.<Star>lambdaQuery();
            wrapper.eq(Star::getUid,uid).eq(Star::getContentId,star.getContentId()).eq(Star::getType,star.getType());
            Star oldRecord=starMapper.selectOne(wrapper);


            //若已经存在该记录,取消点赞
            if(oldRecord!=null) {
                starMapper.deleteById(oldRecord.getId());
                //更新点赞数
                if(star.getType().equals("blog")) {
                    Blog b=blogMapper.selectById(star.getContentId());
                    b.setStar(b.getStar()-1);
                    blogMapper.updateById(b);
                }
                else {
                    Video v=videoMapper.selectById(star.getContentId());
                    v.setStar((v.getStar()-1));
                    videoMapper.updateById(v);
                }
                return Result.success(-1);
            }

            //否则新增
            else{
                starMapper.insert(star);
                //记录点赞数
                if(star.getType().equals("blog")) {
                    Blog b=blogMapper.selectById(star.getContentId());
                    b.setStar(b.getStar()+1);
                    blogMapper.updateById(b);
                }
                else {
                    Video v=videoMapper.selectById(star.getContentId());
                    v.setStar((v.getStar()+1));
                    videoMapper.updateById(v);
                }
                return Result.success(1);
            }
        }
        else return Result.error("401","请先登录");
    }


    //查询是否收藏
    @GetMapping
    public Result<Integer> queryIsLike(@RequestParam String type,@RequestParam Integer cid)
    {
        if(getAccount()==null){
            return Result.success(-1);
        }
        //查询登录用户记录
        Integer uid=getAccount().getId();
        LambdaQueryWrapper<Star> wrapper= Wrappers.<Star>lambdaQuery();
        wrapper.eq(Star::getUid,uid).eq(Star::getType,type).eq(Star::getContentId,cid);
        if(starMapper.selectOne(wrapper)!=null){
            return Result.success(1);
        }
        else return Result.success(-1);
    }

    @GetMapping("/getList/blog")
    public Result<?> getStaredBlogList(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                                 @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                                 @RequestParam(defaultValue = "") String keyWords, //搜索关建字
                                 @RequestParam(defaultValue = "title") String searchPattern, //根据..搜索
                                 @RequestParam(defaultValue = "0") Integer categoryId,
                                 @RequestParam(defaultValue = "0") Integer locationId){
        //查询收藏列表
        if(getAccount()!=null&&getAccount().getType().equals("user")){
            Integer uid=getAccount().getId();
            Page<Star> starPage=starMapper.findBlogPage(new Page<>(pageNum,pageSize),uid,searchPattern,keyWords,categoryId,locationId);

            return Result.success(starPage);
        }
        return Result.error("401","未获取登录用户");
    }

    @GetMapping("/getList/video")
    public Result<?> getStaredVideoList(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                                 @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                                 @RequestParam(defaultValue = "") String keyWords, //搜索关建字
                                 @RequestParam(defaultValue = "title") String searchPattern, //根据..搜索
                                 @RequestParam(defaultValue = "0") Integer categoryId,
                                 @RequestParam(defaultValue = "0") Integer locationId){
        //查询收藏列表
        if(getAccount()!=null&&getAccount().getType().equals("user")){
            Integer uid=getAccount().getId();
            Page<Star> starPage=starMapper.findVideoPage(new Page<>(pageNum,pageSize),uid,searchPattern,keyWords,categoryId,locationId);
            return Result.success(starPage);
        }
        return Result.error("401","未获取登录用户");
    }
}
