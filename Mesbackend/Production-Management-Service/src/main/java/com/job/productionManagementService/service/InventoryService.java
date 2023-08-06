package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Inventory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface InventoryService extends IService<Inventory> {
@Select("SELECT number FROM t_inventory Where material_name =#{material_name}")
Integer getMaterialNumberByName(@Param("materialName") String name);


    Integer getEquipmentNumberByName(String equipmentName);

    Integer getProcessNumberByName(String processName);
}
