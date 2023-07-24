package com.job.productionManagementService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Warehouse;
import com.job.productionManagementService.mapper.WarehouseMapper;
import com.job.productionManagementService.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {
}
