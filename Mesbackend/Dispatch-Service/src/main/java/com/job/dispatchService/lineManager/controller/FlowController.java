package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.*;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.request.ProcessPageReq;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.feign.clients.AuthenticationClient;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:04
 * @description
 */
@RestController
@RequestMapping("/dispatch/flow")
@CrossOrigin
public class FlowController {
    @Autowired
    public FlowService flowService;

    @Autowired
    public FlowProcessRelationService relationService;
    @Autowired
    public LineService lineService;

    @Autowired
    private AuthenticationClient authenticationClient;
    //逻辑删除1未删除0已删除
    private static int IS_DELETE_NO=1;
    private static int IS_DELETE_YES=0;
    /**
     * 流程信息信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @PostMapping("/page")
    @ResponseBody
    public Result page(FlowPageReq req){
        LambdaQueryWrapper<Flow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Flow::getIsDelete,IS_DELETE_NO);
        IPage result = flowService.page(req,queryWrapper);
        return Result.success(result,"查询成功");
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
        queryWrapper.eq("idDelete",IS_DELETE_NO);
        List<Flow> list = flowService.list(queryWrapper);
        return Result.success(list,"查询成功");
    }


    @PostMapping("/save")
    public Result flowSave(@RequestBody Flow flow, HttpServletRequest request){

//        String userId= UserUtil.getUserId(httpServletRequest);
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = (Users) authenticationClient.showdetail(userId).getData();
            String name = users.getName();
            //System.out.println(userId);
            flow.setUpdateUsername(name);
            flow.setCreateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        flow.setCreateTime(DateUtil.date());
        flow.setUpdateTime(DateUtil.date());
        boolean save = flowService.save(flow);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");

    }

    /**
     * 根据id删除
     * @param flowId
     * @return
     */
    @PostMapping("removeByid")
    public Result FlowRemove(@RequestParam String flowId){
        LambdaQueryWrapper<Line> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getLineFlowId,flowId);
        long count = lineService.count(queryWrapper);
        if(count>0){
           return Result.error("删除失败，请先删除对应的流水线");
        }else {
            LambdaUpdateWrapper<Flow> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.set(Flow::getIsDelete,IS_DELETE_YES);
            updateWrapper.eq(Flow::getId,flowId);
            boolean update = flowService.update(updateWrapper);
            if (update){
                return Result.success(null,"删除成功");
            }
            return Result.error("删除失败");
        }
    }

    /**
     * 批量逻辑删除
     * @param idList
     * @return
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public Result batchDeleteById(@RequestParam List<String> idList){

        // 获取需要逻辑删除的记录的ID列表

        List<Flow> recordList = new ArrayList<>();
        for (String id : idList) {
            Flow flow=new Flow();
            flow.setId(id);
            flow.setIsDelete(0);  // 设置要更新的字段和值
            recordList.add(flow);
        }

        boolean b = flowService.updateBatchById(recordList);

        if(b){
            return Result.success(null,"查询成功");
        }
        return Result.error("删除失败");


    }



    /**
     * 根据id查询
     * @param flowId
     * @return
     */
    @PostMapping("/queryId")
    public Result flowQueryByid(@RequestParam(value = "flowId") String flowId){
        if(flowId.isEmpty()){
            return Result.error("id传输为空，请重新");
        }
        Flow flow = flowService.getById(flowId);
        if(flow==null){
        return Result.error("查询为空，请重试");
        }
        return Result.success(flow,"查询成功");
    }

    /**
     * 模糊查询，根据id和名字
     * @param searchName
     * @return
     */
    @PostMapping("/likeSearch")
    public Result likeSearch(@RequestParam String searchName,@RequestParam int size,@RequestParam int current ){
        FlowPageReq req=new FlowPageReq();
        req.setCurrent(current);
        req.setSize(size);
        if(searchName.isEmpty()){
            FlowPageReq page = flowService.page(req);
            return Result.success(page,"成功");
        }
        boolean matches = searchName.matches("-?\\d+(\\.\\d+)?");
        LambdaQueryWrapper<Flow> queryWrapper=new LambdaQueryWrapper<>();
        if(matches){
            queryWrapper
                    .eq(Flow::getIsDelete, 1)
                    .eq(Flow::getId,searchName);
        }else {
            queryWrapper
                    .eq(Flow::getIsDelete, 1)
                    .like(Flow::getFlow, searchName);
        }

        FlowPageReq page = flowService.page(req,queryWrapper);
        return Result.success(page,"查询成功");


    }

    /**
     * 查询所有流程类型
     * @return
     */

    @GetMapping("/queryFlowType")
    public Result queryFlowType(){
        LambdaQueryWrapper<Flow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Flow::getIsDelete,1);
        List<Flow> list = flowService.list(queryWrapper);
        Map<String,String> functionNames = new HashMap<>();
        for (Flow flow : list) {
            String functionId = flow.getId();
            String functionName = flow.getFlow();
            functionNames.put(functionId,functionName);
        }
        if(functionNames==null){
            return Result.error("流水线所属流程类型查询失败");
        }
        return Result.success(functionNames,"查询成功");
    }


}
