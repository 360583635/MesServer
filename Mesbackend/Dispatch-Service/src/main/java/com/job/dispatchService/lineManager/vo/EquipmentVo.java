package com.job.dispatchService.lineManager.vo;

import com.job.common.pojo.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author 庸俗可耐
 * @create 2023-07-18-22:55
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentVo {

    private String title;

    private List<Map<String,String>> equipmentMapList;
}
