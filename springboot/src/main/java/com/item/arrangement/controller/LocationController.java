package com.item.arrangement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Location;
import com.item.arrangement.mapper.LocationMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 视频接口
 */
@RestController
@RequestMapping("/location")
public class LocationController extends BaseController{

    @Resource
    LocationMapper locationMapper;

    //新增
    @PostMapping
    public Result<?> save(@RequestBody Location location){
        if(getAccount().getType().equals("admin")){
            locationMapper.insert(location);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Location location){
        if(getAccount().getType().equals("admin")){
            locationMapper.updateById(location);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //删除
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        if(getAccount().getType().equals("admin")){
            locationMapper.deleteById(id);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //分页查询
    @GetMapping
    public Result<?> getLocationList(){
        LambdaQueryWrapper<Location> wrapper= Wrappers.<Location>lambdaQuery();
        List<Location> categories=locationMapper.selectList(wrapper);
        return Result.success(categories);
    }

    //根据id获取
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id){
        Location c=locationMapper.selectById(id);
        if(c!=null) {
            return Result.success(c);
        }
        return Result.error("404","查询id为"+id+"的位置名称时发生错误");
    }
}
