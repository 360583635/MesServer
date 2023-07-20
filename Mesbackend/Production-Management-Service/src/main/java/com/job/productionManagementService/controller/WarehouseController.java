package com.job.productionManagementService.controller;


import com.job.common.pojo.Warehouse;
import com.job.common.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class  WarehouseController {

    @Resource
    private w

    /**
     * 添加新仓库
     * @param
     * @return
     */
    @PostMapping("/saveWarehouse")
    @ResponseBody
    public Result saveWarehouse(@ResponseBody Warehouse warehouse){

        boolean save = equipmentService.save(equipment);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }
    }

}
