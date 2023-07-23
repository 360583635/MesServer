package com.job.productionManagementService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {


    Integer getMaterialNumberByName(String materialName);

    Integer getEquipNumberByName(String equipmentName);

    Integer getProcessNumberByName(String processName);

    String getMaterialNameByWarehouseType(int WarehouseType);
}
