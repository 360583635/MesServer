package com.job.dispatchService.lineManager.dto;

import com.job.common.pojo.Flow;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-19:38
 * @description
 */
public class FlowDto extends Flow {
    /**
     * 流程与工序关系集合
     */
    private List<com.job.dispatchservice.linemanager.vo.ProcessVo> ProcessVoList;

    public List<com.job.dispatchservice.linemanager.vo.ProcessVo> getProcessVoList() {
        return ProcessVoList;
    }

    public void setProcessVoList(List<com.job.dispatchservice.linemanager.vo.ProcessVo> ProcessVoList) {
        this.ProcessVoList = ProcessVoList;
    }
}
