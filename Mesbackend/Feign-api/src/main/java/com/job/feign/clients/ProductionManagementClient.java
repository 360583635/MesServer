package com.job.feign.clients;


import com.job.feign.pojo.Equipment;
import com.job.feign.pojo.Material;
import com.job.feign.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-18:27
 * @description
 */

@FeignClient(value = "PRODUCTIONMANAGEMENTSERVICE",url = "http://localhost:6041")
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
    @PostMapping("/productionManagement/material/queryMaterialByName")
    public Result queryMaterialByName(@RequestParam String materialName);

    /**
     * 根据原材料名称查询总数量
     * @param materialName
     * @return
     */
    @PostMapping("/productionManagement/produce/queryMaterialNumberByMaterialName")
    Integer queryMaterialNumberByMaterialName(String materialName);
    /**
     * 根据产品名称查询产品id
     * @param produceName
     * @return
     */
    @RequestMapping("/productionManagement/produce/queryProduceIdByProduceName")
    Integer queryProduceIdByProduceName(@RequestParam String produceName);

    @PostMapping("/productionManagement/equipment/queryEquipmentById")
    @ResponseBody
     Equipment queryEquipmentById(@RequestParam String id);
    /**
     * 查询所有产品名称(wen)
     */
    @GetMapping("/productionManagement/produce/queryProduceName")
    Set<String> queryProduceName();

    /**
     * 原材料出库
     * @param materials
     * @return
     */
    @PostMapping("/productionManagement/warehouse/MaterialStockOut")
    @ResponseBody
     Result MaterialStockOut( @RequestParam String materials);
}
