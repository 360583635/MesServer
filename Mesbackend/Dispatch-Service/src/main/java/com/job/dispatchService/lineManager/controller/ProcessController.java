package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
//import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;
//import com.job.dispatchService.LineManager.pojo.TProcess;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.request.ProcessPageReq;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.EquipmentVo;
import com.job.feign.clients.ProductionManagementClient;
import com.job.feign.pojo.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:05
 * @description
 */
@RestController
@RequestMapping("/dispatch/process")
@CrossOrigin
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private FlowProcessRelationService processRelationService;

    @Autowired
    private ProductionManagementClient productionManagementClient;

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
        return Result.success(null,"修改成功");

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
        return Result.success(null,"增加成功");
    }

    /**
     * 删除工序
     * @param processId
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result removePeocess(@RequestBody String processId){
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(FlowProcessRelation::getProcessId,processId);
        long count = processRelationService.count(queryWrapper);
//        List<TFlowProcessRelation> list = processRelationService.list(queryWrapper);
        if(count>0){
            return Result.error("请先删除与本工序有关的流程");
        }
        boolean b = processService.removeById(processId);
        if(b){
            return Result.success(null,"删除成功");
        }
        return Result.error("操作失败，请刷新页面重试");
    }

    @PostMapping("/query/{processName}")
    @ResponseBody
    public Result<ProcessPageReq> query(@PathVariable("process") String procrssName, @RequestBody ProcessPageReq req){
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

    /**
     * 查询全部设备
     */
    @GetMapping("/queryEquipments")
    public Result queryEquipments(){
        List<Equipment> equipmentList = productionManagementClient.queryEquipments();
        return Result.success(equipmentList,"查询成功");
    }

    /**
     * 查询全部设备类型
     */
    @GetMapping("/queryEquipmentTypes")
    public Result queryEquipmentTypes(){
        List<String> equipmentTypes = productionManagementClient.queryEquipmentTypes();
        return Result.success(equipmentTypes,"查询成功");
    }

    /**
     * 根据设备功能类型查询设备
     */
    @GetMapping("/queryEquipmentsByType/{functionName}")
    public Result queryEquipmentsByType(@PathVariable("functionName") String functionName){
        List<Equipment> equipmentList = productionManagementClient.queryEquipmentsByType(functionName);
        /*List<String> equipmentNameList = new ArrayList<>();
        for(Equipment equipment : equipmentList){
            String equipmentName = equipment.getEquipmentName();
            equipmentNameList.add(equipmentName);
        }*/
        return Result.success(equipmentList,"查询成功");
    }

    @GetMapping("/queryEquipmentMap")
    public Result queryEquipmentMap(){
        List<String> equipmentTypes = productionManagementClient.queryEquipmentTypes();
        List<EquipmentVo> equipmentVoList = new ArrayList<>();
        for(String equipmentType : equipmentTypes){
            EquipmentVo equipmentVo = new EquipmentVo();
            equipmentVo.setTitle(equipmentType);
            List<Equipment> equipmentList = productionManagementClient.queryEquipmentsByType(equipmentType);
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
}
