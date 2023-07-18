package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.mapper.FlowProcessRelationMapper;
import com.job.dispatchService.lineManager.mapper.ProcessMaterialRelationMapper;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.dispatchService.lineManager.vo.ProcessVo;
import com.job.feign.clients.MaterialClient;
import com.job.feign.pojo.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-15:23
 * @description
 */
@Service
public class ProcessMaterialRelationServiceImpl extends ServiceImpl<ProcessMaterialRelationMapper, ProcessMaterialRelation> implements ProcessMaterialRelationService{


    @Autowired
    private MaterialClient materialClient;

    @Autowired
    private ProcessMaterialRelationMapper processMaterialRelationMapper;

    @Override
    public Result addOrUpdate(ProcessDto processDto) throws Exception {
        return null;
    }

    @Override
    public List<MaterialVo> allMaterialViewServer() throws Exception {
        List<Material> materials = materialClient.queryMaterials();
        List<MaterialVo> materialVos = new ArrayList<>();
        for(Material material : materials){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setValue(String.valueOf(material.getMaterialId()));
            materialVo.setTitle(material.getMaterialName());
            materialVos.add(materialVo);
        }
        return materialVos;
    }

    /**
     * 绘制当前流程下的工序
     *
     * @param processId
     * @return
     * @throws Exception
     */

    public List<MaterialVo> currentMaterialViewServer(String processId) throws Exception {
        return processMaterialRelationMapper.queryMaterialRelationByProcessId(processId);
    }
}
