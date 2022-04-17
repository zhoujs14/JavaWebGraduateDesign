package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Admin;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Resource
    AdminMapper adminMapper;

    //新增管理员
    @PostMapping
    public Result<?> save(@RequestBody Admin admin){
        Integer operatorLv=getAdmin().getLevel();
        if(operatorLv>1) return Result.error("-1","权限不足");

        //校验数据是否合法
        Result res=Admin.validate(admin);
        if(res.getCode().equals("-1")) return res;

        //校验用户名是否存在
        Admin sameUsername=adminMapper.selectOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getUsername,admin.getUsername()));
        if(sameUsername!=null) return Result.error("-1","该用户名已被注册");

        if(operatorLv==1&&admin.getLevel()<=1) return Result.error("-1","不可创建同级或更高级管理员");

        adminMapper.insert(admin);
        return Result.success();
    }

    //更新管理员
    @PutMapping
    public Result<?> update(@RequestBody Admin admin){
        Integer level=getAdmin().getLevel();
        if(level>1||level>=admin.getLevel()) return Result.error("-1","权限不足");
        adminMapper.updateById(admin);
        return Result.success();
    }

    //删除管理员
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        Integer level=getAdmin().getLevel();
        Admin res=adminMapper.selectById(id);
        if(res==null) return Result.error("-1","无该管理员");
        if(level>1||level>=res.getLevel()) return Result.error("-1","权限不足");
        adminMapper.deleteById(id);
        return Result.success();
    }

    //查询管理员
    @GetMapping("/{id}")
    public Result<Admin> selectById(@PathVariable Integer id){
        if(getAdmin().getLevel()>2) return Result.error("-1","权限不足");
        Admin a=adminMapper.selectById(id);
        a.setPassword(null);
        return Result.success(a);
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, //页码
                              @RequestParam(defaultValue = "10") Integer pageSize, //项目条数
                              @RequestParam(defaultValue = "") String keyWords){ //搜索关建字

        if(getAdmin().getLevel()>2) return Result.error("-1","权限不足");

        LambdaQueryWrapper<Admin> wrapper= Wrappers.<Admin>lambdaQuery();
        if(StrUtil.isNotBlank(keyWords)) wrapper.like(Admin::getUsername,keyWords); //输入不为空才使用like模糊查询
        Page<Admin> adminPage= adminMapper.selectPage(new Page<>(pageNum,pageSize), wrapper.select(
                Admin::getId,Admin::getUsername,Admin::getLevel
        ));
        return Result.success(adminPage);
    }

    //登录
    @PostMapping("/login")
    public Result<Admin> login(@RequestBody Admin admin){
        Admin res=adminMapper.selectOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername,admin.getUsername())
                .eq(Admin::getPassword,admin.getPassword()));

        if(res==null) return Result.error("-1","用户名或密码错误");
        res.setType("admin");
        String token= TokenUtils.genToken(res);
        res.setToken(token);
        res.setPassword(null);
        return Result.success(res);
    }

    @GetMapping("/getLevel")
    public Result<Integer> getLevel(){
        Admin res=getAdmin();
        if(res!=null&&res.getLevel()!=null) return Result.success(res.getLevel());
        else return Result.error("401","未获取到权限等级");
    }
}
