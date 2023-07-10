package com.job.dispatchService.LineManager.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.job.common.pojo.Line;
import com.job.common.result.Result;
import com.job.dispatchService.LineManager.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:06
 * @description
 */

@RestController
@RequestMapping("/dispatch/line")
public class LineController {

    @Autowired
    private LineService lineService;

    /**
     * 添加流水线
     * @param pipeLine
     * @return
     */
    @RequestMapping("/saveLine")
    @ResponseBody
    public Result saveLine(@RequestBody Line pipeLine){

        String user="wen"; //获取用户信息
        pipeLine.setOrderCount("0");
        pipeLine.setStatus("0"); //设置状态为空闲
        lineService.save(pipeLine);
        //ToDo 调用日志接口
        return Result.success(null,"添加成功");
    }

    /**
     * 修改流水线
     * @param pipeLine
     * @return
     */
    @RequestMapping("/updateLine")
    @ResponseBody
    public Result updateLine(@RequestBody Line pipeLine){
        UpdateWrapper updateWrapper=new UpdateWrapper();
        String user="wen";//获取当前用户信息
        lineService.updateById(pipeLine);
        //ToDo 调用日志接口
        return Result.success(null,"修改成功");
    }

    /**
     * 删除流水线
     * @param lineId
     * @return
     */
    @RequestMapping("/removeLine")
    @ResponseBody
    public Result removeLine(String lineId){
        Line byId = lineService.getById(lineId);
        if(!"0".equals(byId.getStatus())){
            return Result.error("流水线未关闭，无法删除");
        }
        lineService.removeById(lineId);
        return Result.success(null,"删除成功");
    }







}
