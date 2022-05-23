package com.item.arrangement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.item.arrangement.common.Result;
import com.item.arrangement.entity.Category;
import com.item.arrangement.mapper.CategoryMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 视频接口
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController{

    @Resource
    CategoryMapper categoryMapper;

    //新增
    @PostMapping
    public Result<?> save(@RequestBody Category category){
        if(getAccount().getType().equals("admin")){
            categoryMapper.insert(category);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Category category){
        if(getAccount().getType().equals("admin")){
            categoryMapper.updateById(category);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //删除
    @DeleteMapping("/{id}") //占位符 /{aa}/{bb} ==> (@PathVariable aa的类型 aa,@PathVariable bb的类型 bb)
    public Result<?> delete(@PathVariable Long id){
        if(getAccount().getType().equals("admin")){
            categoryMapper.deleteById(id);
            return Result.success();
        }
        return Result.error("401","需要管理员权限");
    }

    //查询
    @GetMapping
    public Result<?> getCategoryList(){
        LambdaQueryWrapper<Category> wrapper= Wrappers.<Category>lambdaQuery();
        List<Category> categories=categoryMapper.selectList(wrapper);
        return Result.success(categories);
    }

    //根据id获取
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id){
        Category c=categoryMapper.selectById(id);
        if(c!=null) {
            return Result.success(c);
        }
        return Result.error("404","查询id为"+id+"的类别名称时发生错误");
    }
}
