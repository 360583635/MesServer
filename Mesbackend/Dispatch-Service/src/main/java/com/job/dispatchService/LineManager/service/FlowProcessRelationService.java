package com.job.dispatchService.LineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.dispatchService.LineManager.dto.FlowDto;
import com.job.dispatchService.LineManager.vo.ProcessVo;
import com.job.dispatchService.common.Result;
import com.job.dispatchService.LineManager.pojo.TFlowProcessRelation;

import java.util.List;

public interface FlowProcessRelationService extends IService<TFlowProcessRelation> {
    /**
     * 流程与工序关系新增与修改
     *
     * @param FlowDto 流程信息DTO
     * @return 执行结果
     * @throws Exception 异常
     */
    Result addOrUpdate(FlowDto FlowDto) throws Exception;

    /**
     * 全部工序集合
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<ProcessVo> allProcessViewServer() throws Exception;

    /**
     * 绘制当前流程下的工序
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<ProcessVo> currentProcessViewServer(String flowId) throws Exception;
}