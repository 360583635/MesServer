package com.job.productionManagementService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Produce;
import com.job.productionManagementService.mapper.ProduceMapper;
import com.job.productionManagementService.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduceServiceImpl extends ServiceImpl<ProduceMapper, Produce> implements ProduceService {

    @Autowired
    private ProduceMapper produceMapper;

    @Override
    public List<Produce> queryProduces() {
        LambdaQueryWrapper<Produce> queryWrapper = new LambdaQueryWrapper<>();
        List<Produce> produces = produceMapper.selectList(queryWrapper);
        return produces;
    }

}
