package com.job.feign.clients;


import com.job.feign.pojo.Equipment;
import com.job.feign.pojo.Material;
import com.job.feign.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-18:27
 * @description
 */

@FeignClient(value = "PRODUCTIONMANAGEMENTSERVICE")
public interface ProductionManagementClient {

    /**
     * Dispatch-Service 调用 Production-Management-Service的queryMaterials方法
     * @return
     */
    @GetMapping("/productionManagement/material/queryMaterials")
    List<Material>  queryMaterials();

    @GetMapping("/productionManagement/equipment/queryEquipmentTypes")
    List<String> queryEquipmentTypes();

    @GetMapping("/productionManagement/equipment/queryEquipmentsByType/{functionName}")
    List<Equipment> queryEquipmentsByType(@PathVariable("functionName") String functionName);


    @GetMapping("/productionManagement/equipment/queryEquipments")
    List<Equipment> queryEquipments();

    /**
     * 根据原材料名称查询原材料实体
     * @param materialName
     * @return
     */
    @PostMapping("/productionManagement/queryMaterialByName")
    public Result queryMaterialByName(@RequestParam String materialName);

}
