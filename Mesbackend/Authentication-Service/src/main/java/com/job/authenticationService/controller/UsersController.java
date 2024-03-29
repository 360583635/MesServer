package com.job.authenticationService.controller;

import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.mapper.RolesMapper;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.pojo.Result;
import com.job.authenticationService.service.MenusService;
import com.job.authenticationService.service.RolesService;
import com.job.authenticationService.service.UsersRolesService;
import com.job.authenticationService.service.UsersService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.common.pojo.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.date.DateUtil;
import java.util.*;
//@CrossOrigin
@RequestMapping("/authen")
@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRolesService usersRolesService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private MenusService menusService;

    @Autowired
    private RolesMapper rolesMapper;


    /**
     * 查询角色
     * @return
     */
    @RequestMapping("/showrole")
    public  Result showrole(){
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
        wrapper.eq(Roles::getIsDelete,1);
        List<Roles> roleslist=rolesService.list(wrapper);
        System.out.println(roleslist);
        if (roleslist.size()>0){
            return Result.success(roleslist,"查询角色成功");
        }else {
            return Result.error("查询失败");
        }

    }

    /**
     * 查询所有用户
     */
    @RequestMapping("/showUser")
    public Result showUser(){
        LambdaQueryWrapper<Users> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Users::getIsDelete,1);
        List<Users> usersList=usersService.list(wrapper);
        System.out.println(usersList);
        if (usersList.size()>0){
            return Result.success(usersList,"查询用户成功");
        }else {
            return Result.error("查询失败");
        }
    }


    /**
     * 查询个人信息1
     * @param
     * @return
     */
    @RequestMapping("/showById")
    public Result joinQueryExample(@RequestParam(value = "id") String id) {
        System.out.println(88+id);
        List list = new ArrayList<>();
        Result<Object> result = new Result<>();
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Users::getState, Users::getName, Users::getId) // 指定需要查询的字段
                .eq(Users::getId, id); // 添加其
        // 他查询条件
        List<Map<String, Object>> resultList = usersMapper.selectMaps(queryWrapper);
        list.add(resultList);
        System.out.println(resultList);
        LambdaQueryWrapper<Roles> wrapper=new LambdaQueryWrapper<>();
        wrapper.select(Roles::getRoleId, Roles::getRoleName) // 指定需要查询的字段
                .eq(Roles::getIsDelete,1);
        List<Map<String, Object>> rolesList=rolesMapper.selectMaps(wrapper);
        list.add(rolesList);
        System.out.println(rolesList);
        result.setData(list);
        result.setCode(200);
        return result;
    }

    /**
     * 查询所有权限
     */
    @RequestMapping("/showmenus")
    public Result showmenus(){
        LambdaQueryWrapper<Menus> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Menus::getIs_delete,1);
        List<Menus> menusList=menusService.list(wrapper);
        System.out.println(menusList);
        if (menusList.size()>0){
            return Result.success(menusList,"查询权限成功");
        }else {
            return Result.error("查询失败");
        }
    }

    /**
     * 注册管理员账号
     * @param name
     * @param password
     * @param phone
     * @param email
     * @param sex
     * @param address
     * @return
     */
    @RequestMapping("/regist")
    public Result<Users> regist(@RequestParam(value = "name") String name,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "phone") String phone,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "sex") String sex,
                                @RequestParam(value = "address") String address
                                /*@RequestParam(value = "birth") String birth*/){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        Users users=new Users();
        users.setCreateTime(DateUtil.date());
        users.setUpdateTime(DateUtil.date());
        /*users.setCreateUser(users1.getName());
        users.setUpdateUser(users1.getName());*/
        users.setIsDelete(1);
        String encode=passwordEncoder.encode(password);
        users.setPassword(encode);
        users.setName(name);
        users.setPhone(phone);
        users.setEmail(email);
//        users.setBirth(birth);
        users.setAddress(address);
        users.setSex(sex);
        boolean saveUsers = usersService.save(users);
        String id = null;
        if(saveUsers){
            id = users.getId();
        }else{
            return Result.error("用户注册失败");
        }
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserId(id);
        usersRoles.setUpdateTime(DateUtil.date());
        usersRoles.setCreateTime(DateUtil.date());
                /*usersRoles.setUpdateUser(users1.getName());
                usersRoles.setCreateUser(users1.getName());*/
        usersRoles.setRoleId("1");
        boolean saveRoles = usersRolesService.save(usersRoles);
        if(saveRoles){
            return Result.success(null,"注册成功");
        }
        return Result.error("权限添加失败");

    }

    /**
     * 添加员工用户
     * @param name
     * @param password
     * @param state
     * @param options
     * @return
     */
    @RequestMapping("/addUser")
    public Result<Users> addUser(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "state") Integer state,
                                 @RequestParam(value = "option",required = false) List<String> options,
                                 HttpServletRequest request){
//        System.out.println(options);
//        System.out.println(options.subList(0,-1));

        /*String token=request.getParameter("Authorization");
        System.out.println(111);
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
        Users users1=usersService.getById(userid);*/



        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        Date date=new Date();
        Users users=new Users();
        /*users.setCreateUser(users1.getName());
        users.setUpdateUser(users1.getName());*/
        users.setIsDelete(1);
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

        if (options.size()!=0) {
            options.remove(options.size()-1);
            for (String s : options) {
                UsersRoles usersRoles = new UsersRoles();
                usersRoles.setUserId(id);
                usersRoles.setUpdateTime(date);
                usersRoles.setCreateTime(date);
                /*usersRoles.setUpdateUser(users1.getName());
                usersRoles.setCreateUser(users1.getName());*/
                usersRoles.setRoleId(s);
                usersRolesService.save(usersRoles);
            }
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;

    }

    /**
     * 修改用户信息1
     * @param id
     * @param state
     * @param options
     * @return
     */

    @RequestMapping("/updateUser")
    public Result addUser(@RequestParam(value = "id") String id,
                          @RequestParam(value = "state") Integer state,
                          @RequestParam(value = "option",required = false) List<String> options,
                          HttpServletRequest request) {

        System.out.println(id);
        System.out.println(state);
        System.out.println(options);

        String token=request.getParameter("Authorization");
        System.out.println(111);
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


//                修改状态
        Date date = new Date();
        Users user = usersService.getById(id);
        System.out.println(user);
        user.setState(state);
        user.setUpdateTime(date);
        user.setUpdateUser(users.getName());
        usersService.updateById(user);


        //        修改角色表
        System.out.println(id);
        System.out.println(options);
        usersRolesService.remove(new QueryWrapper<UsersRoles>().eq("user_id", id));
        if (options.size()!=0) {
            options.remove(options.size()-1);
            for (String s : options) {
                System.out.println(s);
                UsersRoles usersRoles = new UsersRoles();
                usersRoles.setUserId(id);
                usersRoles.setCreateTime(date);
                usersRoles.setUpdateTime(date);
                usersRoles.setUpdateUser(users.getName());
                usersRoles.setCreateUser(users.getName());
                usersRoles.setRoleId(s);
                usersRolesService.save(usersRoles);
            }
        }
        Result result=new Result();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;


    }

    /**
     * 删除某个用户
     * @param UserId
     * @param request
     * @return
     */
    //    删除某个用户
    @RequestMapping("/delUser")
    public Result<Roles> deleteRole(@RequestParam("UserId") String UserId, HttpServletRequest request){
        System.out.println("11111");
        String token=request.getHeader("token");
        String cont=request.getHeader( "content-length");
        String token1 = request.getParameter("Authorization");
        System.out.println(token1);
        System.out.println(cont);
        //   System.out.println("token"+token);
        //  System.out.println(UserId);
        System.out.println("controller:"+token1);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token1);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        System.out.println(token1);
        //要删除的用户
        Users user=usersService.getById(UserId);
        //获取登录人信息
        Users users=usersService.getById(userid);
        //System.out.println(user);
        //设置修改人
        user.setUpdateUser(users.getName());
        Date date=new Date();
        user.setUpdateTime(date);
        user.setIsDelete(0);
        usersMapper.updateById(user);




//        删除角色表
        LambdaQueryWrapper<UsersRoles> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UsersRoles::getUserId,UserId);
        List<UsersRoles> list= usersRolesService.list(wrapper);
        System.out.println(list);
        if (!list.isEmpty()){
            for (UsersRoles usersRoles : list) {
                String userId = usersRoles.getUserId();
                String roleId = usersRoles.getRoleId();
                usersRolesService.remove(new QueryWrapper<UsersRoles>().eq("user_id",userId)
                        .eq("role_id",roleId));
            }}

        // Users users=usersService.getById(UserId);
        //System.out.println(users);
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

    /**
     * 展示个人用户信息
     * @param
     * @return
     */
  //  @PreAuthorize("hasAuthority('showdetail')")
    @RequestMapping("/showdetail")
    public Result showdetail(HttpServletRequest request){

        //String token=request.getParameter("Authorization");
        String token=request.getHeader("Authorization");
        System.out.println("进入showdetail"+token);
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        Users users=usersService.getById(userid);

        if (users!=null){
            return Result.success(users,"用户返回成功");
        }else {
            return Result.error("该用户不存在");
        }
    }

    /**
     * 修改个人信息
     * @param id
     * @param name
  //   * @param password
     * @param phone
     * @param email
     * @param sex
     * @param address
     * @param birth
     * @return
     */

    @RequestMapping("/update/detail")
    public  Result updateDetail(@RequestParam(value = "id") String id,
                                @RequestParam(value = "name") String name,
                              //  @RequestParam(value = "password") String password,
                                @RequestParam(value = "phone") String phone,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "sex") String sex,
                                @RequestParam(value = "address") String address,
                                @RequestParam(value = "birth") String birth){
        Users users=usersService.getById(id);
        users.setName(name);
//        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
//        String encode=passwordEncoder.encode(password);
//        users.setPassword(encode);
        users.setPhone(phone);
        users.setEmail(email);
        users.setAddress(address);
        users.setSex(sex);
        // Date births=(Date)birth;
        users.setBirth(birth);
        Date date=new Date();
        users.setUpdateTime(date);
        users.setUpdateUser(name);
        System.out.println("111");
        usersMapper.updateById(users);
//        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
//        usersService.update(users, updateWrapper);
        return Result.success(null,"修改成功");

    }
    @RequestMapping("/updatepassword")
    public  Result UpdatePassword(@RequestParam("password") String password,
                                  @RequestParam("password1") String password1,
                                  HttpServletRequest request){
      //  String token=request.getParameter("Authorization");
        String token=request.getHeader("Authorization");
        System.out.println("进入showdetail"+token);
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        Users users=usersService.getById(userid);
        System.out.println(password.equals(password1));
        if(password.equals(password1)){
            BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            String encode=passwordEncoder.encode(password);
            users.setPassword(encode);
            System.out.println(encode);
            int i= usersMapper.updateById(users);
            if(i>0){
                return Result.success(null,"修改成功");
            }else {
                return Result.error("修改失败");
            }
        }
        return  Result.error("修改错误");
    }



}
