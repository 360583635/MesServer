package com.job.dispatchService.lineManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.ProductionManagementClient;
import com.job.feign.pojo.Material;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-19:29
 * @description
 */
@RestController
@RequestMapping("/dispatch/process/material")
@CrossOrigin
public class ProcessMaterialRelationController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessMaterialRelationService processMaterialRelationService;

    @Autowired
    private ProductionManagementClient productionManagementClient;

    /**
     * 工序与原材料关系管理编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, Process record) throws Exception {
        List<MaterialVo> allMaterialVos = processMaterialRelationService.allMaterialViewServer();
        //全部工序
        model.addAttribute("allMaterial",allMaterialVos);
        if(StringUtils.isNotEmpty(record.getId())){
            Process processById = processService.getById(record.getId());
            //当前流程信息
            model.addAttribute("process",processById);
            List<MaterialVo> currentMaterialVos = processMaterialRelationService.currentMaterialViewServer(record.getId());
            model.addAttribute("currentMaterial",currentMaterialVos);
        }
        return "";
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
        return processMaterialRelationService.addOrUpdate(processDto);
    }


    /**
     * 根据工序名称查询原材料
     * @param processName
     * @return List<String> 原材料名称集合
     * @throws Exception
     */
    @PostMapping("/queryMaterialsByProcess")
    public List<String> queryMaterialsByProcess(@RequestBody String processName) throws Exception{
        LambdaQueryWrapper<ProcessMaterialRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ProcessMaterialRelation::getIsDelete,1)
                .eq(ProcessMaterialRelation::getProcessName,processName);
        List<ProcessMaterialRelation> processMaterialRelationList = processMaterialRelationService.list(queryWrapper);
        List<String> materialNameList = new ArrayList<>();
        for(ProcessMaterialRelation processMaterialRelation : processMaterialRelationList){
            String materialName = processMaterialRelation.getMaterialName();
            materialNameList.add(materialName);
        }
        return materialNameList;
    }

    /**
     * 查询全部原材料Vo
     * @return
     */
    @GetMapping("/queryMaterials")
    public List<MaterialVo> queryMaterials(){
        List<Material> materialList = productionManagementClient.queryMaterials();
        List<MaterialVo> materialVoList = new ArrayList<>();
        for(Material material : materialList){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setTitle(material.getMaterialName());
            materialVo.setValue(String.valueOf(material.getMaterialId()));
        }
        return materialVoList;
    }


}
