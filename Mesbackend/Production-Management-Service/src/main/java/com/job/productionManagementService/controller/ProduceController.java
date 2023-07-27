package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Produce;
import com.job.common.result.Result;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.ProduceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/produce")
public class ProduceController {

    @Resource
    private InventoryService inventoryService ;

    @Resource
    private ProduceService produceService ;

    /**
     * 添加新产品
     */
   @RequestMapping("/saveProduce")
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
     * 通过产品名称查询产品数据
     */
    @RequestMapping("/queryProduceByProduceName")
    List<Produce>queryProduceByProduceName(@RequestParam String prodeceName) {
        LambdaQueryWrapper<Produce> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(Produce::getProduceName,prodeceName);
        List<Produce> produceList = new ArrayList<>();
        produceList=produceService.list(queryWrapper);
        return produceList;
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
}
