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
        if (warehouseNumber < 30) {
            LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Warehouse::getWarehouseId, tWarehouse.getWarehouseId());
            LambdaUpdateWrapper<Warehouse> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Warehouse::getWarehouseName, tWarehouse.getWarehouseName());
            warehouseService.getOne(queryWrapper);
            warehouseService.getOne(lambdaUpdateWrapper);
            if (warehouseService.getOne(queryWrapper) == null && warehouseService.getOne(lambdaUpdateWrapper) == null) {
                warehouseService.save(tWarehouse);
                return Result.success(null, "保存成功");
            } else {
                return Result.error("保存失败");
            }
        }
        return null;
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

    /**
     * 原材料入库
     * @return
     */
    @PostMapping("/MaterialStockIn")
    @ResponseBody
    public Result MaterialStockIn(HttpServletRequest httpServletRequest ,@RequestParam int materialNumber,@RequestParam String materialName,@RequestParam String warehouseId ,@RequestParam int warehouseType) {

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

            wareAbArea = (wareAbArea-(((float) materialNumbers /5)*area));
            //计算出仓库可用面积并更新
            warehouseLambdaUpdateWrapper.eq(Warehouse::getWarehouseId, warehouseId).set(Warehouse::getWarehouseAvailable, wareAbArea);
            warehouseService.update(warehouseLambdaUpdateWrapper);
        }

        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
          queryWrapper.select(Warehouse::getWarehouseAvailable,Warehouse::getWarehouseId)
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
                //根据仓库id查询可用面积大于0的仓库可用面积多少
                LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper1 =new LambdaQueryWrapper<>();
                inventoryLambdaQueryWrapper1.eq(Inventory::getMaterialName,materialName).eq(Inventory::getWarehouseId,warehouseId);

                Inventory inventory = inventoryService.getOne(inventoryLambdaQueryWrapper1);
                int materialNumbers = inventory.getNumber();
                Float warehouseArea = warehouseService.getOne(queryWarehouseArea).getWarehouseAvailable();//获取更新后的可用面积
                //获取仓库层数
                int layers = warehouseService.getOne(queryWarehouseArea).getWarehouseLayers();

                int maxNumber = (int) ((warehouseArea/materialArea) * layers);//获取最大可存储的个数

                if (maxNumber>=materialNumber) {
                    materialNumbers=(materialNumbers+materialNumber);
                    LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getMaterialName, materialName).eq(Inventory::getWarehouseId,warehouseId).set(Inventory::getNumber,materialNumbers);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                     LambdaQueryWrapper<Warehouse> queryWrapper2 =new LambdaQueryWrapper<>();
                     queryWrapper2.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId());

                    Float WarehouseAbArea =warehouseService.getOne(queryWrapper2).getWarehouseAvailable();
                    WarehouseAbArea=(WarehouseAbArea-(materialArea*materialNumber));
                    if (WarehouseAbArea<0){
                        return Result.error("仓库已经吃饱啦吃不下了");
                    }
                    LambdaUpdateWrapper<Warehouse> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,WarehouseAbArea);
                    warehouseService.update(wrapper);

                    break;
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
@PostMapping("/MaterialStockOut")
@ResponseBody
    public Result MaterialStockOut(HttpServletRequest httpServletRequest , @RequestParam String materialName, @RequestParam int materialNumber){

    LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Inventory::getMaterialName,materialName);
   LambdaQueryWrapper<Inventory> lambdaQueryWrapper=new LambdaQueryWrapper<>();
    LambdaQueryWrapper<Material>materialLambdaQueryWrapper = new LambdaQueryWrapper<>();
    materialLambdaQueryWrapper.eq(Material::getMaterialName,materialName);
    //查询原材料的可用面积
    //通过原材料名称查询库存表
    LambdaQueryWrapper<Warehouse> queryWrapper1 = new LambdaQueryWrapper<>();
    LambdaUpdateWrapper<Inventory> queryUpdateWrapper = new LambdaUpdateWrapper<>();
    LambdaUpdateWrapper<Warehouse>queryWrapper2 = new LambdaUpdateWrapper<>();
    int size =inventoryService.list(queryWrapper).size();
    for (int i=0;i<size;i++){

        if (materialNumber<(inventoryService.list(queryWrapper).get(i).getNumber())){
            lambdaQueryWrapper.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            int number =inventoryService.getOne(lambdaQueryWrapper).getNumber();
            number=number-materialNumber;
            queryUpdateWrapper.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Inventory::getNumber,number);
            inventoryService.update(queryUpdateWrapper);
            queryWrapper1.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            float abArea= warehouseService.getOne(queryWrapper1).getWarehouseAvailable();
            float maArea =materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
            abArea =abArea+(maArea*((float) materialNumber /5));
            queryWrapper2.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,abArea);
            warehouseService.update(queryWrapper2);
            break;
        }
        else {
            LambdaQueryWrapper<Material> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper3.eq(Material::getMaterialName,materialName);
            float materialArea =materialService.getOne(lambdaQueryWrapper3).getMaterialArea();
            LambdaQueryWrapper<Inventory> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            int number =inventoryService.getOne(lambdaQueryWrapper1).getNumber();
            LambdaQueryWrapper<Warehouse>warehouseLambdaQueryWrapper=new LambdaQueryWrapper<>();
            warehouseLambdaQueryWrapper.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId());
            float abArea =warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();
            abArea=(abArea+(materialArea*(number/5)));

            LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            inventoryLambdaUpdateWrapper.eq(Inventory::getMaterialName,materialName).eq(Inventory::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Inventory::getNumber,0);
            inventoryService.update(inventoryLambdaUpdateWrapper);
            LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            warehouseLambdaUpdateWrapper.eq(Warehouse::getWarehouseId,inventoryService.list(queryWrapper).get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,abArea);
            warehouseService.update(warehouseLambdaUpdateWrapper);
            materialNumber=materialNumber-number;
        }


    }

