package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dispatchService.LineManager.dto.FlowDto;
import com.job.dispatchService.LineManager.mapper.FlowProcessRelationMapper;
import com.job.dispatchService.LineManager.service.FlowProcessRelationService;
import com.job.dispatchService.LineManager.service.FlowService;
import com.job.dispatchService.LineManager.service.ProcessService;
import com.job.dispatchService.LineManager.vo.ProcessVo;
import com.job.dispatchService.common.Result;
import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;
import com.job.dispatchService.LineManager.pojo.TProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowProcessRelationServiceImpl extends ServiceImpl<FlowProcessRelationMapper, TFlowProcessRelation> implements FlowProcessRelationService {
    /**
     * 流程基础数据服务
     */
    @Autowired
    public FlowService flowService;

    /**
     * 工序基础数据服务
     */
    @Autowired
    public ProcessService processService;
    /**
     * 流程与工序关系
     */
    @Autowired
    public FlowProcessRelationMapper flowProcessRelationMapper;


    /**
     * 流程与工序关系新增与修改
     *
     * @param FlowDto 流程信息DTO
     * @return
     * @throws Exception
     */
    @Override
    public Result addOrUpdate(FlowDto FlowDto) throws Exception {
        return null;
    }

    /**
     * 全部工序集合
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ProcessVo> allProcessViewServer() throws Exception {
        List<TProcess> processList = processService.list();
        List<ProcessVo> processvos = new ArrayList<>();
        //得出全部的工序数据
        for(TProcess tProcess : processList) {
            ProcessVo processVo = new ProcessVo();
            processVo.setValue(tProcess.getId());
            processVo.setTitle(tProcess.getProcess());
            processvos.add(processVo);
        }
        return processvos;
    }


    /**
     * 绘制当前流程下的工序
     *
     * @param flowId
     * @return
     * @throws Exception
     */
    @Override
    public List<ProcessVo> currentProcessViewServer(String flowId) throws Exception {
        return flowProcessRelationMapper.queryOperRelationByFlowId(flowId);
    }
}