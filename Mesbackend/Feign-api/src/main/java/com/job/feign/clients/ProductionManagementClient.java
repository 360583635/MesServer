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
    List<Material>  queryMaterials(@RequestHeader("token") String token);

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
    @GetMapping("/productionManagement/inventory/queryNumbersByMaterialName")
    Integer queryMaterialNumberByMaterialName(@RequestHeader("token")String token,@RequestParam String materialName);
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
    Set<String> queryProduceName(@RequestHeader("token") String token);

    /**
     * 普通原材料出库
     * @param materials
     * @return
     */
    @PostMapping("/productionManagement/warehouse/MaterialStockOut")
    @ResponseBody
     Result MaterialStockOut( @RequestParam String materials);

    @PostMapping("/productionManagement/inventory/queryNumbersBySaveWarehouse")
    Integer queryMaterialNumberBySaveWarehouse(@RequestHeader("token")String token,@RequestParam String materialName);

    /**
     * 加急订单出库方式
     * @param materials
     * @return
     */
    @PostMapping("/productionManagement/warehouse/MaterialStockOutPlus")
    @ResponseBody
    Result MaterialStockOutPlus( @RequestParam String materials);

    @PostMapping("/productionManagement/equipment/updateEquipmentStatus")
    public Boolean updateEquipmentStatus(@RequestParam String equipmentId);

    @PostMapping("/productionManagement/equipment/queryEquipmentByFunction")
    List<Equipment>queryEquipmentByFunction(@RequestParam String functionName);
}
