package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

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
    List<MaterialVo> allMaterialViewServer(String token) throws Exception;

    /**
     * 绘制当前工序下的原材料
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<MaterialVo> currentMaterialViewServer(String processId) throws Exception;

    String addOrUpdateUI(Model model, Process record, HttpServletRequest request);

    Result addOrUpdateRelation(ProcessDto processDto);

    List<String> queryMaterialsByProcess(String processName);

    Map<String, Integer> queryMaterialsByFlowName(Map<String, String> map);

    List<MaterialVo> queryMaterials(HttpServletRequest request);
}
