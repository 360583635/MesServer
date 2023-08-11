package com.job.productionManagementService.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.*;
import com.job.common.result.Result;
import com.job.productionManagementService.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author 猫
 * @create 2023-07-17-18:45
 * @description
 */
@RestController
@RequestMapping("/productionManagement/warehouse")

public class  WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private InventoryService inventoryService;
    @Resource
    private MaterialService materialService;
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private ProduceService produceService;
    /**
     * 查询所有设备
     */
    @PostMapping("/queryEquipments")
    List<Equipment> queryEquipments(@RequestParam int warehouseId){
        LambdaQueryWrapper<Inventory>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseId,warehouseId);
         List<Inventory>insventoryList=inventoryService.list(queryWrapper);
          int size =insventoryList.size();
          List list=new ArrayList<>();
          for (int i= 0 ;i<size;i++) {
              LambdaQueryWrapper<Equipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
              lambdaQueryWrapper.eq(Equipment::getEquipmentName,insventoryList.get(i).getEquipmentName());
             list.add(equipmentService.list(lambdaQueryWrapper));
          }
          return list;
    }

    /**
     * 查询仓库所有产品
     * @param warehouseId
     * @return
     */
    @PostMapping("/queryProduce")
    List<Produce>queryProduce(@RequestParam int warehouseId){
        LambdaQueryWrapper<Inventory>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseId,warehouseId);
        List<Inventory>insventoryList=inventoryService.list(queryWrapper);
        int size =insventoryList.size();
        List list=new ArrayList<>();
        for (int i= 0 ;i<size;i++) {
            LambdaQueryWrapper<Produce> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Produce::getProduceName,insventoryList.get(i).getProduceName());
            System.out.println(produceService.list(lambdaQueryWrapper));
            list.add(produceService.list(lambdaQueryWrapper));
        }
        return list;
    }

    /**
     *
     * 增加仓库
     * @param tWarehouse
     * @return
     */
    @PostMapping("/saveWarehouse")
    @ResponseBody
    public Result saveWarehouse(@RequestBody Warehouse tWarehouse) {
        System.out.println(tWarehouse);

        long warehouseNumber = warehouseService.count();
        if (warehouseNumber < 35) {
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
       queryWrapper.select(Warehouse::getWarehouseId)
               .gt(Warehouse::getWarehouseAvailable,0)
               .eq(Warehouse::getWarehouseType,0)
               .eq(Warehouse::getWarehouseSave,0);
      List<Warehouse> list = warehouseService.list(queryWrapper);
      List warehouses = new ArrayList<>();
      for (Warehouse warehouse : list) {
          Map<Object, Object> map = new HashMap<>();
          map.put("value", warehouse.getWarehouseId());
          map.put("text", warehouse.getWarehouseId());
          warehouses.add(map);
      }
      return warehouses;
   }
    /**
     * 查询安全可用仓库
     */
    @PostMapping("/queryWarehouseByAreaSave")
    List<Warehouse> queryWarehouseByAreaSave(){
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Warehouse::getWarehouseId)
                .gt(Warehouse::getWarehouseAvailable,0)
                .eq(Warehouse::getWarehouseType,0)
                .eq(Warehouse::getWarehouseSave,1);
        List<Warehouse> list = warehouseService.list(queryWrapper);
        List warehouses = new ArrayList<>();
        for (Warehouse warehouse : list) {
            Map<Object, Object> map = new HashMap<>();
            map.put("value", warehouse.getWarehouseId());
            map.put("text", warehouse.getWarehouseId());
            warehouses.add(map);
        }
        return warehouses;
    }
    /**
     *仓库id默认值
     */
    @PostMapping("warehouseId")
    Integer warehouseId(){
        List<Warehouse>warehouseList=warehouseService.list();
        int size=warehouseList.size();
        return size+1;
    }
    /**
     * 根据仓库名称查询仓库（模糊查询）
     */
    @PostMapping("/warehousesByWarehouseName")
    List<Warehouse> warehousesByWarehouseName(@RequestParam String warehouseName) {
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Warehouse::getWarehouseName,warehouseName).eq(Warehouse::getWarehouseType,0).eq(Warehouse::getWarehouseSave,0);
        return  warehouseService.list(queryWrapper);
    }

    /**
     * 原材料入库
     * @return
     */
    @PostMapping("/MaterialStockIn")
    public Result MaterialStockIn(@RequestBody Map<String,String> map) {
        String materialName = map.get("materialName");
        Integer materialNumber = Integer.parseInt(map.get("materialNumber"));
      LambdaQueryWrapper<Warehouse> queryWrapper=new LambdaQueryWrapper<>();
      queryWrapper
              .gt(Warehouse::getWarehouseAvailable,20)
              .eq(Warehouse::getWarehouseType,0)
              .eq(Warehouse::getWarehouseSave,0);
     List<Warehouse>warehouseList=warehouseService.list(queryWrapper);
     //用List存入可用面积大于0以及仓库类型是原材料仓库的仓库列表
        LambdaQueryWrapper<Material>materialLambdaQueryWrapper=new LambdaQueryWrapper<>();
        materialLambdaQueryWrapper.eq(Material::getMaterialName,materialName);
        float maArea=materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
        int size =warehouseList.size();
        if (size==0){
            return Result.error("无可用仓库");
        }
        //获取入库原材料的面积
     for (int i =0 ;i<warehouseList.size();i++){
         /**
          *首先计算出第一个仓库可以存入的最大数量是多少
          */
         LambdaQueryWrapper<Warehouse>wrapper=new LambdaQueryWrapper<>();
         wrapper.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId());

         //取出List集合里的数据在仓库表里查询仓库表的可用面积
         float warehouseAbArea = warehouseService.getOne(wrapper).getWarehouseAvailable();

         int warehouseLayers =warehouseService.getOne(wrapper).getWarehouseLayers();

         //定义最大数量
         int maxNumber = (int) ((warehouseAbArea/maArea)*warehouseLayers);
         //如果第一个仓库可存储的最大数量已经大于所存储数量则直接存
         if(maxNumber>=materialNumber){
             //用查询出来的可用面积来减去原材料所占的面积算出入库后原材料的可用面积然后存储
             warehouseAbArea=warehouseAbArea-(maArea*((float) materialNumber /warehouseLayers));
             //存储
         LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
         warehouseLambdaUpdateWrapper
                 .eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId())
                 .set(Warehouse::getWarehouseAvailable,warehouseAbArea);
         warehouseService.update(warehouseLambdaUpdateWrapper);
         //更改库存表的数据
             LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
             inventoryLambdaQueryWrapper
                     .eq(Inventory::getMaterialName,materialName)
                     .eq(Inventory::getWarehouseId,warehouseList.get(i).getWarehouseId());

            //如果仓库里已经存过这个原材料则查出他的数据然后加上存入的数据
             Inventory inventory =inventoryService.getOne(inventoryLambdaQueryWrapper);
             if (inventory!=null&&inventory.getNumber()!=null) {
                 int materialNumbers = inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                 materialNumbers = materialNumber + materialNumbers;
                 LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                 inventoryLambdaUpdateWrapper
                         .eq(Inventory::getMaterialName, materialName)
                         .set(Inventory::getNumber, materialNumbers);
                 inventoryService.update(inventoryLambdaUpdateWrapper);
             }
             else {
                 Inventory inventory1= new Inventory();
                 inventory1.setNumber(materialNumber);
                 inventory1.setWarehouseType(0);
                 inventory1.setIsDelete(1);
                 inventory1.setWarehouseId(warehouseList.get(i).getWarehouseId());
                 inventory1.setMaterialName(materialName);
              inventoryService.save(inventory1);
             }
             return Result.success("null","入库成功");
         }
         //如果第一个仓库不能存入更多原材料了
         else {
             //就存入这个仓库最大可存储个数
             warehouseAbArea=0;
             LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
             warehouseLambdaUpdateWrapper
                     .eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId())
                     .set(Warehouse::getWarehouseAvailable,warehouseAbArea);
             warehouseService.update(warehouseLambdaUpdateWrapper);
             //数量也加进去
             LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
             inventoryLambdaQueryWrapper
                     .eq(Inventory::getMaterialName,materialName)
                     .eq(Inventory::getWarehouseId,warehouseList.get(i).getWarehouseId());
             //如果仓库里面原本有这个原材料
             Inventory inventory =inventoryService.getOne(inventoryLambdaQueryWrapper);
             if (inventory!=null&& inventory.getNumber()!=0) {
                 int materialNumbers = inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                 materialNumbers = maxNumber + materialNumbers;
                 LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                 inventoryLambdaUpdateWrapper
                         .eq(Inventory::getMaterialName, materialName)
                         .eq(Inventory::getWarehouseType,0)
                         .set(Inventory::getNumber, materialNumbers);
                 inventoryService.update(inventoryLambdaUpdateWrapper);
             }
             //如果仓库原本里面没有这个原材料就在库存表里初始化这个原材料数据
             else {
                 Inventory inventory1= new Inventory();
                 inventory1.setNumber(maxNumber);
                 inventory1.setWarehouseType(0);
                 inventory1.setIsDelete(1);
                 inventory1.setWarehouseId(warehouseList.get(i).getWarehouseId());
                 inventory1.setMaterialName(materialName);
                 inventory1.setSaveWarehouse(0);
                 inventoryService.save(inventory1);
             }
             materialNumber=(materialNumber-maxNumber);

         }
         if(i==warehouseList.size()-1){
             return Result.error("仓库已满还剩"+materialNumber+"没存");}
     }
     return Result.success("null","入库成功");
    }
    /**
     * 安全仓库原材料入库
     * @return
     */
    @PostMapping("/MaterialStockInPlus")
    public Result MaterialStockInPlus(@RequestBody Map<String,String> map) {
        String materialName = map.get("materialName");
        Integer materialNumber = Integer.parseInt(map.get("materialNumber"));
        LambdaQueryWrapper<Warehouse> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper
                .gt(Warehouse::getWarehouseAvailable,20)
                .eq(Warehouse::getWarehouseType,0)
                .eq(Warehouse::getWarehouseSave,1);
        List<Warehouse>warehouseList=warehouseService.list(queryWrapper);
        //用List存入可用面积大于0以及仓库类型是原材料仓库的仓库列表
        LambdaQueryWrapper<Material>materialLambdaQueryWrapper=new LambdaQueryWrapper<>();
        materialLambdaQueryWrapper.eq(Material::getMaterialName,materialName);
        float maArea=materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
        int size =warehouseList.size();
        if (size==0){
            return Result.error("无可用仓库");
        }
        //获取入库原材料的面积
        for (int i =0 ;i<warehouseList.size();i++){
            /**
             *首先计算出第一个仓库可以存入的最大数量是多少
             */
            LambdaQueryWrapper<Warehouse>wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId());

            //取出List集合里的数据在仓库表里查询仓库表的可用面积
            float warehouseAbArea = warehouseService.getOne(wrapper).getWarehouseAvailable();

            int warehouseLayers =warehouseService.getOne(wrapper).getWarehouseLayers();

            //定义最大数量
            int maxNumber = (int) ((warehouseAbArea/maArea)*warehouseLayers);
            //如果第一个仓库可存储的最大数量已经大于所存储数量则直接存
            if(maxNumber>=materialNumber){
                //用查询出来的可用面积来减去原材料所占的面积算出入库后原材料的可用面积然后存储
                warehouseAbArea=warehouseAbArea-(maArea*((float) materialNumber /warehouseLayers));
                //存储
                LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                warehouseLambdaUpdateWrapper
                        .eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId())
                        .set(Warehouse::getWarehouseAvailable,warehouseAbArea);
                warehouseService.update(warehouseLambdaUpdateWrapper);
                //更改库存表的数据
                LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
                inventoryLambdaQueryWrapper
                        .eq(Inventory::getMaterialName,materialName)
                        .eq(Inventory::getWarehouseId,warehouseList.get(i).getWarehouseId());

                //如果仓库里已经存过这个原材料则查出他的数据然后加上存入的数据
                Inventory inventory =inventoryService.getOne(inventoryLambdaQueryWrapper);
                if (inventory!=null&&inventory.getNumber()!=null) {
                    int materialNumbers = inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                    materialNumbers = materialNumber + materialNumbers;
                    LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getMaterialName, materialName)
                            .set(Inventory::getNumber, materialNumbers);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                }
                else {
                    Inventory inventory1= new Inventory();
                    inventory1.setNumber(materialNumber);
                    inventory1.setWarehouseType(0);
                    inventory1.setIsDelete(1);
                    inventory1.setWarehouseId(warehouseList.get(i).getWarehouseId());
                    inventory1.setMaterialName(materialName);
                    inventoryService.save(inventory1);
                }
                return Result.success("null","入库成功");
            }
            //如果第一个仓库不能存入更多原材料了
            else {
                //就存入这个仓库最大可存储个数
                warehouseAbArea=0;
                LambdaUpdateWrapper<Warehouse>warehouseLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                warehouseLambdaUpdateWrapper
                        .eq(Warehouse::getWarehouseId,warehouseList.get(i).getWarehouseId())
                        .set(Warehouse::getWarehouseAvailable,warehouseAbArea);
                warehouseService.update(warehouseLambdaUpdateWrapper);
                //数量也加进去
                LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
                inventoryLambdaQueryWrapper
                        .eq(Inventory::getMaterialName,materialName)
                        .eq(Inventory::getWarehouseId,warehouseList.get(i).getWarehouseId());
                //如果仓库里面原本有这个原材料
                Inventory inventory =inventoryService.getOne(inventoryLambdaQueryWrapper);
                if (inventory!=null&& inventory.getNumber()!=0) {
                    int materialNumbers = inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                    materialNumbers = maxNumber + materialNumbers;
                    LambdaUpdateWrapper<Inventory> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getMaterialName, materialName)
                            .eq(Inventory::getWarehouseType,0)
                            .set(Inventory::getNumber, materialNumbers);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                }
                //如果仓库原本里面没有这个原材料就在库存表里初始化这个原材料数据
                else {
                    Inventory inventory1= new Inventory();
                    inventory1.setNumber(maxNumber);
                    inventory1.setWarehouseType(0);
                    inventory1.setIsDelete(1);
                    inventory1.setWarehouseId(warehouseList.get(i).getWarehouseId());
                    inventory1.setMaterialName(materialName);
                    inventory1.setSaveWarehouse(0);
                    inventoryService.save(inventory1);
                }
                materialNumber=(materialNumber-maxNumber);

            }
            if(i==warehouseList.size()-1){
                return Result.error("仓库已满还剩"+materialNumber+"没存");}
        }
        return Result.success("null","入库成功");
    }
