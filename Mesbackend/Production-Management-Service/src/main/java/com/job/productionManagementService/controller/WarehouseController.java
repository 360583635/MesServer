package com.job.productionManagementService.controller;


import com.job.common.pojo.Inventory;
import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.WarehouseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public class  WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private InventoryService inventoryService;
    @Resource

    /**
     *
     * 增加仓库
     * @param tWarehouse
     * @return
     */
    @PostMapping("/saveWarehouse")
    @ResponseBody
    public Result saveWarehouse(@RequestBody Warehouse tWarehouse, HttpServletRequest httpServletRequest) {
        boolean save = warehouseService.save(tWarehouse);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }

    /**
     * 原材料入库
     * @return
     */
    @PostMapping("/MaterialStockin")
    @RequestBody
    public Result MaterialStockin(@RequestBody Inventory tInverntory ){

    }


    }


