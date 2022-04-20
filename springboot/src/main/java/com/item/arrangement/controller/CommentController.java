package com.item.arrangement.controller;

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
public class CommentController {

    @Resource
    CommentMapper commentMapper;

    //新增评论
    @PostMapping
    public Result<?> save(@RequestBody Comment comment){
        comment.setTime(new Date()); //设置创建时间
        comment.setLike(0);
        commentMapper.insert(comment);
        return Result.success();
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
}
