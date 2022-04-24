package com.item.arrangement.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Blog;
import com.item.arrangement.mapper.BlogMapper;
import com.item.arrangement.mapper.CategoryMapper;
import com.item.arrangement.mapper.LocationMapper;
import com.item.arrangement.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 博客接口
 */
@RestController
@RequestMapping("/blog")
public class BlogController extends BaseController{

    @Resource
    BlogMapper blogMapper;

    @Resource
    LocationMapper locationMapper;

    @Resource
    CategoryMapper categoryMapper;

    //新增博客
    @PostMapping
    public Result<?> save(@RequestBody Blog blog){
        if(getAccount().getType().equals("user")){
            blog.setAuthorId(getAccount().getId());
        }
        blog.setTime(new Date()); //设置创建时间
        blogMapper.insert(blog);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Blog blog){
        blogMapper.updateById(blog);
        return Result.success();
    }

    //删除博客
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Integer id){
        blogMapper.deleteById(id);
        return Result.success();
    }

    //根据id获取博客内容
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id){
        Blog b=blogMapper.selectById(id);
        if(b!=null) {
            if(b.getCateId()!=null) b.setCateName(categoryMapper.selectById(b.getCateId()).getName());
            if(b.getLocationId()!=null) b.setLocationName(locationMapper.selectById(b.getLocationId()).getName());
            return Result.success(b);
        }
        return Result.error("404","文章不见了");
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字
        LambdaQueryWrapper<Blog> wrapper= Wrappers.<Blog>lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(Blog::getTitle,keyWords); //输入不为空才使用like模糊查询
        Page<Blog> blogPage= blogMapper.findPage(new Page<>(pageNum,pageSize));
        return Result.success(blogPage);
    }
}
