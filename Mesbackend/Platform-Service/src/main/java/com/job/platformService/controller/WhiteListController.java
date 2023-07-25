package com.job.platformService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.common.pojo.Users;
import com.job.common.utils.JwtUtil;
import com.job.platformService.result.Result;
import com.job.platformService.service.UsersService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/platform")
public class WhiteListController {
    @Autowired
    private UsersService usersService;
    //白名单状态值
   private static String WHITESTATE= "1";
   //黑名单状态值
   private static String BLACKSTATE="0";

    /**
     * 展示黑白名单用户
     * @param option
     * @return
     */

    @RequestMapping("/show")
    public Result show(@RequestParam(value = "option") String option){
        List<Users> usersList=query(option);
        System.out.println(usersList);
        //前端读取list,去除list对象中的name(展示)
        return Result.success(usersList,"展示成功");
    }

    public List<Users> query(String state){
        LambdaQueryWrapper<Users> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Users::getIsBlack,state);
        List<Users> list=usersService.list(wrapper);
        return list;
    }

    /**
     * 点击添加按钮、跳转到增加的展示界面
     * @param option
     * @return
     */
    @RequestMapping("/add/show")
    public Result addshow(@RequestParam(value = "option") String option){
        if (option.equals(WHITESTATE)){
           List<Users> BlackList=query(BLACKSTATE);
            System.out.println(BlackList);
            return Result.success(BlackList,"展示黑名单用户");
        }else {
            List<Users> WhiteList=query(WHITESTATE);
            System.out.println(WhiteList);
            return Result.success(WhiteList,"展示白名单用户");
        }
    }

    /**
     * 通过传入的选中list 修改Isblack
     * @param IDS
     * @return
     */

    @RequestMapping("/add")
    //传入id值
    public Result add (@RequestParam(value = "IDS") List<Integer>IDS, HttpServletRequest request)
    {
        //获取token
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
        Users user=usersService.getById(userid);
        Date date=new Date();

        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        for (int ID : IDS) {
            //System.out.println(ID);
            Users users=usersService.getById(ID);
            if (users.getIsBlack().equals(WHITESTATE)){
                users.setIsBlack(Integer.valueOf(BLACKSTATE));
                users.setUpdateUser(user.getName());
                users.setUpdateTime(date);
                //usersService.save(users);
                usersService.update(users,updateWrapper);
            }else {
                users.setIsBlack(Integer.valueOf(WHITESTATE));
                usersService.update(users,updateWrapper);
                users.setUpdateUser(user.getName());
                users.setUpdateTime(date);
              //  usersService.save(users);
            }
        }
        return Result.success(null,"添加成功");
    }

    /**
     * 删除黑白名单，即修改isBlack
     * @param idS
     * @return
     */
    @RequestMapping("/del")
    public Result del(@RequestParam(value ="idS") List<Integer> idS,HttpServletRequest request){
        //获取token
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
        Users user=usersService.getById(userid);
        Date date=new Date();

        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        for (int ID : idS) {
            //System.out.println(ID);
            Users users=usersService.getById(ID);
            if (users.getIsBlack().equals(WHITESTATE)){
                users.setIsBlack(Integer.valueOf(BLACKSTATE));
                users.setUpdateUser(user.getName());
                users.setUpdateTime(date);
                //usersService.save(users);
                usersService.update(users,updateWrapper);
            }else {
                users.setIsBlack(Integer.valueOf(WHITESTATE));
                users.setUpdateUser(user.getName());
                users.setUpdateTime(date);
                usersService.update(users,updateWrapper);
                //  usersService.save(users);
            }
        }
        return Result.success(null,"删除成功");
    }

}
