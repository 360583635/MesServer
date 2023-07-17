package com.job.authenticationService.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.authenticationService.mapper.UsersMapper;
import com.job.authenticationService.service.UsersRolesService;
import com.job.authenticationService.service.UsersService;
import com.job.authenticationService.utils.JwtUtil;
import com.job.common.pojo.MenusRoles;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;
import com.job.common.pojo.UsersRoles;
import com.job.common.result.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

//@RequestMapping("/authen")
@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRolesService usersRolesService;

    @Autowired
    private UsersMapper usersMapper;

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
    public Result<Roles> deleteRole(@PathVariable("UserId") String UserId, HttpServletRequest request){
        String token=request.getHeader("token");
     //   System.out.println("token"+token);
      //  System.out.println(UserId);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
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
     * @param UserId
     * @return
     */
    @RequestMapping("/showdetail/{Userid}")
    public Result showdetail(@PathVariable("UserId") String UserId){
        Users users=usersService.getById(UserId);
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
     * @param password
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
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "phone") String phone,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "sex") String sex,
                                @RequestParam(value = "address") String address,
                                @RequestParam(value = "birth") String birth){
        Users users=usersService.getById(id);
        users.setName(name);
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encode=passwordEncoder.encode(password);
        users.setPassword(encode);
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
    /*
    User user = UserMapper.seleteOne("条件参数")

User newUser = new User();

newUser.setId(user.getId());

newUser.setUserName("张三");

UserMapper.updateById(newUser);

User user = new User();
user.setUserId(1);
user.setAge(29);
 userMapper.updateById(user);
     */


}
