package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Produce;
import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import com.job.productionManagementService.mapper.ProduceMapper;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.ProduceService;
import com.job.productionManagementService.service.WarehouseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productionManagement/produce")
@Component
@RestController
/**
 * @author 猫
 * @create 2023-07-17-18:45
 * @description
 */
public class ProduceController {

    @Resource
    private InventoryService inventoryService ;

    @Resource
    private ProduceService produceService ;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private ProduceMapper produceMapper;
    /**
     *
     * 添加新产品
     */
   @RequestMapping("/addProduce")
    @ResponseBody
    public Result saveProduce(@RequestBody Produce tProdece){

       boolean save =produceService.save(tProdece);

       if (save){
           return Result.success(null,"添加成功");
       }
       else {
           return Result.error("添加失败");
       }

   }
    /**
     * 初始化产品添加
     * @return
     */
    @PostMapping("/initializationProduce")
    public Result initializationProduce(@RequestParam String produceName,int warehouseId){
        Inventory inventory=new Inventory();
        inventory.setProduceName(produceName);
        inventory.setWarehouseId(warehouseId);
        inventory.setWarehouseType(2);
        inventory.setIsDelete(1);
        inventory.setNumber(0);
        inventoryService.save(inventory);
        return null ;
    }

    /**
     * 初始化仓库id
     * @return
     */
    @PostMapping("produceId")
    Integer produceId(){
        List<Produce>produceList=produceService.list();
        int size = produceList.size();
        return  size+1;
    }
    /**
     * 初始化仓库id
     */
    @PostMapping("/queryWarehouseByType")
    @ResponseBody
    List<Warehouse> queryWarehouseByType(){
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Warehouse::getWarehouseId).gt(Warehouse::getWarehouseAvailable,0).eq(Warehouse::getWarehouseType,2);
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
    @RequestMapping("/queryProduceByProduceName")
    List<Produce>queryProduceByProduceName(@RequestParam String produceName) {
        LambdaQueryWrapper<Produce> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Produce::getProduceName,produceName);
        List<Produce> produceList = new ArrayList<>();
        produceList=produceService.list(queryWrapper);
        return produceList;
    }

    /**
     * 根据产品名称查询产品id
     * @param produceName
     * @return
     */
    @RequestMapping("/queryProduceIdByProduceName")
    Integer queryProduceIdByProduceName(@RequestParam String produceName) {
        LambdaQueryWrapper<Produce> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Produce::getProduceName,produceName);
        return produceService.getOne(queryWrapper).getProduceId();
    }
    /**
     * 修改产品信息
     */
    @RequestMapping("/updateProduce")
    public Result updateProduce(Produce produce){
        boolean update = produceService.updateById(produce);
        if (update){
            return  Result.success(null,"更新成功");
        }
        return Result.error("失败");
    }
    /**
     * 查询所有产品名称
     */
    @PostMapping("queryProduceName")
    List<String> queryMaterialName(){
        QueryWrapper<Produce> queryWrapper= Wrappers.query();
        queryWrapper.select("produce_name");
        List<Produce> produces = produceMapper.selectList(queryWrapper);
        List<String> nameList = new ArrayList<>();
        for (Produce name : produces) {
            nameList.add(name.getProduceName());
        }
        return nameList ;

    }
}
