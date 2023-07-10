package com.job.dispatchservice.linemanager.dto;

import com.job.common.pojo.Flow;
import com.job.dispatchservice.linemanager.vo.ProcessVo;

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
    private List<ProcessVo> ProcessVoList;

    public List<ProcessVo> getProcessVoList() {
        return ProcessVoList;
    }

    public void setProcessVoList(List<ProcessVo> ProcessVoList) {
        this.ProcessVoList = ProcessVoList;
    }
}
