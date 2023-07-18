package com.job.dispatchService.lineManager.dto;

import com.job.common.pojo.Process;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.dispatchService.lineManager.vo.ProcessVo;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-15:33
 * @description
 */
public class ProcessDto extends Process {
    /**
     * 流程与工序关系集合
     */
    private List<MaterialVo> MaterialVoList;

    public List<MaterialVo> getMaterialVoList() {
        return MaterialVoList;
    }

    public void setMaterialVoList(List<MaterialVo> MaterialVoList) {
        this.MaterialVoList = MaterialVoList;
    }
}
