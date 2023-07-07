package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dispatchService.LineManager.dto.FlowDto;
import com.job.dispatchService.LineManager.mapper.FlowProcessRelationMapper;
import com.job.dispatchService.LineManager.pojo.TFlow;
import com.job.dispatchService.LineManager.service.FlowProcessRelationService;
import com.job.dispatchService.LineManager.service.FlowService;
import com.job.dispatchService.LineManager.service.ProcessService;
import com.job.dispatchService.LineManager.vo.ProcessVo;
import com.job.dispatchService.common.Result;
import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;
import com.job.dispatchService.LineManager.pojo.TProcess;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowProcessRelationServiceImpl extends ServiceImpl<FlowProcessRelationMapper, TFlowProcessRelation> implements FlowProcessRelationService {

    Logger log = LoggerFactory.getLogger(FlowProcessRelationServiceImpl.class);


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
     * @param flowDto 流程信息DTO
     * @return
     * @throws Exception
     */
    @Override
    public Result addOrUpdate(FlowDto flowDto) throws Exception {
        List<ProcessVo> processVoList = flowDto.getProcessVoList();
        List<TFlowProcessRelation> flowProcessRelationList = new ArrayList<>();
        StringBuilder processBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(processVoList)||processVoList.size() <= 1){
            throw new Exception("流程下必须存在至少两个工序");
        }
        TFlow tFlow = new TFlow();
        BeanUtils.copyProperties(flowDto,tFlow);
        String flowId = tFlow.getId();
        String flow = tFlow.getFlow();
        if(StringUtils.isNotEmpty(flowId)){
            flowProcessRelationMapper.deleteProcessRelationByFlowId(flowId);
        }else{
            //如果流程头表为空先创建一下
            flowService.saveOrUpdate(tFlow);
            flowId = tFlow.getId();
        }
        // 批量处理需要插入数据库的工序
        log.info("开始处理流程下工序关系");
        for(int i=0;i<processVoList.size();i++){
            TFlowProcessRelation relation = new TFlowProcessRelation();
            relation.setFlowId(flowId);//流程ID
            relation.setFlow(flow);//流程编码
            TProcess process = processService.getById(processVoList.get(i).getValue());
            if(i == 0){//首个工序
                relation.setPerProcess("");
                relation.setPerProcess("");
                //下一道工序
                relation.setNextProcessId(processVoList.get(i + 1).getValue());
                relation.setNextProcess(processVoList.get(i + 1).getTitle());
            }else if (i + 1 >= processVoList.size()) {//末尾工序
                //前一道工序
                relation.setPerProcessId(processVoList.get(i - 1).getValue());
                relation.setPerProcess(processVoList.get(i - 1).getTitle());
                relation.setNextProcessId("");
                relation.setNextProcess("");
            }else {
                //前一道工序
                relation.setPerProcessId(processVoList.get(i - 1).getValue());
                relation.setPerProcess(processVoList.get(i - 1).getTitle());
                //下一道工序
                relation.setNextProcessId(processVoList.get(i + 1).getValue());
                relation.setNextProcess(processVoList.get(i + 1).getTitle());
            }
            //当前工序
            relation.setProcessId(processVoList.get(i).getValue());
            relation.setProcess(processVoList.get(i).getTitle());
            relation.setSortNum(i + 1);//顺序
            if (i == 0) {
                processBuilder.append(process.getProcessDesc());
            } else {
                processBuilder.append("->" + process.getProcessDesc());
            }
            flowProcessRelationList.add(relation);
        }
        log.info("本次流程时序" + processBuilder.toString());
        tFlow.setProcess(processBuilder.toString());
        //更细流程头表信息
        flowService.saveOrUpdate(tFlow);
        saveOrUpdateBatch(flowProcessRelationList);
        return Result.success();
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