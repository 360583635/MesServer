package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.pojo.Order;
import com.job.dataVisualizationService.mapper.InventoryMapper;
import com.job.dataVisualizationService.mapper.MaterialMapper;
import com.job.dataVisualizationService.mapper.OrderMapper;
import com.job.dataVisualizationService.pojo.MaterialData;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.dataVisualizationService.service.MaterialService;
import com.job.dataVisualizationService.service.OrderService;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class MaterialImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public MaterialData classification() {
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


                    area[y] = number1*area1;
                    volume[y] = number1*volume1;
                    total[y] = number1*cost;
                }
            }
            y++;
        }


        materialData.setId(id);
        materialData.setName(name);
        materialData.setNumber(number);
        materialData.setArea(area);
        materialData.setVolume(volume);
        materialData.setTotal(total);
        for (int i1 : materialData.getId()) {
            System.out.println(i1);
        }
        for (String s : name) {
            System.out.println(s);
        }
        for (int i1 : number) {
            System.out.println(i1);
        }
        for (int i1 : area) {
            System.out.println(i1);
        }
        for (int i1 : volume) {
            System.out.println(i1);
        }
        for (double v : total) {
            System.out.println(v);
        }

        return materialData;
    }
}
