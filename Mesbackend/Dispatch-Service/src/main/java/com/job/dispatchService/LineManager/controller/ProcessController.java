package com.job.dispatchService.LineManager.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
//import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;
import com.job.dispatchService.LineManager.pojo.TProcess;
import com.job.dispatchService.LineManager.service.FlowProcessRelationService;
import com.job.dispatchService.LineManager.service.ProcessService;
import com.job.dispatchService.common.Result;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 修改工序
     * @param tProcess
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result updateProcess(@RequestBody Process tProcess){
        //获得用户信息
        String userinf="温帅";
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
        String userinf="温帅";
        tProcess.setUpdateUsername(userinf);
        tProcess.setCreateUsername(userinf);
        DateTime nowTime = DateUtil.date();
        tProcess.setUpdateTime(nowTime);
        processService.saveOrUpdate(tProcess);
        return Result.success();
    }

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




}
