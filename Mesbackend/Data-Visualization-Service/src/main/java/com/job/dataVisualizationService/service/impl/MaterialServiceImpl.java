package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.pojo.Warehouse;
import com.job.dataVisualizationService.mapper.InventoryMapper;
import com.job.dataVisualizationService.mapper.MaterialMapper;
import com.job.dataVisualizationService.mapper.WarehouseMapper;
import com.job.dataVisualizationService.pojo.MaterialData;
import com.job.dataVisualizationService.service.MaterialService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    public Map<Object, Object> classification() {
        MaterialData materialData = new MaterialData();
        QueryWrapper<Inventory> q1 = new QueryWrapper<Inventory>();
        q1.select("material_name","sum(number) as number");
        q1.ne("material_name","Null");
        q1.groupBy("material_name");
        List<Inventory> list = inventoryMapper.selectList(q1);
        //原材料id
        int[] id = new int[list.size()];
        //原材料名称
        String [] name = new String[list.size()];
        //占地面积
        int[] area = new int[list.size()];
        //占仓库体积
        int[] volume = new int[list.size()];
        //占货款
        double[] total = new double[list.size()];
        //原材料数量
        int[] number = new int[list.size()];

        int i = 0;
        for (Inventory material : list) {
            name[i] = material.getMaterialName();
            number[i] = material.getNumber();
            i++;
        }
        QueryWrapper<Material> q2 = new QueryWrapper<Material>();
        q2.select("material_id","material_name","material_area","material_volume","material_cost");
        q2.in("material_name",name);
        List<Material> list2 = materialMapper.selectList(q2);
        int y = 0;
        for (String s : name) {
            for (Material material : list2) {
                if (s.equals(material.getMaterialName())){
                    //填写id
                    id[y] = material.getMaterialId();
                    //获取数量
                    int number1 = number[y];
                    double cost = material.getMaterialCost().doubleValue();
                    System.out.println(material.getMaterialCost());
                    System.out.println("cost="+cost);
                    //获取区域面积
                    String materialArea = "";
                    for (int i1 = 0; i1 < material.getMaterialArea().length(); i1++) {
                        if (material.getMaterialArea().charAt(i1)>=48 && material.getMaterialArea().charAt(i1)<=57) {
                            materialArea += material.getMaterialArea().charAt(i1);
                        }
                    }
                    int area1 = Integer.parseInt(materialArea);
                    //获取体积
                    String materialVolume = "";
                    for (int i1 = 0; i1 < material.getMaterialVolume().length(); i1++) {
                        if (material.getMaterialVolume().charAt(i1)>=48 && material.getMaterialVolume().charAt(i1)<=60) {
                            materialVolume += material.getMaterialVolume().charAt(i1);
                        }
                    }
                    int volume1 = Integer.parseInt(materialVolume);


                    area[y] = number1*area1/100;
                    volume[y] = number1*volume1/100;
                    total[y] = number1*cost;
                }
            }
            y++;
        }
        int z = 0;
        Map<Object,Object> map = new HashMap<>();
        for (int i1 : id) {
            Map<Object,Object> map1 = new HashMap<>();
            map1.put("原材料名称",name[z]);
            map1.put("原材料数量",number[z]);
            map1.put("原材料占地面积",area[z]);
            map1.put("原材料占体积",volume[z]);
            map1.put("原材料所占金额",total[z]);
            map.put(i1,map1);
            z++;
        }
        return map;
    }

    @Override
    public Map<Object,Object> getWarehouse() {
        MaterialData materialData = new MaterialData();

        Map<Object,Object> map = new HashMap<>();

        //获取原材料名称和数量
        QueryWrapper<Inventory> q1 = new QueryWrapper<Inventory>();
        q1.select("material_name","sum(number) as number");
        q1.ne("material_name","Null");
        q1.groupBy("material_name");
        List<Inventory> list1 = inventoryMapper.selectList(q1);
        //原材料名称
        String [] name = new String[list1.size()];
        //原材料数量
        int[] number = new int[list1.size()];

        int i = 0;
        for (Inventory inventory : list1) {
            name[i] = inventory.getMaterialName();
            number[i] = inventory.getNumber();
            i++;
        }

        //获取仓库总空间和剩余空间
        QueryWrapper<Warehouse> q2 = new QueryWrapper<>();
        q2.select("warehouse_capacity","warehouse_available");
        List<Warehouse> list2 = warehouseMapper.selectList(q2);
        int sumWarehouseCapacity = 0;
        int sumWarehouseAvailable = 0;

        for (Warehouse warehouse : list2) {
            String warehouseCapacity = "";
            for (int i1 = 0; i1 < warehouse.getWarehouseCapacity().length(); i1++) {
                if (warehouse.getWarehouseCapacity().charAt(i1)>=48 && warehouse.getWarehouseCapacity().charAt(i1)<=57) {
                    warehouseCapacity += warehouse.getWarehouseCapacity().charAt(i1);
                }
            }
            int warehouseCapacity1 = Integer.parseInt(warehouseCapacity);

            String warehouseAvailable = "";
            for (int i1 = 0; i1 < warehouse.getWarehouseAvailable().length(); i1++) {
                if (warehouse.getWarehouseAvailable().charAt(i1)>=48 && warehouse.getWarehouseAvailable().charAt(i1)<=57) {
                    warehouseAvailable += warehouse.getWarehouseAvailable().charAt(i1);
                }
            }
            int warehouseAvailable1 = Integer.parseInt(warehouseAvailable);

            sumWarehouseCapacity += warehouseCapacity1;
            sumWarehouseAvailable += warehouseAvailable1;
        }

        map.put("所有仓库剩余空间",sumWarehouseAvailable);
        map.put("所有仓库总空间",sumWarehouseCapacity);

        //获取原材料100个使用空间
        QueryWrapper<Material> q3 = new QueryWrapper<>();
        q3.select("material_name","material_volume");
        q3.in("material_name",name);

        //Map("原材料名称",使用空间)
        int y = 0;
        List<Material> list3 = materialMapper.selectList(q3);
        Map<String ,Object> map1 = new HashMap<>();
        for (String s : name) {

            for (Material material : list3) {
                if (s.equals(material.getMaterialName())){

                    String materialVolume = "";
                    for (int i1 = 0; i1 < material.getMaterialVolume().length(); i1++) {
                        if (material.getMaterialVolume().charAt(i1)>=48 && material.getMaterialVolume().charAt(i1)<=57) {
                            materialVolume += material.getMaterialVolume().charAt(i1);
                        }
                    }
                    int warehouseAvailable1 = Integer.parseInt(materialVolume);
                    map1.put(s,warehouseAvailable1*number[y]/100);
                }
            }
            y++;
        }
        map.put("原材料所占空间",map1);
        return map;
    }
}
