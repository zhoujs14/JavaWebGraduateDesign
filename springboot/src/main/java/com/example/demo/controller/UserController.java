package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    UserMapper userMapper;

    //新增用户
    @PostMapping
    public Result<?> save(@RequestBody User user){
        if(getAdmin()!=null&&getAdmin().getLevel()>2) return Result.error("402","当前用户无权限进行该操作");

        //校验注册数据合法性
        Result res = User.validate(user);
        if(res.getCode().equals("-1")) return res;

        //校验用户名是否存在
        User u=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(u!=null) return Result.error("-1","该用户名已被注册");

        //校验昵称是否存在
        u=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNickName,user.getNickName()));
        if(u!=null) return Result.error("-2","该昵称已被注册");

        userMapper.insert(user);
        return Result.success();
    }

    //更新用户
    @PutMapping
    public Result<?> update(@RequestBody User user){
        Account a=getAccount();
        if(a.getType().equals("admin")){
            if(getAdmin().getLevel()>2) return Result.error("402","当前用户权限不足");
            userMapper.updateById(user);
            return Result.success();
        }
        else if(a.getType().equals("user")&&getUser().getId()==user.getId()){
            userMapper.updateById(user);
            return Result.success();
        }
        else return Result.error("403","账号类型错误");
    }

    //删除用户
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        if(!getAccount().getType().equals("admin")||getAdmin().getLevel()>2) return Result.error("402","当前用户权限不足");
        userMapper.deleteById(id);
        return Result.success();
    }

    //查询用户
    @GetMapping("/{id}")
    public Result<User> selectById(@PathVariable Integer id){
        User u=userMapper.selectById(id);
        u.setPassword(null);
        return Result.success(u);
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字

        if(!getAccount().getType().equals("admin")||getAdmin().getLevel()>2) return Result.error("402","普通用户无权浏览后台网站");

        LambdaQueryWrapper<User> wrapper= Wrappers.<User>lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(User::getNickName,keyWords); //输入不为空才使用like模糊查询
        Page<User> userPage= userMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(userPage);
    }

    //登录
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        User res=userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername,user.getUsername())
                .eq(User::getPassword,user.getPassword()));

        if(res==null) return Result.error("-1","用户名或密码错误");
        res.setType("user");
        String token= TokenUtils.genToken(res);
        res.setToken(token);
        res.setPassword(null);
        return Result.success(res);
    }

    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        //校验注册数据合法性
        Result res = User.validate(user);
        if(res.getCode().equals("-1")) return res;

        //校验用户名是否存在
        User u=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(u!=null) return Result.error("-1","该用户名已被注册");

        //校验昵称是否存在
        u=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNickName,user.getNickName()));
        if(u!=null) return Result.error("-2","该昵称已被注册");

        userMapper.insert(user);
        return Result.success();
    }

    //保存头像
    @PostMapping("/avatar")
    public Result<?> saveAvatar(@RequestBody User user){
        if(getUser().getId()!=user.getId()) return Result.error("402","当前用户无权限进行该操作");

        User res=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res==null) return Result.error("-1","头像保存失败");

        //更新头像路径
        String oldSrc=res.getAvatarSrc();
        res.setAvatarSrc(user.getAvatarSrc());
        userMapper.updateById(user);

        //删除旧头像
        if(oldSrc!=null&&oldSrc.length()>0) FileController.removeOldAvatar(oldSrc);
        return Result.success();
    }
}
