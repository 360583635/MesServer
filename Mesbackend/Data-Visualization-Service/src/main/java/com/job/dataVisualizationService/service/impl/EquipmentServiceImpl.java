package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Equipment;
import com.job.common.pojo.Flow;
import com.job.common.pojo.Line;
import com.job.dataVisualizationService.mapper.EquipmentMapper;
import com.job.dataVisualizationService.mapper.FlowMapper;
import com.job.dataVisualizationService.mapper.LineMapper;
import com.job.dataVisualizationService.service.EquipmentService;
import com.job.dataVisualizationService.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public Map<Object, Object> getEqument() {
        //设备状态统计
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper<Equipment> q1 = new QueryWrapper<>();
        q1.select("count(equipment_id) as count","equipment_state");
        q1.groupBy("equipment_state");
        List<Equipment> list = equipmentMapper.selectList(q1);


        Map<Object,Object> map1 = new HashMap<>();
        Map<Object,Object> map2 = new HashMap<>();
        for (Equipment equipment : list) {
            String string = "";
            switch (equipment.getEquipmentState()){
                case 0:string="空闲";break;
                case 1:string="使用中";break;
                case 2:string="异常";break;
                case 3:string="维修中";break;
            }

            map1.put(string,equipment.getCount());

            QueryWrapper<Equipment> q2 = new QueryWrapper<>();
            q2.select("equipment_id");
            q2.eq("equipment_state",equipment.getEquipmentState());
            List<Equipment> list2 = equipmentMapper.selectList(q2);
            List list1 = new ArrayList<>();
            for (Equipment equipment1 : list2) {
                list1.add(equipment1.getEquipmentId());
            }
            map2.put(equipment.getEquipmentState(),list1);
        }

        Map<Object,Object> map3 = new HashMap<>();

        QueryWrapper<Equipment> q3 = new QueryWrapper<>();
        q3.select("equipment_id","equipment_state","production_Capacity","maximum_Load","Operating_Speed");
        List<Equipment> list1 = equipmentMapper.selectList(q3);

        for (Equipment equipment : list1) {
            Map<Object,Object> map4 =new HashMap<>();
            map4.put("id",equipment.getEquipmentId());
            map4.put("production_Capacity",equipment.getProductionCapacity());
            map4.put("maximum_Load",equipment.getMaximumLoad());
            map4.put("Operating_Speed",equipment.getOperatingSpeed());
            //设备id —— 设备详细信息
            map3.put(equipment.getEquipmentId(),map4);
        }
        //状态数量对应
        map.put("state_count",map1);
        //状态 ids 对应
        map.put("state_ids",map2);
        //设备详情对应
        map.put("equipment",map3);
        return map;
    }
}
