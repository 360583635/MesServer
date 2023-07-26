package com.job.productionManagementService.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import com.job.productionManagementService.mapper.MaterialMapper;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.MaterialService;
import com.job.productionManagementService.service.WarehouseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
       queryWrapper.select(Warehouse::getWarehouseId).gt(Warehouse::getWarehouseAvailable,0);
      List<Warehouse> list = warehouseService.list(queryWrapper);
      List warehouses = new ArrayList<>();
      for (Warehouse warehouse : list) {
          Map<Object, Object> map = new HashMap<>();
          map.put("value", warehouse.toString());
          map.put("text", warehouse.getWarehouseId());
          warehouses.add(map);
      }
      return warehouses;
   }

   @Autowired
   private MaterialMapper materialMapper;

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
        //封装根据仓库id来查询仓库表
        LambdaUpdateWrapper<Warehouse> warehouseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //根据仓库id查询仓库面积
        LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        inventoryLambdaQueryWrapper.eq(Inventory::getWarehouseId, warehouseId).eq(Inventory::getWarehouseType,warehouseType);
        //封装根据仓库id和仓库类型来查询库存表
        List<Inventory> materiallist = inventoryService.list(inventoryLambdaQueryWrapper);
        //根据这个仓库来查询原材料的种类的集合
        LambdaQueryWrapper<Material> materialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //遍历原材料种类来算出可用面积
        for (int i = 0; i < materiallist.size(); i++) {
            int materialNumbers = materiallist.get(i).getNumber();
            materialLambdaQueryWrapper.eq(Material::getMaterialName, materiallist.get(i).getMaterialName());
            //根据存在这个仓库的原材料名称去原材料表里查询数据原材料所占面积
            float area = materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
            //通过仓库id和仓库类型查询仓库可用面积
            float wareAbArea = warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();

            wareAbArea = (wareAbArea-(materialNumber*area));
            //计算出仓库可用面积并更新
            warehouseLambdaUpdateWrapper.eq(Warehouse::getWarehouseId, warehouseId).set(Warehouse::getWarehouseAvailable, wareAbArea);
            warehouseService.update(warehouseLambdaUpdateWrapper);
        }
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
          queryWrapper.select(Warehouse::getWarehouseAvailable, Warehouse::getWarehouseId)
                  .gt(Warehouse::getWarehouseAvailable, 0);
        //查询仓库可用面积大于0的面积数和仓库id
          List<Warehouse> warehouseList= warehouseService.list(queryWrapper);
        LambdaQueryWrapper<Material> queryMaterialArea = new LambdaQueryWrapper<>();
        queryMaterialArea
                .eq(Material::getMaterialName, materialName);
        //查询原材料的所占面积
        Float materialArea = materialService.getOne(queryMaterialArea).getMaterialArea();
        //仓库id和名字所匹配

        int len = warehouseList.size();
        for (int i = 0;i < len;i++) {
            if (materialNumber > 0) {
                LambdaQueryWrapper<Warehouse> queryWarehouseArea = new LambdaQueryWrapper<>();
                queryWarehouseArea
                        .eq(Warehouse::getWarehouseId, warehouseList.get(i).getWarehouseId());
                LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper1 =new LambdaQueryWrapper<>();
                inventoryLambdaQueryWrapper1.eq(Inventory::getMaterialName,materialName).eq(Inventory::getWarehouseId,warehouseId);

                Inventory inventory = inventoryService.getOne(inventoryLambdaQueryWrapper1);
                int materialNumbers = inventory.getNumber();
                System.out.println(inventoryService.getOne(inventoryLambdaQueryWrapper1));
                Float warehouseArea = warehouseService.getOne(queryWarehouseArea).getWarehouseAvailable();//获取更新后的可用面积
                //获取仓库层数
                int layers = warehouseService.getOne(queryWarehouseArea).getWarehouseLayers();

                int maxNumber = (int) ((warehouseArea/materialArea) * layers);//获取最大可存储的个数

                if (maxNumber >= materialNumber) {
                    materialNumbers=materialNumbers+materialNumber;
                    LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getMaterialName, materialName).set(Inventory::getNumber,materialNumbers);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                     LambdaQueryWrapper<Warehouse> queryWrapper2 =new LambdaQueryWrapper<>();
                     queryWrapper2.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId());
                    Float WarehouseAbArea =warehouseService.getOne(queryWrapper2).getWarehouseAvailable();
                    WarehouseAbArea=(WarehouseAbArea-(materialArea*materialNumber));
                    LambdaUpdateWrapper<Warehouse> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,WarehouseAbArea);
                    warehouseService.update(wrapper);
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
    public Result MaterialStockOut(HttpServletRequest httpServletRequest , @RequestParam String materialName, @RequestParam int materialNumber,@RequestParam String warehouseId){

    LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Inventory::getMaterialName,materialName);
    LambdaQueryWrapper<Material>materialLambdaQueryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Inventory::getMaterialName,materialName);
    //通过原材料名称查询库存表
    LambdaQueryWrapper<Warehouse> queryWrapper1 = new LambdaQueryWrapper<>();
    LambdaUpdateWrapper<Inventory> queryUpdateWrapper = new LambdaUpdateWrapper<>();
    LambdaUpdateWrapper<Warehouse>queryWrapper2 = new LambdaUpdateWrapper<>();
    for (int i=0;i<inventoryService.list(queryWrapper).size();i++){

        if (materialNumber<(inventoryService.list(queryWrapper).get(i).getNumber())){
            queryWrapper.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            int number =inventoryService.getOne(queryWrapper).getNumber();
            number=number-materialNumber;
            queryUpdateWrapper.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Inventory::getNumber,number);
            inventoryService.update(queryUpdateWrapper);
            queryWrapper1.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            inventoryService.update(queryUpdateWrapper);
            float abArea= warehouseService.getOne(queryWrapper1).getWarehouseAvailable();
            float maArea =materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
            abArea =abArea-(maArea*materialNumber);
            queryWrapper2.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,abArea);
            warehouseService.update(queryWrapper2);
            break;
        }
        else {
            LambdaQueryWrapper<Material> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Material::getMaterialName,materialName);
            float materialArea =materialService.getOne(lambdaQueryWrapper).getMaterialArea();
            LambdaQueryWrapper<Inventory> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            int number =inventoryService.getOne(lambdaQueryWrapper1).getNumber();
            LambdaQueryWrapper<Warehouse>warehouseLambdaQueryWrapper=new LambdaQueryWrapper<>();
            warehouseLambdaQueryWrapper.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            float abArea =warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();
            abArea=(abArea-materialArea*materialNumber);

            LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            inventoryLambdaUpdateWrapper.eq(Inventory::getMaterialName,materialName).eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Inventory::getNumber,0);
            inventoryService.update(inventoryLambdaUpdateWrapper);
            LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            warehouseLambdaUpdateWrapper.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,abArea);
            warehouseService.update(warehouseLambdaUpdateWrapper);
        }


    }

return Result.success("null","出库成功");

}

}




