package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Material;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.mapper.FlowProcessRelationMapper;
import com.job.dispatchService.lineManager.mapper.ProcessMapper;
import com.job.dispatchService.lineManager.mapper.ProcessMaterialRelationMapper;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.ProcessMaterialRelationService;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import com.job.feign.clients.ProductionManagementClient;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-15:23
 * @description
 */
@Service
@Transactional
public class ProcessMaterialRelationServiceImpl extends ServiceImpl<ProcessMaterialRelationMapper, ProcessMaterialRelation> implements ProcessMaterialRelationService{


    @Autowired
    private ProductionManagementClient productionManagementClient;

    @Resource
    private ProcessMaterialRelationMapper processMaterialRelationMapper;


    @Resource
    private FlowProcessRelationMapper flowProcessRelationMapper;

    @Resource
    private ProcessMapper processMapper;
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

    @Override
    public String addOrUpdateUI(Model model, Process record, HttpServletRequest request) {
        String token = request.getHeader("token");
        List<MaterialVo> allMaterialVos = null;
        try {
            allMaterialVos = allMaterialViewServer(token);
            //全部工序
            model.addAttribute("allMaterial",allMaterialVos);
            if(StringUtils.isNotEmpty(record.getId())){
                Process processById = processMapper.selectById(record.getId());
                //当前流程信息
                model.addAttribute("process",processById);
                List<MaterialVo> currentMaterialVos = currentMaterialViewServer(record.getId());
                model.addAttribute("currentMaterial",currentMaterialVos);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    @Override
    public Result addOrUpdateRelation(ProcessDto processDto) {
        List<ProcessMaterialRelation> processMaterialRelationList = null;
        try {
            processMaterialRelationList = addOrUpdate(processDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        log.info("返回的工序与原材料关系集合有"+String.valueOf(processMaterialRelationList)+"条数据。");
        boolean saveBatch = saveBatch(processMaterialRelationList);
        if(saveBatch == true){
            return Result.success(null,"更新成功");
        }
        return Result.error("更新失败");
    }

    @Override
    public List<String> queryMaterialsByProcess(String processName) {
        LambdaQueryWrapper<ProcessMaterialRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ProcessMaterialRelation::getIsDelete,1)
                .eq(ProcessMaterialRelation::getProcessName,processName);
        List<ProcessMaterialRelation> processMaterialRelationList = list(queryWrapper);
        List<String> materialNameList = new ArrayList<>();
        for(ProcessMaterialRelation processMaterialRelation : processMaterialRelationList){
            String materialName = processMaterialRelation.getMaterialName();
            materialNameList.add(materialName);
        }
        return materialNameList;
    }

    @Override
    public Map<String, Integer> queryMaterialsByFlowName(Map<String, String> map) {
        String flowName = map.get("flowName");
        Map<String,Integer> materialMap = new HashMap<>();

        LambdaQueryWrapper<FlowProcessRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(FlowProcessRelation::getIsDelete,1)
                .eq(FlowProcessRelation::getFlow,flowName)
                .orderBy(true,false,FlowProcessRelation::getSortNum);
        List<FlowProcessRelation> processRelationList = flowProcessRelationMapper.selectList(queryWrapper);

        //遍历流程工序关系列表
        for(FlowProcessRelation flowProcessRelation:processRelationList){
            String process = flowProcessRelation.getProcess();
            List<String> queryMaterialsByProcess = queryMaterialsByProcess(process);
            for(String materialName : queryMaterialsByProcess){
                LambdaQueryWrapper<ProcessMaterialRelation> processMaterialRelationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                processMaterialRelationLambdaQueryWrapper
                        .eq(ProcessMaterialRelation::getIsDelete,1)
                        .eq(ProcessMaterialRelation::getProcessName,process)
                        .eq(ProcessMaterialRelation::getMaterialName,materialName);
                ProcessMaterialRelation processMaterialRelation = getOne(processMaterialRelationLambdaQueryWrapper);
                if(materialMap.containsKey(materialName)==false){
                    //根据工序名称，原材料名称查询工序原材料关系表获取原材料数量信息
                    materialMap.put(materialName, Integer.valueOf(processMaterialRelation.getNumber()));
                }else{
                    materialMap.put(materialName, Integer.valueOf(processMaterialRelation.getNumber())+materialMap.get(materialName));
                }
            }
        }

        return materialMap;
    }

    @Override
    public List<MaterialVo> queryMaterials(HttpServletRequest request) {
        String token = request.getHeader("token");
        List<Material> materialList = productionManagementClient.queryMaterials(token);
        List<MaterialVo> materialVoList = new ArrayList<>();
        for(Material material : materialList){
            MaterialVo materialVo = new MaterialVo();
            materialVo.setTitle(material.getMaterialName());
            materialVo.setValue(String.valueOf(material.getMaterialId()));
            materialVoList.add(materialVo);
        }
        return materialVoList;
    }

}
