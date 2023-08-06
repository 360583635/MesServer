package com.job.dispatchService.lineManager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Line;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.mapper.LineMapper;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.request.LinePageReq;
import com.job.dispatchService.lineManager.service.LineService;

import com.job.feign.clients.AuthenticationClient;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.job.dispatchService.lineManager.controller.LineTaskController.findThreadByName;


@Service
@Slf4j
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private RedisCache redisCache;

    //逻辑删除1未删除0已删除
    private static int IS_DELETE_NO=1;
    private static int IS_DELETE_YES=0;

    @Override
    public Result linePage(LinePageReq req) {
        LambdaQueryWrapper<Line> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getIsDelete,IS_DELETE_NO);
        IPage result = page(req,queryWrapper);
        return Result.success(result,"查询成功");
    }

    @Override
    public Result likeSearch(String searchName, int size, int current) {
        LinePageReq req = new LinePageReq();
        req.setCurrent(current);
        req.setSize(size);
        if(StringUtil.isNullOrEmpty(searchName)){
            LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Line::getIsDelete,1);
            LinePageReq page = page(req,queryWrapper);
            return Result.success(page,"成功");
        }
        boolean matches = searchName.matches("-?\\d+(\\.\\d+)?");
        LambdaQueryWrapper<Line> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getIsDelete,IS_DELETE_NO);
        if(matches){
            queryWrapper
                    .eq(Line::getIsDelete,IS_DELETE_NO)
                    .eq(Line::getId,searchName);
        }else {
            queryWrapper
                    .eq(Line::getIsDelete,IS_DELETE_NO)
                    .like(Line::getLine, searchName);
        }
        LinePageReq page = page(req, queryWrapper);
        return Result.success(page,"搜索查询成功");
    }

    @Override
    public Result saveLine(Line pipeLine, HttpServletRequest request) {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            pipeLine.setUpdateUsername(name);
            pipeLine.setCreateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getLine,pipeLine.getLine());
        long count = count(queryWrapper);
        if(count>0){
            return Result.error("流水线实体名称不能重复，请重新添加");
        }
        pipeLine.setCreateTime(DateUtil.date());
        pipeLine.setUpdateTime(DateUtil.date());
        pipeLine.setOrderCount(0);
        pipeLine.setLineStatus("0"); //设置状态为空闲
        pipeLine.setIsDelete(1);
        boolean b = save(pipeLine);
        //ToDo 调用日志接口
        if(b){
            return Result.success(null,"添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result updateLine(Line pipeLine, HttpServletRequest request) {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            pipeLine.setUpdateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        pipeLine.setUpdateTime(DateUtil.date());
        boolean b = updateById(pipeLine);
        if(b){
            //ToDo 调用日志接口
            return Result.success(null,"修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result removeLine(String lineId) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,lineId);
        Line byId = getOne(queryWrapper);
        if(!"1".equals(byId.getLineStatus())){
            return Result.error("流水线未关闭，请先关闭流水线");
        }
        byId.setIsDelete(0);
        boolean b = updateById(byId);
        if(b){
            return Result.success(null,"删除成功");
        }else{
            return Result.error("删除失败");
        }
    }

    @Override
    public Result batchRemoveById(List<String> idList) {
        List<Line> lines = listByIds(idList);
        boolean hasStatusOne = lines.stream().anyMatch(line -> line.getLineStatus().equals("0"));

        if (hasStatusOne) {
            return Result.error("请先保证流水线状态为关闭");
        } else {
            // 列表中不存在status为1的Line对象
            System.out.println("列表中不存在status为1的Line对象");
            List<Line> lineList = new ArrayList<>();
            for (String id : idList) {
                Line line = new Line();
                line.setId(id);
                line.setIsDelete(0);  // 设置要更新的字段和值
                lineList.add(line);
            }

            boolean b = updateBatchById(lineList);

            if (b) {
                return Result.success(null, "查询成功");
            }
            return Result.error("删除失败");

        }
    }

    @Override
    public Result lineList() {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getIsDelete,1);
        List<Line> list = list(queryWrapper);
        return Result.success(list,"查询成功");
    }

    @Override
    public Result selectLineById(String id) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,id);
        Line line = getOne(queryWrapper);
        if(line==null){
            return Result.error("查询失败");
        }
        return Result.success(line,"查询成功");
    }

    @Override
    public Result selectLineByFlowId(String flowId, int size, int current) {
        FlowPageReq req=new FlowPageReq();
        req.setCurrent(current);
        req.setSize(size);
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getLineFlowId,flowId);
        FlowPageReq page = page(req, queryWrapper);
        return Result.success(page,"查询成功");
    }

    @Override
    public Result haltLine(String id) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,id);
        Line line = getOne(queryWrapper);
        if(line==null){
            return Result.error("该流水线不存在");
        }
        line.setLineStatus("1");
        String lineName = line.getLine();
        String lineId = line.getId();
        Thread threadByName = findThreadByName(lineName+lineId);
        if(threadByName!=null){
            threadByName.interrupt();
        }
        return Result.success(null,"该流水线已关闭");
    }

    @Override
    public Result updateStatus(String id) {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getId,id);
        Line line = getOne(queryWrapper);
        if(line==null){
            return Result.error("该流水线不存在");
        }
        String flag = line.getLineStatus();
        log.info(flag);
        if("0".equals(flag)){
            line.setLineStatus("1");
            updateById(line);
        }else if("1".equals(flag)){
            line.setLineStatus("0");
            updateById(line);
        }else{
            return Result.error("流水线状态不处于空闲，不能关闭");
        }
        if(flag.equals(line.getLineStatus())){
            return Result.error("流水线状态修改失败");
        }
        return Result.success(null,"流水线状态修改成功");
    }


}