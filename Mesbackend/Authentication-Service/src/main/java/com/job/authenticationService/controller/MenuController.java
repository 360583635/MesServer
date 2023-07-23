package com.job.authenticationService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.authenticationService.mapper.MenusMapper;
import com.job.authenticationService.pojo.LoginUser;
import com.job.authenticationService.pojo.ResponseResult;
import com.job.authenticationService.pojo.Result;
import com.job.authenticationService.service.MenusService;
import com.job.authenticationService.service.UsersService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.common.pojo.Menus;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/authen/menus")
public class MenuController {

    @Autowired
    private MenusService menusService;
    @Autowired
    private MenusMapper menusMapper;
    @Autowired
    private UsersService usersService;



    /**
     * 添加权限
     * @param menuname
     * @param url
     * @param type
     * @param parentid
     * @param permission
     * @param remark
     * @param request
     * @return
     */
    @RequestMapping("/add")
    public Result addMenus(@RequestParam(value = "menuname") String menuname,
                           @RequestParam(value = "url") String url,
                           @RequestParam(value = "type") int type,
                           @RequestParam(value = "parentid") String parentid,
                           @RequestParam(value = "permission") String permission,
                           @RequestParam(value = "remark") String remark, HttpServletRequest request) {
        //  String token = request.getHeader("token");
        String token=request.getParameter("Authorization");
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //得到Users修改人信息
        Users users = usersService.getById(userid);

        Menus menus = new Menus();
        menus.setName(menuname);
        menus.setUrl(url);
        menus.setType(type);
        menus.setParentId(parentid);
        menus.setPermission(permission);
        menus.setRemark(remark);
        Date date = new Date();
        menus.setCreateTime(date);
        menus.setCreateUser(users.getName());
        menus.setUpdateUser(users.getName());
        menus.setUpdateTime(date);
        //menus.setSort();
        menusService.save(menus);
        return Result.success(null,"添加成功");
    }




    /**
     * 删除权限
     * @param menusid
     * @return
     */
    @RequestMapping("/delete")
    public Result deleteMenus(@RequestParam("menusid") String menusid,HttpServletRequest request) {
        String token=request.getParameter("Authorization");
        //String token=request.getParameter("Authorization");
        System.out.println("controller:"+token);
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
        Menus menus=menusService.getById(menusid);
        menus.setIs_delete(0);
        menus.setUpdateUser(users.getName());
        Date date=new Date();
        menus.setUpdateTime(date);
        int i=menusMapper.updateById(menus);
        System.out.println(i);
        if (i>0){
            return Result.success(null,"删除成功");
        }else {
            return Result.error("删除失败");
        }

    }


    /**
     * 修改权限
     * @param menusid
     * @param menuname
     * @param url
     * @param type
     * @param parentid
     * @param permission
     * @param remark
     * @param request
     * @return
     */
    @RequestMapping("/update")
    public Result updateMenus(@RequestParam(value = "menusid") String menusid,
                              @RequestParam(value = "menuname") String menuname,
                              @RequestParam(value = "url") String url,
                              @RequestParam(value = "type") int type,
                              @RequestParam(value = "parentid") String parentid,
                              @RequestParam(value = "permission") String permission,
                              @RequestParam(value = "remark") String remark, HttpServletRequest request) {

        //String token = request.getHeader("token");
        String token=request.getParameter("Authorization");
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        String  userid = loginUser.getUser().getId();
        //得到Users修改人信息
        Users users = usersService.getById(userid);
        Menus menus=menusService.getById(menusid);
        menus.setName(menuname);
        menus.setUrl(url);
        menus.setType(type);
        menus.setParentId(parentid);
        menus.setPermission(permission);
        menus.setRemark(remark);
        Date date = new Date();
        menus.setUpdateUser(users.getName());
        menus.setUpdateTime(date);
        int i=menusMapper.updateById(menus);
        if (i>0){
            return  Result.success(null,"修改成功");
        }else {
            return Result.error("修改失败");
        }
    }


    /**
     * 展示已有权限
     * @return
     */
    @RequestMapping("/showmenus")
    public Result showmenus(){
//        String s= GetUserid.getID();
//        //System.out.println("10001");
//        System.out.println(s);
        LambdaQueryWrapper<Menus> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Menus::getIs_delete,1);
        List<Menus> menuslist= menusService.list(wrapper);
        // System.out.println(menuslist);
        return Result.success(menuslist,"展示成功");
        //return Result.success(list,"查询成功");
//        if (list.size()>0){
//            return Result.success(list,"查询成功");
//        }else {
//            return Result.error("查询失败");
//        }
    }

    /**
     * 提供修改权限界面
     * @param menusid
     * @return
     */
    @RequestMapping("/update/show")
    public  Result updateshow(@RequestParam(value = "menusid") String menusid){
        Menus menus=menusService.getById(menusid);
        if (menus!=null){
            return Result.success(menus,"展示成功");
        }else {
            return Result.error("展示失败");
        }
    }

}
