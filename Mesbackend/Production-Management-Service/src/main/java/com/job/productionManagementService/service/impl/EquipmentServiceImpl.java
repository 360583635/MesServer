package com.job.productionManagementService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Equipment;
import com.job.productionManagementService.mapper.EquipmentMapper;
import com.job.productionManagementService.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dangerous
 * @create 2023-07-17-18:42
 * @description
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;
    @Override
    public Boolean updateEquipmentStatus(String equipmentId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Equipment::getEquipmentId,equipmentId)
                .eq(Equipment::getIsDelete,1);
        Equipment equipment = equipmentMapper.selectOne(queryWrapper);
        if(equipment.getEquipmentState()==0){
            equipment.setEquipmentState(1);
        }else if(equipment.getEquipmentState()==1){
            equipment.setEquipmentState(0);
        }
        int updateById = equipmentMapper.updateById(equipment);
        if(updateById !=0){
            return true;
        }
        return false;
    }
}
