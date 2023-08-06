package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Equipment;
import org.springframework.web.bind.annotation.RequestParam;


public interface EquipmentService extends IService<Equipment> {

    public Boolean updateEquipmentStatus(@RequestParam String equipmentId);
}
