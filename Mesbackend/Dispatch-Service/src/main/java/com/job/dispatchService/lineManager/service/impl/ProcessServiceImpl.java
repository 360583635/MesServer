package com.job.dispatchService.lineManager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.controller.ProcessMaterialRelationController;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.dto.ProcessUpdateDto;
import com.job.dispatchService.lineManager.mapper.ProcessMapper;
import com.job.dispatchService.lineManager.request.ProcessPageReq;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.EquipmentVo;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.ProductionManagementClient;
import com.job.feign.pojo.Equipment;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProcessMaterialRelationController processMaterialRelationController;
    @Autowired
    private ProcessMaterialRelationService processMaterialRelationService;
    @Autowired
    private FlowProcessRelationService processRelationService;
    @Autowired
    private ProductionManagementClient productionManagementClient;

    @Override
    public Result updateProcess(ProcessDto processDto, HttpServletRequest request) {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            processDto.setUpdateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        processDto.setUpdateTime(DateUtil.date());
        Process process = new Process();
        BeanUtils.copyProperties(processDto,process);

        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getProcess, processDto.getProcess());
        boolean b = updateById(process);
        if(b==true) {
            Process process1 =getOne(queryWrapper);
            processDto.setId(process1.getId());
            Result result = null;
            try {
                result = processMaterialRelationController.addOrUpdate(processDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(result.getCode()==1){
                return Result.success(null,"修改成功");
            }
        }
        return Result.error("工序修改失败");
    }

    @Override
    public Result saveProcess(ProcessDto processDto, HttpServletRequest request) {
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            Users users = BeanUtil.copyProperties(redisCache.getCacheObject("login"+userId), Users.class);
            String name = users.getName();
            //System.out.println(userId);
            processDto.setUpdateUsername(name);
            processDto.setCreateUsername(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        DateTime nowTime = DateUtil.date();
        processDto.setUpdateTime(nowTime);
        processDto.setCreateTime(nowTime);
        Process process = new Process();
        BeanUtils.copyProperties(processDto,process);

        Boolean aBoolean = productionManagementClient.updateEquipmentStatus(process.getEquipmentId());
        if(!aBoolean){
            return Result.error("设备状态信息修改失败,工序增加异常");
        }

        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getProcess, processDto.getProcess());
        long count = count(queryWrapper);
        if(count>0){
            return Result.error("工序名称不可重复，请重新添加");
        }
        boolean b = saveOrUpdate(process);
        if(b==true) {
            Process process1 = getOne(queryWrapper);
            processDto.setId(process1.getId());
            Result result = null;
            try {
                result = processMaterialRelationController.addOrUpdate(processDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(result.getCode()==1){
                return Result.success(null,"增加成功");
            }
        }
        return Result.error("增加失败");
    }

    @Override
    public Result removePeocess(String processId) {
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(FlowProcessRelation::getProcessId,processId);
        long count = processRelationService.count(queryWrapper);
//        List<TFlowProcessRelation> list = processRelationService.list(queryWrapper);
        if(count>0){
            return Result.error("请先删除与本工序有关的流程");
        }
        //删除工序
        LambdaQueryWrapper<Process> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Process::getIsDelete,1).eq(Process::getId,processId);
        Process one = getOne(queryWrapper1);
        one.setIsDelete(0);
        boolean b = updateById(one);

        //修改设备状态为空闲
        boolean aBoolean = productionManagementClient.updateEquipmentStatus(one.getEquipmentId());


        //删除工序相关关系
        LambdaUpdateWrapper<ProcessMaterialRelation> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(ProcessMaterialRelation::getProcessId,processId)
                .set(ProcessMaterialRelation::getIsDelete,0);
        boolean update = processMaterialRelationService.update(updateWrapper);


        if(b&&update&&aBoolean){
            return Result.success(null,"删除成功");
        }
        return Result.error("操作失败，请刷新页面重试");
    }

    @Override
    public Result batchDeleteById(List<String> idList) {
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(FlowProcessRelation::getProcessId,idList);
        long count = processRelationService.count(queryWrapper);
        if(count>0){
            return Result.error("请保证删除的工序没有流程与之绑定");
        }
        // 获取需要逻辑删除的记录的ID列表
        Vector<Process> recordList = new Vector<>();
        Vector<ProcessMaterialRelation> processMaterialRelationList = new Vector<>();
        for (String id : idList) {
            Process process=new Process();
            process.setId(id);
            process.setIsDelete(0);  // 设置要更新的字段和值
            recordList.add(process);

            ProcessMaterialRelation processMaterialRelation = new ProcessMaterialRelation();
            processMaterialRelation.setProcessId(id);
            processMaterialRelation.setIsDelete(0);
            processMaterialRelationList.add(processMaterialRelation);
        }

        boolean b = updateBatchById(recordList);

        boolean b1 = processMaterialRelationService.updateBatchById(processMaterialRelationList);
        if(b&&b1){
            return Result.success(null,"查询成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result<ProcessPageReq> likeQuery(String searchName, int size, int current) {
        ProcessPageReq req=new ProcessPageReq();
        req.setCurrent(current);
        req.setSize(size);
        if(StringUtil.isNullOrEmpty(searchName)){
            ProcessPageReq page = page(req);
            return Result.success(page,"查询成功");
        }
        boolean matches = searchName.matches("-?\\d+(\\.\\d+)?");
        LambdaQueryWrapper<Process> queryWrapper=new LambdaQueryWrapper<>();
        if(matches){
            queryWrapper.eq(Process::getId,searchName);
        }else {
            queryWrapper.like(Process::getProcess, searchName);
        };
        ProcessPageReq page =page(req, queryWrapper);
        return Result.success(page,"查询成功");
    }

    @Override
    public Result queryEquipmentMap(HttpServletRequest request) {
        String token = request.getHeader("token");
        List<String> equipmentTypes = productionManagementClient.queryEquipmentTypes(token);
        List<EquipmentVo> equipmentVoList = new ArrayList<>();
        for(String equipmentType : equipmentTypes){
            EquipmentVo equipmentVo = new EquipmentVo();
            equipmentVo.setTitle(equipmentType);
            List<Equipment> equipmentList = productionManagementClient.queryEquipmentByFunction(equipmentType);
            List<Map<String,String>> mapList = new ArrayList<>();
            for(Equipment equipment : equipmentList){
                Map<String,String> map = new HashMap<>();
                map.put("id", String.valueOf(equipment.getEquipmentID()));
                map.put("title",equipment.getEquipmentName());
                mapList.add(map);
            }
            equipmentVo.setEquipmentMapList(mapList);
        }

        return Result.success(equipmentVoList,"查询成功");
    }

    @Override
    public Result queryProcessUpdateDtoById(String id) {
        if(id==null){
            return Result.error("传入参数为空");
        }

        Process process = (Process) queryById(id).getData();
        ProcessUpdateDto processUpdateDto = new ProcessUpdateDto();
        BeanUtil.copyProperties(process,processUpdateDto);
        String equipmentId = process.getEquipmentId();

        Equipment equipment = productionManagementClient.queryEquipmentById(equipmentId);
        if(equipment==null){
            return Result.error("设备未查询成功");
        }
        processUpdateDto.setFunction(equipment.getFunctionName());
        processUpdateDto.setEquipmentName(equipment.getEquipmentName());

        LambdaQueryWrapper<ProcessMaterialRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ProcessMaterialRelation::getIsDelete,1)
                .eq(ProcessMaterialRelation::getProcessId,id);
        List<ProcessMaterialRelation> processMaterialRelations = processMaterialRelationService.list(queryWrapper);
        List<MaterialVo> materialVoList = new Vector<>();
        for(ProcessMaterialRelation processMaterialRelation : processMaterialRelations){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setTitle(processMaterialRelation.getMaterialName());
            materialVo.setValue(processMaterialRelation.getMaterialId());
            materialVo.setNumber(processMaterialRelation.getNumber());
            materialVoList.add(materialVo);
        }
        processUpdateDto.setMaterialVoList(materialVoList);
        return Result.success(processUpdateDto,"查询成功");
    }

    @Override
    public Result queryById(String id) {
        LambdaQueryWrapper<Process> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Process::getIsDelete,1)
                .eq(Process::getId,id);
        Process process = getOne(lambdaQueryWrapper);
        return Result.success(process,"查询成功");
    }




}