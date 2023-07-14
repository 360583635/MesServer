package com.job.platformService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.common.pojo.Roles;
import com.job.common.pojo.Users;
import com.job.platformService.mapper.UsersMapper;
import com.job.platformService.pojo.User;
import com.job.platformService.result.Result;
import com.job.platformService.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/platform")
public class WhiteListController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersMapper usersMapper;
    //白名单状态值
   private static Integer WHITESTATE=1;
   //黑名单状态值
   private static Integer BLACKSTATE=0;

    /**
     * 展示黑白名单用户
     * @param option
     * @return
     */

    @RequestMapping("/show")
    public Result show(@RequestParam(value = "option") String option){
        List<Users> usersList=query(Integer.valueOf(option));
        System.out.println(usersList);
        //前端读取list,去除list对象中的name(展示)
        return Result.success(usersList,"展示成功");
    }

    public List<Users> query(Integer state){
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
//0是黑名单

    @RequestMapping("/add")
    //传入id值
    public Result add (@RequestParam(value = "IDS") List<Integer>IDS)
    {
        for (int ID : IDS) {
            //System.out.println(ID);
            Users users=usersService.getById(ID);
            if (users.getIsBlack().equals(WHITESTATE)){
                users.setIsBlack(Integer.valueOf(BLACKSTATE));
                usersMapper.updateById(users);
            }
            else if (users.getIsBlack().equals(BLACKSTATE)) {
                users.setIsBlack(Integer.valueOf(WHITESTATE));
                usersMapper.updateById(users);
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
    public Result del(@RequestParam(value ="idS") List<Integer> idS){
       // UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        for (int ID : idS) {
            //System.out.println(ID);
            Users users=usersService.getById(ID);
            System.out.println(users.getIsBlack().equals(WHITESTATE));
            if (users.getIsBlack().equals(WHITESTATE)){
                users.setIsBlack(0);
                usersMapper.updateById(users);
            }else {
                users.setIsBlack(1);
                usersMapper.updateById(users);
            }
        }
        return Result.success(null,"删除成功");
    }

}
