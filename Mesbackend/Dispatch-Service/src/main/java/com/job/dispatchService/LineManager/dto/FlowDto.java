package com.job.dispatchService.LineManager.dto;

import com.job.dispatchService.LineManager.vo.ProcessVo;
import com.job.dispatchService.LineManager.pojo.TFlow;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-19:38
 * @description
 */
public class FlowDto extends TFlow {
    /**
     * 流程与工序关系集合
     */
    private List<ProcessVo> ProcessVoList;

    public List<ProcessVo> getProcessVoList() {
        return ProcessVoList;
    }

    public void setProcessVoList(List<ProcessVo> ProcessVoList) {
        this.ProcessVoList = ProcessVoList;
    }
}
