package com.job.dispatchService.LineManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.dispatchService.LineManager.request.FlowPageReq;
import com.job.dispatchService.LineManager.service.FlowService;
import com.job.dispatchService.common.Result;
import com.job.dispatchService.LineManager.pojo.TFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:04
 * @description
 */
@Controller
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
    public Result page(FlowPageReq req){
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
        List<TFlow> list = flowService.list(queryWrapper);
        return Result.success(list);
    }
}
