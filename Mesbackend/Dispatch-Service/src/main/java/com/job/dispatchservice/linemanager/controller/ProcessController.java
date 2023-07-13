package com.job.dispatchService.linemanager.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
//import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;
//import com.job.dispatchService.LineManager.pojo.TProcess;
import com.job.dispatchservice.linemanager.request.ProcessPageReq;
import com.job.dispatchservice.linemanager.service.FlowProcessRelationService;
import com.job.dispatchservice.linemanager.service.ProcessService;
import com.job.dispatchservice.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:05
 * @description
 */
@RestController
@RequestMapping("/dispatch/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private FlowProcessRelationService processRelationService;

    /**
     * 工序分页查询
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Result page(ProcessPageReq req){
        IPage result = processService.page(req);
        return Result.success(result,"查询成功");
    }

    /**
     * 修改工序
     * @param tProcess
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result updateProcess(@RequestBody Process tProcess){
        //获得用户信息
        String userinf="郭帅比";
        tProcess.setUpdateUsername(userinf);
        DateTime nowTime = DateUtil.date();
        tProcess.setUpdateTime(nowTime);
        processService.updateById(tProcess);
        return Result.success();

    }

    /**
     * 增加工序
     * @param tProcess
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result saveProcess(@RequestBody Process tProcess){
        //获得用户信息
        String userinf="郭帅比";
        tProcess.setUpdateUsername(userinf);
        tProcess.setCreateUsername(userinf);
        DateTime nowTime = DateUtil.date();
        tProcess.setUpdateTime(nowTime);
        processService.saveOrUpdate(tProcess);
        return Result.success();
    }

    /**
     * 删除工序
     * @param processId
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result removePeocess(String processId){
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(FlowProcessRelation::getProcessId,processId);
        long count = processRelationService.count(queryWrapper);
//        List<TFlowProcessRelation> list = processRelationService.list(queryWrapper);
        if(count>0){
            return Result.failure("请先删除与本工序有关的流程");
        }
        boolean b = processService.removeById(processId);
        if(b){
            return Result.success();
        }
        return Result.failure("操作失败，请刷新页面重试");
    }

    @PostMapping("/query/{procrssName}")
    @ResponseBody
    public Result<ProcessPageReq> query(@PathVariable("process") String procrssName,@RequestBody ProcessPageReq req){
        LambdaQueryWrapper<Process> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Process::getProcess,procrssName);
        ProcessPageReq page = processService.page(req, lambdaQueryWrapper);
        return Result.success(page,"查询成功");
    }
    /**
     * 查询全部工序
     */
    @GetMapping("/list")
    public Result list(){
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
        List<Process> list = processService.list(queryWrapper);
        return Result.success(list,"查询成功");
    }

}
