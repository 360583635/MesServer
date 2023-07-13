package com.job.authenticationService.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.service.UsersRolesService;
import com.job.authenticationService.service.UsersService;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;
import com.job.common.pojo.UsersRoles;
import com.job.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRolesService usersRolesService;

    @RequestMapping("/addUser")
    public Result<Users> addUser(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "state") Integer state,
                                 @RequestParam(value = "option") List<String> options){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Date date=new Date();
        Users users=new Users();
        users.setCreateUser("zyx");
        users.setUpdateUser("zyx");
        users.setState(state);
        String encode=passwordEncoder.encode(password);
        System.out.println(encode);
        users.setPassword(encode);
        users.setName(name);
        users.setCreateTime(date);
        users.setUpdateTime(date);
        usersService.save(users);
        String id=users.getId();
        System.out.println(id);

        System.out.println(options);
        for(String s:options){
            System.out.println(s);
            UsersRoles usersRoles=new UsersRoles();
            usersRoles.setUserId( id);
            usersRoles.setCreateTime(date);
            usersRoles.setUpdateTime(date);
            usersRoles.setCreateTime(date);
            usersRoles.setUpdateUser("zyx");
            usersRoles.setCreateUser("zyx");
            usersRoles.setRoleId(s);
            usersRolesService.save(usersRoles);
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }


    @RequestMapping("/updateUser")
    public Result<Users> addUser(@RequestParam(value = "id") String id,
                                 @RequestParam(value = "state") Integer state,
                                 @RequestParam(value = "option") List<String> options) {

//                修改角色名
        Date date = new Date();
        Users user = usersService.getById(id);
        user.setState(state);
        user.setUpdateTime(date);
        user.setUpdateUser("zyx");
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        usersService.update(user, updateWrapper);

        //        修改角色权限表
        System.out.println(id);
        System.out.println(options);
        usersRolesService.remove(new QueryWrapper<UsersRoles>().eq("user_id",id));
        for(String s:options){
            System.out.println(s);
            UsersRoles usersRoles=new UsersRoles();
            usersRoles.setUserId(id);
            usersRoles.setCreateTime(date);
            usersRoles.setCreateTime(date);
            usersRoles.setUpdateUser("zyx");
            usersRoles.setCreateUser("zyx");
            usersRoles.setRoleId(s);
            usersRolesService.save(usersRoles);
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;

    }

    //    删除某个用户角色
    @RequestMapping("/delUser/{UserId}")
    public Result<Roles> deleteRole(@PathVariable("UserId") String UserId){
        System.out.println(UserId);
        Users user=usersService.getById(UserId);
        System.out.println(user);
        user.setIsDelete(1);
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        usersService.update(user,updateWrapper);
        Users users=usersService.getById(UserId);
        System.out.println(users);
        Result result = new Result<>();
        if (users.getIsDelete().equals(1)){
            result.setData("删除成功");
            result.setCode(200);
        }
        else {
            result.setData("删除失败");
            result.setCode(405);
        }
        return result;
    }

}
