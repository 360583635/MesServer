package com.job.dispatchService.lineManager.dto;

import com.job.common.pojo.Process;
import com.job.dispatchService.lineManager.vo.MaterialVo;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-29-23:40
 * @description
 */
public class ProcessUpdateDto extends Process {
    private String function;
    private String equipmentName;

    private List<MaterialVo> materialVoList;

    public List<MaterialVo> getMaterialVoList() {
        return materialVoList;
    }

    public void setMaterialVoList(List<MaterialVo> materialVoList) {
        this.materialVoList = materialVoList;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}