return Result.success("null","出库成功");

/**
 * 更新仓库可用面积
 */
}
@PostMapping("updateWarehouseAbArea")
@ResponseBody
public Result updateWarehouseAbArea(@RequestParam int warehouseId,HttpServletRequest httpServletRequest){

    LambdaQueryWrapper<Warehouse>queryWrapper =new LambdaQueryWrapper<>();
    queryWrapper.eq(Warehouse::getWarehouseId,warehouseId);
    //根据仓库id查询仓库
    LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper =new LambdaQueryWrapper<>();
    inventoryLambdaQueryWrapper.eq(Inventory::getWarehouseId,warehouseId);

    List<Inventory>inventoryList =inventoryService.list(inventoryLambdaQueryWrapper);
    int size = inventoryList.size();
    for (int i=0;i<size;i++){
        LambdaQueryWrapper<Warehouse>warehouseLambdaQueryWrapper =new LambdaQueryWrapper<>();
        warehouseLambdaQueryWrapper.eq(Warehouse::getWarehouseId,warehouseId);
        float abArea =warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();
        LambdaQueryWrapper<Warehouse>warehouseLambdaQueryWrapper1=new LambdaQueryWrapper<>();
             warehouseLambdaQueryWrapper1.eq(Warehouse::getWarehouseId,warehouseId);
        LambdaQueryWrapper<Material>materialLambdaQueryWrapper =new LambdaQueryWrapper<>();
        materialLambdaQueryWrapper.eq(Material::getMaterialName,inventoryList.get(i).getMaterialName());
        float maArea =materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();

        float suArea =warehouseService.getOne(warehouseLambdaQueryWrapper1).getWarehouseArea();

        LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper1=new LambdaQueryWrapper<>();
                        inventoryLambdaQueryWrapper1.eq(Inventory::getMaterialName,inventoryList.get(i).getMaterialName()).eq(Inventory::getWarehouseId,warehouseId);
        int number =inventoryService.getOne(inventoryLambdaQueryWrapper1).getNumber();

        if (i==0) {
            abArea = suArea - (maArea * ((float) number / 5));
        }
        else {
            abArea = abArea- (maArea * ((float) number / 5));
        }
        LambdaUpdateWrapper<Warehouse>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        inventoryLambdaUpdateWrapper.eq(Warehouse::getWarehouseId,inventoryList.get(i).getWarehouseId()).set(Warehouse::getWarehouseAvailable,abArea);
        warehouseService.update(inventoryLambdaUpdateWrapper);
    }
    return Result.success(null,"更新成功");
}

    /**
     * 查询原材料仓库
     * @return
     */
    @PostMapping("/queryMaterialWarehouseByWarehouseType")
     List<Warehouse>queryWarehouseByWarehouseType(){
    LambdaQueryWrapper<Warehouse> lambdaQueryWrapper =new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(Warehouse::getWarehouseType,0);
    return warehouseService.list(lambdaQueryWrapper);
}
    /**
     *通过仓库id查询库存信息
     * @return
     */
    @PostMapping("/queryMaterialByWarehouseId")
    List<Inventory> queryMaterialByWarehouseId(@RequestParam int warehouseId){
            LambdaQueryWrapper<Inventory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Inventory::getWarehouseId,warehouseId);
            return  inventoryService.list(lambdaQueryWrapper);
    }

    /**
     * 查询设备仓库
     * @return
     */
    @PostMapping("queryEquipmentWarehouseByWarehouseType")
    List<Warehouse>queryEquipmentWarehouseByWarehouseType(){
        LambdaQueryWrapper<Warehouse> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Warehouse::getWarehouseType,1);
        return warehouseService.list(lambdaQueryWrapper);
    }
    /**
     * 查询产品仓库
     */
    @PostMapping("queryProduceWarehouseByWarehouseType")
    List<Warehouse>queryProduceWarehouseByWarehouseType(){
        LambdaQueryWrapper<Warehouse> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Warehouse::getWarehouseType,2);
        return warehouseService.list(lambdaQueryWrapper);
    }

}




