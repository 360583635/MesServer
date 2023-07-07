package com.job.dispatchService.LineManager.controller;

import com.job.dispatchService.LineManager.service.FlowProcessRelationService;
import com.job.dispatchService.LineManager.service.FlowService;
import com.job.dispatchService.LineManager.service.ProcessService;
import com.job.dispatchService.LineManager.pojo.TFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:05
 * @description
 */
@Controller
@RequestMapping("/dispatch/flow/process")
public class FlowProcessRelationController {

    /**
     * 工序基础数据服务
     */
    @Autowired
    public ProcessService processService;

    /**
     * 流程基础数据服务
     */
    @Autowired
    public FlowService flowService;

    /**
     * 流程与工序基础数据服务
     */
    @Autowired
    public FlowProcessRelationService flowProcessRelationService;


    /**
     * 流程与工序关系管理界面
     *
     * @param model 模型
     * @return 流程与工序关系管理界面
     */
    @GetMapping("/list-ui")
    public String listUI(Model model){
        return "";
    }

    /**
     * 流程与工序关系管理编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, TFlow record){
        return "";
    }
}
