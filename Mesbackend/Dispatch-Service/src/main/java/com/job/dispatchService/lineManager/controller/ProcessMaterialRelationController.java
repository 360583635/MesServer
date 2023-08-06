package com.job.dispatchService.lineManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.AuthenticationClient;
import com.job.feign.clients.ProductionManagementClient;
import com.job.feign.pojo.Material;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-19:29
 * @description
 */
@RestController
@RequestMapping("/dispatch/process/material")
@Slf4j
public class ProcessMaterialRelationController {



    @Autowired
    private ProcessMaterialRelationService processMaterialRelationService;



    /**
     * 工序与原材料关系管理编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, Process record, HttpServletRequest request) throws Exception {
       return  processMaterialRelationService.addOrUpdateUI(model,record,request);

    }

    /**
     * 工序与原材料关系管理新增+修改
     * @param processDto
     * @return
     * @throws Exception
     */
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(@RequestBody ProcessDto processDto) throws Exception {
        return processMaterialRelationService.addOrUpdateRelation(processDto);

    }


    /**
     * 根据工序名称查询原材料
     * @param processName
     * @return List<String> 原材料名称集合
     * @throws Exception
     */
    @PostMapping("/queryMaterialsByProcess")
    public List<String> queryMaterialsByProcess(@RequestParam String processName) throws Exception{
        return processMaterialRelationService.queryMaterialsByProcess(processName);

    }

    /**
     *根据流程功能查询原材料
     */
    @PostMapping("/queryMaterialsByFlowName")
    public Map<String,Integer> queryMaterialsByFlowName(@RequestBody Map<String,String> map) throws Exception {
        return processMaterialRelationService.queryMaterialsByFlowName(map);

    }

    /**
     * 查询全部原材料Vo
     * @return
     */
    @GetMapping("/queryMaterials")
    public List<MaterialVo> queryMaterials(HttpServletRequest request){
        return processMaterialRelationService.queryMaterials(request);

    }


}
