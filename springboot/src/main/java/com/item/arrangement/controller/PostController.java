package com.item.arrangement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Post;
import com.item.arrangement.entity.User;
import com.item.arrangement.mapper.PostMapper;
import com.item.arrangement.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 讨论贴接口
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController{

    @Resource
    PostMapper postMapper;

    @Resource
    UserMapper userMapper;

    //新增博客
    @PostMapping
    public Result<?> save(@RequestBody Post post){
        if(getAccount().getType().equals("user")){
            post.setAuthorId(getAccount().getId());
        }
        post.setTime(new Date()); //设置创建时间
        postMapper.insert(post);
        return Result.success();
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Post post){
        postMapper.updateById(post);
        return Result.success();
    }

    //删除帖子
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Integer id){
        Post res = postMapper.selectById(id);
        if(res!=null){
            if(getAccount().getType().equals("user")&&res.getAuthorId()!=getAccount().getId()){
                return Result.error("401","无权限");
            }
            else {
                postMapper.deleteById(id);
                return Result.success();
            }
        }
        return Result.error("402","内容不存在");
    }

    //根据id获取帖子内容
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id){
        Post p=postMapper.selectById(id);
        User rootAuthor=userMapper.selectById(p.getAuthorId());
        p.setAuthorName(rootAuthor.getNickName());
        p.setAuthorAvatar(rootAuthor.getAvatarSrc());

        if(p!=null){
            LambdaQueryWrapper<Post> wrapper= Wrappers.<Post>lambdaQuery();
            wrapper.eq(Post::getParentId,id);

            //取出属于该根节点的所有帖子
            List<Post> postList=postMapper.selectList(wrapper);
            for(Post post : postList){
                User author=userMapper.selectById(post.getAuthorId());
                post.setAuthorName(author.getNickName());
                post.setAuthorAvatar(author.getAvatarSrc());
            }
            p.setChildren(postList);
            return Result.success(p);
        }
        return Result.error("404","内容不存在");
    }

    private List<Post> loopQuery(Integer pid, List<Post> allPosts){
        List<Post> children=new ArrayList<>();
        for(Post post : allPosts){
            if(post.getParentId()==pid){
                children.add(post);
                post.setChildren(loopQuery(post.getId(),allPosts));
            }
        }
        return children;
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords, //搜索关建字
                              @RequestParam(defaultValue = "title") String searchPattern //根据..搜索
    ){
        Page<Post> postPage= postMapper.findPage(new Page<>(pageNum,pageSize),searchPattern,keyWords);
        return Result.success(postPage);
    }
}
