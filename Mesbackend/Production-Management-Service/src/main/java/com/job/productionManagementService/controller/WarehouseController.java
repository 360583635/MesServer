package com.job.productionManagementService.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.MaterialService;
import com.job.productionManagementService.service.WarehouseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public Result saveWarehouse(@RequestBody Warehouse tWarehouse, HttpServletRequest httpServletRequest) {

        long warehouseNumber = warehouseService.count();
        if (warehouseNumber < 10) {
            boolean save = warehouseService.save(tWarehouse);
            if (save) {
                return Result.success(null, "保存成功");
            }
            return Result.error("保存失败");
        }
            return Result.error("仓库个数已满无法创建新仓库");
    }




    /**
     * 原材料入库
     * @return
     */
    @PostMapping("/MaterialStockin")
    @ResponseBody
    public Result MaterialStockin(HttpServletRequest httpServletRequest ,@RequestBody int materialNumber,@RequestBody String materialName){

        int number = 0;

        LambdaQueryWrapper<Warehouse> queryWrapper= new LambdaQueryWrapper<>();
        List<Warehouse> warehouseList = (List<Warehouse>) queryWrapper
               .select(Warehouse::getWarehouseAvailable,Warehouse::getWarehouseId)
               .ge(Warehouse::getWarehouseAvailable,0);
        if(warehouseList.size()<=0){
            return Result.error("无可用仓库");
        }


        LambdaQueryWrapper<Material> queryMaterialArea= new LambdaQueryWrapper<>();
        queryMaterialArea
                .select(Material::getMaterialArea)
                .eq(Material::getMaterialName,materialName);
        Float materialArea = materialService.getOne(queryMaterialArea).getMaterialArea();

//        LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        inventoryLambdaQueryWrapper
//                .eq(Inventory::getMaterialName,materialName)

        int len = warehouseList.size();

        for(int i=0;i<len;i++){
            if(number<materialNumber){
                LambdaQueryWrapper<Warehouse> queryWarehouseArea = new LambdaQueryWrapper<>();
                queryWarehouseArea
                        .select(Warehouse::getWarehouseAvailable).eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId());
                Float warehouseArea = warehouseService.getOne(queryWarehouseArea).getWarehouseArea();
                int maxNumber = (int) (warehouseArea/materialArea);//获取最大可存储的个数

                if(maxNumber >= materialNumber){

                }

                number=maxNumber;

            }{
                break;
            }
        }






    }




