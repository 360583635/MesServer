package com.job.feign.clients;


import com.job.feign.pojo.Material;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-18:27
 * @description
 */

@FeignClient(value = "PRODUCTIONMANAGEMENTSERVICE")
public interface MaterialClient {

    /**
     * Dispatch-Service 调用 Production-Management-Service的queryMaterials方法
     * @return
     */
    @GetMapping("/productionManagement/material/queryMaterials")
    List<Material>  queryMaterials();

}
