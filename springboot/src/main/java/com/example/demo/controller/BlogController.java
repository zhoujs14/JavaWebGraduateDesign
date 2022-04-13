package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Blog;
import com.example.demo.mapper.BlogMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 博客接口
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    BlogMapper blogMapper;

    //新增博客
    @PostMapping
    public Result<?> save(@RequestBody Blog blog){
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
    public Result<?> delete(@PathVariable Long id){
        blogMapper.deleteById(id);
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字
        LambdaQueryWrapper<Blog> wrapper= Wrappers.<Blog>lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(Blog::getTitle,keyWords); //输入不为空才使用like模糊查询
        Page<Blog> blogPage= blogMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(blogPage);
    }
}
