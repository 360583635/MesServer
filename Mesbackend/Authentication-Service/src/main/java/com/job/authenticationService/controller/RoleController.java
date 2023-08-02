package com.job.authenticationService.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.mapper.MenusRolesMapper;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.service.MenusRolesService;
import com.job.authenticationService.service.RolesService;
import com.job.authenticationService.service.UsersService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.common.pojo.Menus;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;
import com.job.authenticationService.pojo.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.catalina.User;
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
//@CrossOrigin
public class RoleController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private MenusRolesService menusRolesService;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private MenusMapper menusMapper;
    @Autowired
    private MenusRolesMapper menusRolesMapper;
    @Autowired
    private UsersService usersService;


//    查询所有可用用户角色

    /**
     * 查询所有可用用户角色
     * @return
     */
    @RequestMapping("/getRoles")
    public Result getAllRoles(){
//        String token=request.getParameter("Authorization");
//        String userid;
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userid = claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("token非法");
//        }
//        //获取修改人信息
//        Users users=usersService.getById(userid);

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
    @RequestMapping("/showRoleById")
    public Result joinQueryExample(@RequestParam(value = "RoleId") String id) {
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
    @RequestMapping("/delRole")
    public Result<Roles> deleteRole(@RequestParam("RoleId") String RoleId,HttpServletRequest request){
        System.out.println(RoleId);
        String token=request.getParameter("Authorization");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //获取修改人信息
        Users users=usersService.getById(userid);

        Roles roles=rolesService.getById(RoleId);
        roles.setIsDelete(0);
        roles.setUpdateUser(users.getName());
        Date date=new Date();
        roles.setUpdateTime(date);
        rolesService.updateById(roles);
        Roles role=rolesService.getById(RoleId);


//        删除权限表
        LambdaQueryWrapper<MenusRoles> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(MenusRoles::getRoleId,RoleId);
        List<MenusRoles> list= menusRolesService.list(wrapper);
        System.out.println(list);

        if (!list.isEmpty()){
            for (MenusRoles menusRoles : list) {
                String menuId = menusRoles.getMenuId();
                String roleId = menusRoles.getRoleId();
                menusRolesService.remove(new QueryWrapper<MenusRoles>().eq("menu_id",menuId)
                        .eq("role_id",roleId));
            }}


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
    public Result<Roles> addRole(@RequestParam(value = "name") String role_name,
                                 @RequestParam(value = "option",required = false) List<String> options,HttpServletRequest request){

        System.out.println(role_name);
        System.out.println(options.size());
        String token=request.getParameter("Authorization");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //获取添加人信息
        Users users=usersService.getById(userid);

        Date date=new Date();
        Roles roles=new Roles();
        roles.setRoleName(role_name);
        roles.setCreateTime(date);
        roles.setUpdateTime(date);
        roles.setCreateUser(users.getName());
        roles.setIsDelete(1);
        roles.setUpdateUser(users.getName());
        rolesService.save(roles);
        String roleId = roles.getRoleId();
        System.out.println(roleId);
        System.out.println(role_name);
        System.out.println(options);
        if (options.size()!=0) {
            options.remove(options.size()-1);
            for (String s : options) {
                System.out.println(s);
                MenusRoles menusRoles = new MenusRoles();
                menusRoles.setRoleId(roleId);
                menusRoles.setCreateTime(date);
                menusRoles.setUpdateTime(date);
                menusRoles.setUpdateUser(users.getName());
                menusRoles.setCreateUser(users.getName());
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
    public Result<Roles> updateRole(@RequestParam(value = "name") String role_name,
                                    @RequestParam(value = "role_id") String role_id,
                                    @RequestParam(value = "option",required = false) List<String> options,HttpServletRequest request){

        System.out.println(role_name);
        System.out.println(role_id);
        System.out.println(options);
        String token=request.getParameter("Authorization");
        System.out.println(token);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //获取修改人信息
        Users users=usersService.getById(userid);


//        修改角色名
        Date date=new Date();
        Roles roles=rolesService.getById(role_id);
        roles.setRoleName(role_name);
        roles.setUpdateTime(date);
        roles.setUpdateUser(users.getName());
        rolesService.updateById(roles);

//        修改角色权限表
        System.out.println(role_id);
        System.out.println(options);
        menusRolesService.remove(new QueryWrapper<MenusRoles>().eq("role_id",role_id));
        if (options.size()!=0) {
            options.remove(options.size()-1);
            for (String s : options) {
                System.out.println(s);
                MenusRoles menusRoles = new MenusRoles();
                menusRoles.setRoleId(role_id);
                menusRoles.setCreateTime(date);
                menusRoles.setUpdateTime(date);
                menusRoles.setUpdateUser(users.getName());
                menusRoles.setCreateUser(users.getName());
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
