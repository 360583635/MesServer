package com.job.authenticationService.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.service.MenusRolesService;
import com.job.authenticationService.service.RolesService;
import com.job.common.pojo.Menus;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;
import com.job.authenticationService.pojo.Result;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
对角色进行增删改查
 */
@RequestMapping("/authen")
@RestController
public class RoleController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private MenusRolesService menusRolesService;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private MenusMapper menusMapper;


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
        com.job.authenticationService.pojo.Result result = new com.job.authenticationService.pojo.Result<>();
        result.setData(list);
        result.setCode(200);
// 循环输出查询结果
        for (Roles entity : list) {
            System.out.println(entity);
        }
        return result;
    }

    /**
     * 查询单个角色信息
     * @param
     * @return
     */
    @RequestMapping("/showRoleById/{RoleId}")
    public Result joinQueryExample(@PathVariable("RoleId") String id) {
        System.out.println(id);
        List list = new ArrayList<>();
        Result<Object> result = new Result<>();
        LambdaQueryWrapper<Roles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Roles::getRoleId, Roles::getRoleName) // 指定需要查询的字段
                .eq(Roles::getRoleId, id); // 添加其他查询条件
        List<Map<String, Object>> resultList = rolesMapper.selectMaps(queryWrapper);
        System.out.println(resultList);
        list.add(resultList);
        LambdaQueryWrapper<Menus> wrapper=new LambdaQueryWrapper<>();
        wrapper.select(Menus::getMenuID, Menus::getName) // 指定需要查询的字段
                .eq(Menus::getIs_delete,1);
        List<Map<String, Object>> rolesList=menusMapper.selectMaps(wrapper);
        list.add(rolesList);
        System.out.println(rolesList);
        result.setData(list);
        result.setCode(200);
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
     * 添加某个用户角色1
     * @param role_name
     * @param options
     * @return
     */
    @RequestMapping("/addRole")
    public Result<Roles> addRole(@RequestParam(value = "role_name") String role_name,
                                 @RequestParam(value = "option",required = false) List<String> options){
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
        if (options!=null) {
            for (String s : options) {
                System.out.println(s);
                MenusRoles menusRoles = new MenusRoles();
                menusRoles.setRoleId(roleId);
                menusRoles.setCreateTime(date);
                menusRoles.setCreateTime(date);
                menusRoles.setUpdateUser("zyx");
                menusRoles.setCreateUser("zyx");
                menusRoles.setMenuId(s);
                menusRolesService.save(menusRoles);
            }
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }



    //    修改某个用户角色

    /**
     * 修改某个用户角色1
     * @param role_name
     * @param role_id
     * @param options
     * @return
     */
    @RequestMapping("/updateRole")
    public Result<Roles> updateRole(@RequestParam(value = "role_name") String role_name,
                                    @RequestParam(value = "role_id") String role_id,
                                 @RequestParam(value = "option",required = false) List<String> options){
//        修改角色名
        Date date=new Date();
        Roles roles=rolesService.getById(role_id);
        roles.setRoleName(role_name);
        roles.setUpdateTime(date);
        roles.setUpdateUser("zyx");
        rolesService.updateById(roles);

//        修改角色权限表
        System.out.println(role_id);
        System.out.println(options);
        menusRolesService.remove(new QueryWrapper<MenusRoles>().eq("role_id",role_id));
        if (options!=null) {
            for (String s : options) {
                System.out.println(s);
                MenusRoles menusRoles = new MenusRoles();
                menusRoles.setRoleId(role_id);
                menusRoles.setCreateTime(date);
                menusRoles.setCreateTime(date);
                menusRoles.setUpdateUser("zyx");
                menusRoles.setCreateUser("zyx");
                menusRoles.setMenuId(s);
                menusRolesService.save(menusRoles);
            }
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }





}
