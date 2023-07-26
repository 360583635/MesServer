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
     * 工序与原材料关系集合
     */
    private List<MaterialVo> materialVoList;

    public List<MaterialVo> getMaterialVoList() {
        return materialVoList;
    }

    public void setMaterialVoList(List<MaterialVo> MaterialVoList) {
        this.materialVoList = materialVoList;
    }
}
