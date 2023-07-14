package com.job.dispatchService.lineManager.controller;
import com.job.common.result.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.dispatchService.lineManager.dto.FlowDto;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.ProcessVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:05
 * @description
 */
@RestController
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
    public String addOrUpdateUI(Model model, Flow record) throws Exception {
        List<ProcessVo> allProcessVos = flowProcessRelationService.allProcessViewServer();
        //全部工序
        model.addAttribute("allProcess",allProcessVos);
        if(StringUtils.isNotEmpty(record.getId())){
            Flow flowbyId = flowService.getById(record.getId());
            //当前流程信息
            model.addAttribute("flow",flowbyId);
            List<ProcessVo> currentProcessVo = flowProcessRelationService.currentProcessViewServer(record.getId());
            model.addAttribute("currentProcess",currentProcessVo);
        }
        return "";
    }

    /**
     * 流程信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    // TODO: 流程信息分页查询


    /**
     * 删除流程与工序关系
     *
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(FlowDto req) throws Exception {
        //先删除流程头表
        LambdaQueryWrapper<Flow> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1
                .eq(Flow::getIsDelete,1)
                .eq(Flow::getId,req.getId());
        Flow flowById = flowService.getOne(queryWrapper1);
        flowById.setIsDelete(0);
        //删除流程关系表
        LambdaQueryWrapper<FlowProcessRelation> queryWrapper2 = new LambdaQueryWrapper();
        queryWrapper2
                .eq(FlowProcessRelation::getIsDelete,req.getIsDelete())
                .eq(FlowProcessRelation::getFlowId, req.getId());
        FlowProcessRelation flowProcessRelation = flowProcessRelationService.getOne(queryWrapper2);
        flowProcessRelation.setIsDelete(0);
        return Result.success(null,"删除成功");
    }

}
