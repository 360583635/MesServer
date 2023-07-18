package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.Flow;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:04
 * @description
 */
@RestController
@RequestMapping("/dispatch/flow")
public class FlowController {
    @Autowired
    public FlowService flowService;

    /**
     * 流程信息信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @PostMapping("/page")
    @ResponseBody
    public Result page(@RequestBody FlowPageReq req){

        IPage result = flowService.page(req);
        return Result.success(result);
    }


    /**
     * 流程全部信息查询
     *
     * @return Result 执行结果
     */
    @GetMapping("/list")
    @ResponseBody
    public Result list(){
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Flow> list = flowService.list(queryWrapper);
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result flowSave(@RequestBody Flow flow){
        flow.setCreateTime(DateUtil.date());
        flow.setUpdateTime(DateUtil.date());
        flow.setUpdateUsername("张三");
        flow.setCreateUsername("张三");
        boolean save = flowService.save(flow);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");

    }


}
