package com.item.arrangement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Video;
import com.item.arrangement.mapper.CategoryMapper;
import com.item.arrangement.mapper.LocationMapper;
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

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    LocationMapper locationMapper;

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

    //删除视频
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){

        Video res=videoMapper.selectById(id);
        if(getAccount().getType().equals("user")&&res.getAuthorId()!=getAccount().getId()) return Result.error("401","无权限");
        else {
            videoMapper.deleteById(id);
            FileController.removeOldAvatar(res.getCover());
            FileController.removeOldAvatar(res.getSrc());
            return Result.success();
        }
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords, //搜索关建字
                              @RequestParam(defaultValue = "title") String searchPattern, //根据..搜索
                              @RequestParam(defaultValue = "0") Integer categoryId,
                              @RequestParam(defaultValue = "0") Integer locationId){
        Page<Video> videoPage= videoMapper.findPage(new Page<>(pageNum,pageSize),searchPattern,keyWords,categoryId,locationId);

        return Result.success(videoPage);
    }

    //根据id获取博客内容
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id){
        Video v=videoMapper.selectById(id);
        if(v!=null) {
            if(v.getCateId()!=null&&v.getCateId()!=0) v.setCateName(categoryMapper.selectById(v.getCateId()).getName());
            if(v.getLocationId()!=null&&v.getLocationId()!=0) v.setLocationName(locationMapper.selectById(v.getLocationId()).getName());
            return Result.success(v);
        }
        return Result.error("404","视频不见了");
    }
}
