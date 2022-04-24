package com.item.arrangement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Comment;
import com.item.arrangement.mapper.CommentMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController{

    @Resource
    CommentMapper commentMapper;

    //新增评论
    @PostMapping
    public Result<?> save(@RequestBody Comment comment){
        if(getAccount().getType().equals("user")){
            comment.setAuthorId(getAccount().getId());
            comment.setTime(new Date()); //设置创建时间
            commentMapper.insert(comment);
            return Result.success();
        }
        return Result.error("401","未获取用户");
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Comment comment){
        commentMapper.updateById(comment);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        commentMapper.deleteById(id);
        return Result.success();
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "blog") String type,
                              @RequestParam Integer parentId){
        if(type.equals("blog")){
            Page<Comment> blogPage= commentMapper.findBlogPage(new Page<>(pageNum,pageSize),parentId);
            return Result.success(blogPage);
        }
        else{
            Page<Comment> videoPage=commentMapper.findVideoPage(new Page<>(pageNum,pageSize),parentId);
            return Result.success(videoPage);
        }
    }
}
