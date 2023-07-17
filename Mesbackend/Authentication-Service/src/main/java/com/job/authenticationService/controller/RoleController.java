package com.job.authenticationService.controller;

import com.job.authenticationService.pojo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.service.MenusRolesService;
import com.job.authenticationService.service.RolesService;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/*
对角色进行增删改查
 */
//@RequestMapping("/roles")
@RestController
public class RoleController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private MenusRolesService menusRolesService;
    @Autowired
    private RolesMapper rolesMapper;


//    查询所有可用用户角色

    /**
     * 查询所有可用用户角色
     * @return
     */
    @RequestMapping("/getRoles")
    public Result getAllRoles(){
        LambdaQueryWrapper<Roles> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Roles::getIsDelete,"1");
        List<Roles> list= rolesService.list(wrapper);
        System.out.println(list);
        Result result = new Result<>();
        result.setData(list);
        result.setCode(200);
// 循环输出查询结果
        for (Roles entity : list) {
            System.out.println(entity);
        }
        return result;
    }

    /**
     * 删除某个用户角色
     * @param RoleId
     * @return
     */
    @RequestMapping("/delRole/{RoleId}")
    public Result<Roles> deleteRole(@PathVariable("RoleId") String RoleId){
        Roles roles=rolesService.getById(RoleId);
        roles.setIsDelete(0);
        rolesService.updateById(roles);
        Roles role=rolesService.getById(RoleId);
        System.out.println(role);
        Result result = new Result<>();
        if (role.getIsDelete().equals(0)){
            result.setData("删除成功");
            result.setCode(200);
        }
        else {
            result.setData("删除失败");
            result.setCode(405);
        }
        return result;
    }

//    添加某个用户角色

    /**
     * 添加某个用户角色
     * @param role_name
     * @param options
     * @return
     */
    @RequestMapping("/addRole")
    public Result<Roles> addRole(@RequestParam(value = "role_name") String role_name,
                                 @RequestParam(value = "option") List<String> options){
        Date date=new Date();
        Roles roles=new Roles();
        roles.setRoleName(role_name);
        roles.setCreateTime(date);
        roles.setUpdateTime(date);
        roles.setCreateUser("zyx");
        roles.setUpdateUser("zyx");
        rolesService.save(roles);
        String roleId = roles.getRoleId();
        System.out.println(roleId);
        System.out.println(role_name);
        System.out.println(options);
        for(String s:options){
            System.out.println(s);
            MenusRoles menusRoles=new MenusRoles();
            menusRoles.setRoleId(roleId);
            menusRoles.setCreateTime(date);
            menusRoles.setCreateTime(date);
            menusRoles.setUpdateUser("zyx");
            menusRoles.setCreateUser("zyx");
            menusRoles.setMenuId(s);
            menusRolesService.save(menusRoles);
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }



    //    修改某个用户角色

    /**
     * 修改某个用户角色
     * @param role_name
     * @param role_id
     * @param options
     * @return
     */
    @RequestMapping("/updateRole")
    public Result<Roles> updateRole(@RequestParam(value = "role_name") String role_name,
                                    @RequestParam(value = "role_id") String role_id,
                                 @RequestParam(value = "option") List<String> options){
//        修改角色名
        Date date=new Date();
        Roles roles=rolesService.getById(role_id);
        roles.setRoleName(role_name);
        roles.setUpdateTime(date);
        roles.setUpdateUser("zyx");
        UpdateWrapper<Roles> updateWrapper = new UpdateWrapper<>();
        rolesService.update(roles,updateWrapper);

//        修改角色权限表
        System.out.println(role_id);
        System.out.println(options);
        menusRolesService.remove(new QueryWrapper<MenusRoles>().eq("role_id","1"));
        for(String s:options){
            System.out.println(s);
            MenusRoles menusRoles=new MenusRoles();
            menusRoles.setRoleId(role_id);
            menusRoles.setCreateTime(date);
            menusRoles.setCreateTime(date);
            menusRoles.setUpdateUser("zyx");
            menusRoles.setCreateUser("zyx");
            menusRoles.setMenuId(s);
            menusRolesService.save(menusRoles);
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }





}
