package com.job.dispatchService.lineManager.controller;

import com.job.common.pojo.Process;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.ProductionManagementClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-19:29
 * @description
 */
@RestController
@RequestMapping("/dispatch/process/material")
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
}
