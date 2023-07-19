package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.vo.MaterialVo;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-15:22
 * @description
 */
public interface ProcessMaterialRelationService extends IService<ProcessMaterialRelation> {
    /**
     * 工序与原材料关系的增加和修改
     *
     * @param processDto 工序信息
     * @return
     * @throws Exception
     */
    List<ProcessMaterialRelation> addOrUpdate(ProcessDto processDto) throws Exception;

    /**
     * 全部原材料集合
     *
     * @return 原材料VO集合
     * @throws Exception 异常
     */
    List<MaterialVo> allMaterialViewServer() throws Exception;

    /**
     * 绘制当前工序下的原材料
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<MaterialVo> currentMaterialViewServer(String processId) throws Exception;
}
