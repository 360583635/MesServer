package com.job.productionManagementService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Inventory;
import com.job.productionManagementService.mapper.InventoryMapper;
import com.job.productionManagementService.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService  {

    private InventoryMapper inventoryMapper ;

    @Override
    public Integer getMaterialNumberByName(String materialName){

        return inventoryMapper.getMaterialNumberByName(materialName);
    }
    @Override
    public Integer getEquipmentNumberByName(String equipmentName){

        return inventoryMapper.getEquipNumberByName(equipmentName);
    }
    @Override
    public Integer getProcessNumberByName(String processName){

        return inventoryMapper.getProcessNumberByName(processName);
    }
}
