package com.job.dispatchService.lineManager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Line;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.mapper.FlowMapper;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {


    @Autowired
    public FlowProcessRelationService relationService;
    @Autowired
    public LineService lineService;

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private ProductionManagementClient productionManagementClient;

    @Autowired
    private FlowProcessRelationService flowProcessRelationService;
    @Autowired
    private RedisCache redisCache;
    //逻辑删除1未删除0已删除
    private static int IS_DELETE_NO=1;
    private static int IS_DELETE_YES=0;


    public Result flowPage(FlowPageReq req) {
        LambdaQueryWrapper<Flow> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Flow::getIsDelete,IS_DELETE_NO);
        IPage result = page(req,queryWrapper);
        return Result.success(result,"查询成功");
    }

    public Result flowList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("idDelete",IS_DELETE_NO);
        List<Flow> list = list(queryWrapper);
        return Result.success(list,"查询成功");
    }

    @Override
    public Result flowSave(Flow flow, HttpServletRequest request) {
        String token= request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
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
        boolean save = save(flow);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }

    @Override
    public Result FlowRemove(String flowId) {
        LambdaQueryWrapper<Line> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getLineFlowId,flowId);
        long count = lineService.count(queryWrapper);
        if(count>0){
            return Result.error("删除失败，请先删除对应的流水线");
        }else {
            LambdaUpdateWrapper<Flow> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.set(Flow::getIsDelete,IS_DELETE_YES);
            updateWrapper.eq(Flow::getId,flowId);
            boolean update = update(updateWrapper);
            LambdaUpdateWrapper<FlowProcessRelation> lambdaUpdateWrapper =new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(FlowProcessRelation::getFlowId,flowId).set(FlowProcessRelation::getIsDelete,0);
            boolean update1 = relationService.update(lambdaUpdateWrapper);
            if (update&&update1){
                return Result.success(null,"删除成功");
            }
            return Result.error("删除失败");
        }
    }

    @Override
    public Result batchDeleteById(List<String> idList) {
        // 获取需要逻辑删除的记录的ID列表

        Vector<Flow> recordList = new Vector<>();
        Vector<FlowProcessRelation> flowProcessRelationList = new Vector<>();

        for (String id : idList) {
            Flow flow=new Flow();
            flow.setId(id);
            flow.setIsDelete(0);  // 设置要更新的字段和值
            recordList.add(flow);

            FlowProcessRelation flowProcessRelation = new FlowProcessRelation();
            flowProcessRelation.setFlowId(id);
            flowProcessRelation.setIsDelete(0);
            flowProcessRelationList.add(flowProcessRelation);
        }

        boolean b = updateBatchById(recordList);
        boolean b1 = flowProcessRelationService.updateBatchById(flowProcessRelationList);

        if(b&&b1){
            return Result.success(null,"查询成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result flowQueryByid(String flowId) {
        if(flowId.isEmpty()){
            return Result.error("id传输为空，请重新");
        }
        Flow flow = getById(flowId);
        if(flow==null){
            return Result.error("查询为空，请重试");
        }
        return Result.success(flow,"查询成功");
    }

    @Override
    public Result likeSearch(String searchName, int size, int current) {
        FlowPageReq req=new FlowPageReq();
        req.setCurrent(current);
        req.setSize(size);
        if(StringUtil.isNullOrEmpty(searchName)){
            FlowPageReq page = page(req);
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

        FlowPageReq page = page(req,queryWrapper);
        return Result.success(page,"查询成功");
    }

    @Override
    public Result queryFlowType() {
        LambdaQueryWrapper<Flow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Flow::getIsDelete,1);
        List<Flow> list = list(queryWrapper);
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

    @Override
    public Result flowProcessByFlowId(String flowId) {
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowProcessRelation::getFlowId,flowId);
        List<FlowProcessRelation> list = relationService.list(queryWrapper);
        if(BeanUtil.isEmpty(list)&&list.size()==0){
            return Result.error("查询失败");
        }
        return Result.success(list,"查询成功");
    }

    @Override
    public Result updateFlowById(Flow flow, HttpServletRequest request) {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = (Users) authenticationClient.showdetail(userId).getData();
            String name = users.getName();
            //System.out.println(userId);
            flow.setUpdateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        flow.setUpdateTime(DateUtil.date());
        boolean b = updateById(flow);
        if (b){
            return Result.success(null,"修改成功");
        }else {
            return Result.error("修改失败");
        }
    }

    @Override
    public Result queryProduceName(HttpServletRequest request) {
        String token = request.getHeader("token");
        Set<String> queryProduceName = productionManagementClient.queryProduceName(token);
        if(queryProduceName.size() == 0) {
            return Result.error("材料查询失败");
        }

        LambdaQueryWrapper<Flow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Flow::getIsDelete,1);

        List<Flow> flowList = list(queryWrapper);
        Set<String> stringSet = flowList.stream().map(Flow::getFlow).collect(Collectors.toSet());

        Set<String> produceNames = new HashSet<>(queryProduceName);
        produceNames.removeAll(stringSet);

        return Result.success(produceNames,"查询成功");
    }


}