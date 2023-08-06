package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Line;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.feign.clients.AuthenticationClient;
import com.job.feign.clients.ProductionManagementClient;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    /*@Autowired
    public FlowProcessRelationService relationService;*/
   /* @Autowired
    public LineService lineService;*/
    /**
     * 流程信息信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @PostMapping("/page")
    public Result flowPage(@RequestBody FlowPageReq req){
        return flowService.flowPage(req);
    }


    /**
     * 流程全部信息查询
     *
     * @return Result 执行结果
     */
    @GetMapping("/list")
    public Result flowList(){
        return flowService.flowList();
    }


    /**
     * 添加流程
     * @param flow
     * @param request
     * @return
     */
    @PostMapping("/save")
    public Result flowSave(@RequestBody Flow flow, HttpServletRequest request){

//        String userId= UserUtil.getUserId(httpServletRequest);
        return flowService.flowSave(flow,request);
    }

    /**
     * 根据id删除
     * @param flowId
     * @return
     */
    @PostMapping("removeByid")
    public Result FlowRemove(@RequestParam String flowId){
        return flowService.FlowRemove(flowId);
    }

    /**
     * 批量逻辑删除
     * @param idList
     * @return
     */
    @PostMapping("/batchDelete")
    public Result batchDeleteById(@RequestParam List<String> idList){
        return flowService.batchDeleteById(idList);
    }



    /**
     * 根据id查询
     * @param flowId
     * @return
     */
    @PostMapping("/queryId")
    public Result flowQueryByid(@RequestParam(value = "flowId") String flowId){
        return flowService.flowQueryByid(flowId);
    }

    /**
     * 模糊查询，根据id和名字
     * @param searchName
     * @return
     */
    @PostMapping("/likeSearch")
    public Result likeSearch(@RequestParam String searchName,@RequestParam int size,@RequestParam int current ){
        return flowService.likeSearch(searchName,size,current);
    }

    /**
     * 查询所有流程类型
     * @return
     */

    @GetMapping("/queryFlowType")
    public Result queryFlowType(){
        return flowService.queryFlowType();
    }


    @PostMapping("/flowProcessByFlowId")
    public Result flowProcessByFlowId(@RequestParam String flowId){
        return flowService.flowProcessByFlowId(flowId);
    }

    /**
     * 修改流程通过id
     * @param flow
     * @return
     */
    @PostMapping("/updateFlowById")
    public Result updateFlowById(@RequestBody Flow flow,HttpServletRequest request){
        return flowService.updateFlowById(flow,request);
    }

    @GetMapping("/queryProduceName")
    public Result queryProduceName(HttpServletRequest request){
        return flowService.queryProduceName(request);
    }


}
