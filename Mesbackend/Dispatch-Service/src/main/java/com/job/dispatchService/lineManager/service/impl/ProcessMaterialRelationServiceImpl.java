package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.mapper.ProcessMaterialRelationMapper;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.ProductionManagementClient;
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
    private ProductionManagementClient productionManagementClient;

    @Autowired
    private ProcessMaterialRelationMapper processMaterialRelationMapper;

    /*@Autowired
    private ProcessMaterialRelationService relationService;*/

    //逻辑删除1未删除，0已删除
    private static Integer IS_DELETE=1;


    @Override
    public List<ProcessMaterialRelation> addOrUpdate(ProcessDto processDto) throws Exception {
        List<MaterialVo> materialVoList = processDto.getMaterialVoList();
        List<FlowProcessRelation> flowProcessRelationList = new ArrayList<>();
        List<ProcessMaterialRelation> processMaterialRelationList=new ArrayList<>();

        for (MaterialVo materialVo :materialVoList) {
            ProcessMaterialRelation processMaterialRelation=new ProcessMaterialRelation(processDto.getId(),processDto.getProcess());
            processMaterialRelation.setProcessId(processDto.getId());
            processMaterialRelation.setMaterialId(materialVo.getValue());
            processMaterialRelation.setMaterialName(materialVo.getTitle());
            processMaterialRelation.setNumber(materialVo.getNumber());
            processMaterialRelation.setIsDelete(IS_DELETE);
            processMaterialRelationList.add(processMaterialRelation);
        }


        return processMaterialRelationList;
    }

    @Override
    public List<MaterialVo> allMaterialViewServer(String token) throws Exception {

        List<Material> materials = productionManagementClient.queryMaterials(token);
        List<MaterialVo> materialVos = new ArrayList<>();
        for(Material material : materials){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setValue(String.valueOf(material.getMaterialId()));
            materialVo.setTitle(material.getMaterialName());
            materialVos.add(materialVo);
        }
        return materialVos;
    }



    public List<MaterialVo> currentMaterialViewServer(String processId) throws Exception {
        return processMaterialRelationMapper.queryMaterialRelationByProcessId(processId);
    }

}
