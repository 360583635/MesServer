package com.job.dispatchService.lineManager.dto;

import com.job.common.pojo.Flow;
import com.job.dispatchService.lineManager.vo.ProcessVo;

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
    private List<ProcessVo> processVoList;

    public List<ProcessVo> getProcessVoList() {
        return processVoList;
    }

    public void setProcessVoList(List<ProcessVo> processVoList) {
        this.processVoList = processVoList;
    }
}
