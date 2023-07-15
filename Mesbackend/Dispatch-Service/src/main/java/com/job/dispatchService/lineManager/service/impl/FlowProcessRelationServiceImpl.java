package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchservice.linemanager.dto.FlowDto;
import com.job.dispatchservice.linemanager.mapper.FlowProcessRelationMapper;
import com.job.dispatchservice.linemanager.service.FlowService;
import com.job.dispatchservice.linemanager.service.ProcessService;
import com.job.dispatchservice.linemanager.vo.ProcessVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.job.common.pojo.Process;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlowProcessRelationServiceImpl extends ServiceImpl<FlowProcessRelationMapper, FlowProcessRelation> implements FlowProcessRelationService {

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
        List<FlowProcessRelation> flowProcessRelationList = new ArrayList<>();
        StringBuilder processBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(processVoList)||processVoList.size() <= 1){
            throw new Exception("流程下必须存在至少两个工序");
        }
        Flow flow = new Flow();
        BeanUtils.copyProperties(flowDto,flow);
        String flowId = flow.getId();
        String flowName = flow.getFlow();
        if(StringUtils.isNotEmpty(flowId)){
            flowProcessRelationMapper.deleteProcessRelationByFlowId(flowId);
        }else{
            //如果流程头表为空先创建一下
            flowService.saveOrUpdate(flow);
            flowId = flow.getId();
        }
        // 批量处理需要插入数据库的工序
        log.info("开始处理流程下工序关系");
        for(int i=0;i<processVoList.size();i++){
            FlowProcessRelation relation = new FlowProcessRelation();
            relation.setFlowId(flowId);//流程ID
            relation.setFlow(flowName);//流程编码
            Process process = processService.getById(processVoList.get(i).getValue());
            if(i == 0){//首个工序
                relation.setPerProcess("");
                relation.setPerProcess("");
                //下一道工序
                relation.setNextProcessId(processVoList.get(i + 1).getValue());
                relation.setNextProcess(processVoList.get(i + 1).getTitle());
                relation.setProcessType("firstProcess");
            }else if (i + 1 >= processVoList.size()) {//末尾工序
                //前一道工序
                relation.setPerProcessId(processVoList.get(i - 1).getValue());
                relation.setPerProcess(processVoList.get(i - 1).getTitle());
                relation.setNextProcessId("");
                relation.setNextProcess("");
                relation.setProcessType("lastProcess");
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
        flow.setProcess(processBuilder.toString());
        //更细流程头表信息
        flowService.saveOrUpdate(flow);
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
        List<Process> processList = processService.list();
        List<ProcessVo> processvos = new ArrayList<>();
        //得出全部的工序数据
        for(Process process : processList) {
            ProcessVo processVo = new ProcessVo();
            processVo.setValue(process.getId());
            processVo.setTitle(process.getProcess());
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