/**
 * 普通订单原材料出库
 */
@PostMapping("/MaterialStockOut")
@ResponseBody
    public Result MaterialStockOut(HttpServletRequest httpServletRequest , @RequestParam String materials){

        String[] material = materials.split(",");
       //逗号分隔符将数据分开
        if(material.length>0) {
            for (int i = 0; i < (material.length) / 2; i++) {
                LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Inventory::getMaterialName, material[i])
                        .eq(Inventory::getWarehouseType,0)
                        .eq(Inventory::getSaveWarehouse,0);
                //在库存表里查询存有这个原材料名称的集合
                List<Inventory> warehouseList= inventoryService.list(queryWrapper);
                LambdaQueryWrapper<Material>materialLambdaQueryWrapper=new LambdaQueryWrapper<>();
                materialLambdaQueryWrapper
                        .eq(Material::getMaterialName,material[i]);
                float maArea = materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
                int size =warehouseList.size();
                int outNumber= Integer.parseInt(material[i+1]);
            for (int j=0;j<size;j++){
                 LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
                 inventoryLambdaQueryWrapper
                         .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                         .eq(Inventory::getMaterialName,material[i]);
                 //查询这个仓库的这个原材料的库存
                int number =inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                //需要出库的数量
                //如果第一个仓库的库存足够出库
                if (number>=outNumber){
                    LambdaQueryWrapper<Warehouse>lambdaQueryWrapper=new LambdaQueryWrapper<>();
                    lambdaQueryWrapper
                            .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId());
                  float abArea = warehouseService.getOne(lambdaQueryWrapper).getWarehouseAvailable();
                  float layers = warehouseService.getOne(lambdaQueryWrapper).getWarehouseLayers();
                    //查询出来这个仓库的可用空间
                    abArea = abArea+((outNumber/layers)*maArea);
                    LambdaUpdateWrapper<Warehouse>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper
                            .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId())
                            .set(Warehouse::getWarehouseAvailable,abArea);
                    warehouseService.update(lambdaUpdateWrapper);
                    number=number-outNumber;
                    LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                            .eq(Inventory::getMaterialName,material[i])
                            .set(Inventory::getNumber,number);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                    return Result.success("null","出库成功");
                }
                //如果第一个仓库存量不足以出库所有原材料
                else {
                    LambdaQueryWrapper<Warehouse>lambdaQueryWrapper=new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId());
                    //查询可用空间
                    float abArea = warehouseService.getOne(lambdaQueryWrapper).getWarehouseAvailable();
                    //查询仓库层数
                    float layers = warehouseService.getOne(lambdaQueryWrapper).getWarehouseLayers();
                    //计算出来这个仓库的可用空间
                    abArea = abArea+((number/layers)*maArea);
                    LambdaUpdateWrapper<Warehouse>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper
                            .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId())
                            .set(Warehouse::getWarehouseAvailable,abArea);
                    warehouseService.update(lambdaUpdateWrapper);
                    LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    inventoryLambdaUpdateWrapper
                            .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                            .eq(Inventory::getMaterialName,material[i])
                            .set(Inventory::getNumber,0);
                    inventoryService.update(inventoryLambdaUpdateWrapper);
                   outNumber=outNumber-number;
                }
            }

            }
        }
        return null;
}
    /**
     * 加急订单原材料出库
     */
    @PostMapping("/MaterialStockOutPlus")
    @ResponseBody
    public Result MaterialStockOutPlus(HttpServletRequest httpServletRequest , @RequestParam String materials){

        String[] material = materials.split(",");
        //逗号分隔符将数据分开
        if(material.length>0) {
            for (int i = 0; i < (material.length) / 2; i++) {
                LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Inventory::getMaterialName, material[i])
                        .eq(Inventory::getWarehouseType,0);
                //在库存表里查询存有这个原材料名称的集合
                List<Inventory> warehouseList= inventoryService.list(queryWrapper);
                LambdaQueryWrapper<Material>materialLambdaQueryWrapper=new LambdaQueryWrapper<>();
                materialLambdaQueryWrapper
                        .eq(Material::getMaterialName,material[i]);
                float maArea = materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
                int size =warehouseList.size();
                int outNumber= Integer.parseInt(material[i+1]);
                for (int j=0;j<size;j++){
                    LambdaQueryWrapper<Inventory>inventoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
                    inventoryLambdaQueryWrapper
                            .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                            .eq(Inventory::getMaterialName,material[i]);
                    //查询这个仓库的这个原材料的库存
                    int number =inventoryService.getOne(inventoryLambdaQueryWrapper).getNumber();
                    //需要出库的数量
                    //如果第一个仓库的库存足够出库
                    if (number>=outNumber){
                        LambdaQueryWrapper<Warehouse>lambdaQueryWrapper=new LambdaQueryWrapper<>();
                        lambdaQueryWrapper
                                .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId());
                        float abArea = warehouseService.getOne(lambdaQueryWrapper).getWarehouseAvailable();
                        float layers = warehouseService.getOne(lambdaQueryWrapper).getWarehouseLayers();
                        //查询出来这个仓库的可用空间
                        abArea = abArea+((outNumber/layers)*maArea);
                        LambdaUpdateWrapper<Warehouse>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper
                                .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId())
                                .set(Warehouse::getWarehouseAvailable,abArea);
                        warehouseService.update(lambdaUpdateWrapper);
                        number=number-outNumber;
                        LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                        inventoryLambdaUpdateWrapper
                                .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                                .eq(Inventory::getMaterialName,material[i])
                                .set(Inventory::getNumber,number);
                        inventoryService.update(inventoryLambdaUpdateWrapper);
                        return Result.success("null","出库成功");
                    }
                    //如果第一个仓库存量不足以出库所有原材料
                    else {
                        LambdaQueryWrapper<Warehouse>lambdaQueryWrapper=new LambdaQueryWrapper<>();
                        lambdaQueryWrapper.eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId());
                        //查询可用空间
                        float abArea = warehouseService.getOne(lambdaQueryWrapper).getWarehouseAvailable();
                        //查询仓库层数
                        float layers = warehouseService.getOne(lambdaQueryWrapper).getWarehouseLayers();
                        //计算出来这个仓库的可用空间
                        abArea = abArea+((number/layers)*maArea);
                        LambdaUpdateWrapper<Warehouse>lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper
                                .eq(Warehouse::getWarehouseId,warehouseList.get(j).getWarehouseId())
                                .set(Warehouse::getWarehouseAvailable,abArea);
                        warehouseService.update(lambdaUpdateWrapper);
                        LambdaUpdateWrapper<Inventory>inventoryLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                        inventoryLambdaUpdateWrapper
                                .eq(Inventory::getWarehouseId,warehouseList.get(j).getWarehouseId())
                                .eq(Inventory::getMaterialName,material[i])
                                .set(Inventory::getNumber,0);
                        inventoryService.update(inventoryLambdaUpdateWrapper);
                        outNumber=outNumber-number;
                    }
                }

            }
        }
        return null;
    }
    /**
     * 更新仓库可用面积
     */
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
    for (int i=0;i<size;i++) {
        LambdaQueryWrapper<Warehouse> warehouseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        warehouseLambdaQueryWrapper.eq(Warehouse::getWarehouseId, warehouseId);
        //把所需要更新的仓库可用面积先查出来
        float abArea = warehouseService.getOne(warehouseLambdaQueryWrapper).getWarehouseAvailable();

        LambdaQueryWrapper<Warehouse> warehouseLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        warehouseLambdaQueryWrapper1.eq(Warehouse::getWarehouseId, warehouseId);

        LambdaQueryWrapper<Material> materialLambdaQueryWrapper = new LambdaQueryWrapper<>();
        materialLambdaQueryWrapper.eq(Material::getMaterialName, inventoryList.get(i).getMaterialName());
        //可用面积
        Material material =materialService.getOne(materialLambdaQueryWrapper);
        if (material!=null) {
            float maArea = materialService.getOne(materialLambdaQueryWrapper).getMaterialArea();
            //总面积
            float suArea = warehouseService.getOne(warehouseLambdaQueryWrapper1).getWarehouseArea();

            LambdaQueryWrapper<Inventory> inventoryLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            inventoryLambdaQueryWrapper1
                    .eq(Inventory::getMaterialName, inventoryList.get(i).getMaterialName())
                    .eq(Inventory::getWarehouseId, warehouseId);
            int number = inventoryService.getOne(inventoryLambdaQueryWrapper1).getNumber();

            if (i == 0) {
                abArea = suArea - (maArea * ((float) number / 5));
            } else {
                abArea = abArea - (maArea * ((float) number / 5));
            }
            LambdaUpdateWrapper<Warehouse> inventoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            inventoryLambdaUpdateWrapper
                    .eq(Warehouse::getWarehouseId, inventoryList.get(i).getWarehouseId())
                    .set(Warehouse::getWarehouseAvailable, abArea);
            warehouseService.update(inventoryLambdaUpdateWrapper);
        }

    }
    return Result.success(null,"更新成功");
}

    /**
     * 查询原材料普通仓库
     * @return
     */
    @PostMapping("/queryMaterialWarehouseByWarehouseType")
     List<Warehouse>queryWarehouseByWarehouseType(){
    LambdaQueryWrapper<Warehouse> lambdaQueryWrapper =new LambdaQueryWrapper<>();
    lambdaQueryWrapper
            .eq(Warehouse::getWarehouseType,0);
    return warehouseService.list(lambdaQueryWrapper);
}

    /**
     * 查询原材料仓库详情
     * @return
     */
    @PostMapping("/queryMaterialWarehouseByWarehouseId")
    List<Warehouse>queryWarehouseByWarehouseId(@RequestParam int warehouseId){
        LambdaQueryWrapper<Warehouse> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Warehouse::getWarehouseId,warehouseId);
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
    /**
     * 修改仓库数据
     */
    @PostMapping("/updateWarehouse")
    public Result updateWarehouse(@RequestBody Warehouse twarehouse) {
        warehouseService.updateById(twarehouse);
        return Result.success("null","恭喜你修改成功");
    }

}




