package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Warehouse;

public interface WarehouseService extends IService<Warehouse> {

    boolean saveRawMaterial(Inventory inventory);
}
