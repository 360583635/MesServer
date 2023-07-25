package com.job.productionManagementService.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.MaterialService;
import com.job.productionManagementService.service.WarehouseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/warehouse")
public class  WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private InventoryService inventoryService;
    @Resource
    private MaterialService materialService;
    /**
     *
     * 增加仓库
     * @param tWarehouse
     * @return
     */
    @PostMapping("/saveWarehouse")
    @ResponseBody
    public Result saveWarehouse(@RequestBody Warehouse tWarehouse) {

        long warehouseNumber = warehouseService.count();
        if (warehouseNumber < 15) {
            boolean save = warehouseService.save(tWarehouse);
            if (save) {
                return Result.success(null, "保存成功");
            }
            return Result.error("保存失败");
        }
            return Result.error("仓库个数已满无法创建新仓库");
    }


  /**
  * 查询可用仓库
  */@PostMapping("/queryWarehouseByArea")
   List<Warehouse> queryWarehouseByArea(){
       LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.select(Warehouse::getWarehouseId).ge(Warehouse::getWarehouseAvailable,0);
       return warehouseService.list(queryWrapper);
   }
    /**
     * 原材料入库
     * @return
     */
    @PostMapping("/MaterialStockIn")
    @ResponseBody
    public Result MaterialStockin(HttpServletRequest httpServletRequest ,@RequestParam int materialNumber,@RequestParam String materialName,@RequestParam String warehouseId ,@RequestParam int warehouseType) {

        int number = 0;
        LambdaQueryWrapper<Warehouse> warehouseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        warehouseLambdaQueryWrapper.eq(Warehouse::getWarehouseId, warehouseId);
        LambdaUpdateWrapper<Warehouse> warehouseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();

        float wareArea = warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseArea();
        LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        inventoryLambdaQueryWrapper.eq(Inventory::getWarehouseId, warehouseId).eq(Inventory::getWarehouseType, warehouseType);
        List<Inventory> materiallist = inventoryService.list();
        LambdaQueryWrapper<Material> materialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (int i = 0; i < materiallist.size(); i++) {
            int materialNumbers = materiallist.get(i).getNumber();
            materialLambdaQueryWrapper.eq(Material::getMaterialName, materiallist.get(i).getMaterialName());
            float area = materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
            float wareAbArea = warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();
            wareAbArea = wareArea - (materialNumbers * area);
            warehouseLambdaUpdateWrapper.eq(Warehouse::getWarehouseId, warehouseId).set(Warehouse::getWarehouseAvailable, wareAbArea);
            warehouseService.update(warehouseLambdaUpdateWrapper);
        }

        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        List<Warehouse> warehouseList = (List<Warehouse>) queryWrapper
                .select(Warehouse::getWarehouseAvailable, Warehouse::getWarehouseId)
                .ge(Warehouse::getWarehouseAvailable, 0);
        if (warehouseList.size() <= 0) {
            return Result.error("无可用仓库");
        }

        LambdaQueryWrapper<Material> queryMaterialArea = new LambdaQueryWrapper<>();
        queryMaterialArea
                .select(Material::getMaterialArea)
                .eq(Material::getMaterialName, materialName);
        Float materialArea = materialService.getOne(queryMaterialArea).getMaterialArea();

        LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        inventoryLambdaUpdateWrapper
                .eq(Inventory::getMaterialName, materialName).setSql("number=number+{}");


        int len = warehouseList.size();
        for (int i = 0;i < len;i++) {
            if (materialNumber > 0) {
                LambdaQueryWrapper<Warehouse> queryWarehouseArea = new LambdaQueryWrapper<>();
                queryWarehouseArea
                        .select(Warehouse::getWarehouseAvailable, Warehouse::getWarehouseLayers).eq(Warehouse::getWarehouseId, warehouseList.get(i).getWarehouseId());

                Float warehouseArea = warehouseService.getOne(queryWarehouseArea).getWarehouseAvailable();
                int layers = warehouseService.getOne(queryWrapper).getWarehouseLayers();
                int maxNumber = (int) ((warehouseArea / materialArea) * layers);//获取最大可存储的个数

                if (maxNumber >= materialNumber) {
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getMaterialName, materialName).setSql("number=number+{materialNUmber}");
                }
                else {materialNumber = (materialNumber - maxNumber);}
            }
            else {
             break;
            }
        }
        return Result.success("null", "入库成功");
    }

/**
 * 原材料出库
 */
@PostMapping("/MaterialStockout")
@ResponseBody
    public Result MaterialStockOut(HttpServletRequest httpServletRequest , @RequestParam String materialName, @RequestParam int materNumber,RequestBody warehouseId){

    LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Inventory::getMaterialName,materialName);
    LambdaUpdateWrapper<Warehouse> queryWrapper1 = new LambdaUpdateWrapper<>();
    LambdaUpdateWrapper<Inventory> queryUpdateWrapper = new LambdaUpdateWrapper<>();
    LambdaQueryWrapper<Material>queryWrapper2 = new LambdaQueryWrapper<>();
    for (int i=0;i<inventoryService.list(queryWrapper).size();i++){
        if (materNumber<(inventoryService.list(queryWrapper).get(i).getNumber())){
            queryUpdateWrapper.eq(Inventory::getMaterialName,inventoryService.list(queryWrapper).get(i).getMaterialName()).setSql("number=number-{materialNumber}");
            queryWrapper2.eq(Material::getMaterialName,inventoryService.list(queryWrapper).get(i).getMaterialName());
            float materialArea= materialService.getOne(queryWrapper2).getMaterialArea();
            queryWrapper1.eq(Warehouse::getWarehouseId,warehouseId).setSql("warehouse_available+{materialArea*materNumber}");
            break;
        }
        else {
            queryUpdateWrapper.eq(Inventory::getEquipmentName,inventoryService.list(queryWrapper).get(i).getMaterialName()).setSql("number=0");
            queryWrapper2.eq(Material::getMaterialName,inventoryService.list(queryWrapper).get(i).getMaterialName());
            float materialArea= materialService.getOne(queryWrapper2).getMaterialArea();
            queryWrapper1.eq(Warehouse::getWarehouseId,warehouseId).setSql("warehouse_available+{materialArea*number}");
        }

    }

return Result.success("null","出库成功");

}

}




