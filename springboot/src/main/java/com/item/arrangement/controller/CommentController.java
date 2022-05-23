package com.item.arrangement.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Comment;
import com.item.arrangement.entity.User;
import com.item.arrangement.mapper.CommentMapper;
import com.item.arrangement.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController{

    @Resource
    CommentMapper commentMapper;

    @Resource
    UserMapper userMapper;

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
    public Result<?> delete(@PathVariable Integer id){
        Comment res=commentMapper.selectById(id);
        if(getAccount().getType().equals("user")){
            if(getAccount().getId()!=res.getAuthorId()){
                return Result.error("402","只能删除自己发布的评论");
            }
            List<Comment> commentList=loopQuery(id,commentMapper.selectList(new LambdaQueryWrapper<Comment>()));
            loopDelete(commentList);
            commentMapper.deleteById(id);
            return Result.success();
        }
        else if(getAccount().getType().equals("admin")){
            List<Comment> commentList=loopQuery(id,commentMapper.selectList(new LambdaQueryWrapper<Comment>()));
            loopDelete(commentList);
            commentMapper.deleteById(id);
            return Result.success();
        }
        else return Result.error("402","账号类型错误");

    }

    //递归删除子评论
    private void loopDelete(List<Comment> commentList){
        for(Comment c:commentList){
            if(c.getChildren().size()>0){
                loopDelete(c.getChildren());
            }
            commentMapper.deleteById(c.getId());
        }
    }

    //分页查询
    @GetMapping("/admin")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字
        //查询对应id内容的全部评论
        LambdaQueryWrapper<Comment> wrapper=Wrappers.lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(Comment::getAuthorId,keyWords); //输入不为空才使用like模糊查询
        Page<Comment> commentPage=commentMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        return Result.success(commentPage);
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "blog") String type,
                              @RequestParam Integer rootId){
        //查询对应id内容的全部评论
        LambdaQueryWrapper<Comment> wrapper= Wrappers.<Comment>lambdaQuery();
        wrapper.eq(Comment::getType,type).eq(Comment::getRootId,rootId);
        List<Comment> commentList=commentMapper.selectList(wrapper);

        //添加评论人信息
        for(Comment comment:commentList){
            User author=userMapper.selectById(comment.getAuthorId());
            comment.setAuthorName(author.getNickName());
            comment.setAvatarSrc(author.getAvatarSrc());
        }
        //得到结果列表
        List<Comment> resList=loopQuery(null,commentList);

        //转为page
        Page<Comment> page=new Page<>(pageNum,pageSize);
        int start=(int)((page.getCurrent()-1)*page.getSize());
        int end = (int)((start + page.getSize()) > resList.size() ? resList.size() : (page.getSize() * page.getCurrent()));
        page.setRecords(resList.subList(start,end));
        page.setTotal(resList.size());

        return Result.success(page);
    }

    //递归查询子评论
    private List<Comment> loopQuery(Integer pid,List<Comment> allComments){
        List<Comment> commentList=new ArrayList<>();
        for(Comment comment:allComments){
            if(pid==null){
                if(comment.getParentId()==null){
                    commentList.add(comment);
                    comment.setChildren(loopQuery(comment.getId(),allComments));
                }
            } else if(pid==comment.getParentId()){
                commentList.add(comment);
                comment.setChildren(loopQuery(comment.getId(),allComments));
            }
        }
        return commentList;
    }
}
