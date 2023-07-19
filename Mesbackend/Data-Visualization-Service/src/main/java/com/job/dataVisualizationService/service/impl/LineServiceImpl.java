package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Process;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.dataVisualizationService.mapper.FlowMapper;
import com.job.dataVisualizationService.mapper.LineMapper;
import com.job.common.pojo.Line;
import com.job.dataVisualizationService.mapper.FlowProcessRelationMapper;
import com.job.dataVisualizationService.mapper.ProcessMapper;
import com.job.dataVisualizationService.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:Liang
 */
@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {
    @Autowired
    private LineMapper lineMapper;
    @Autowired
    private FlowMapper flowMapper;
    @Autowired
    private FlowProcessRelationMapper flowProcessRelationMapper;
    @Autowired
    private ProcessMapper processMapper;
    @Override
    public Map<Object, Object> getall() {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper<Line> q1 = new QueryWrapper<>();
        q1.select("sum(exception_count) as totalExceptionCount","sum(success_count) as totalSuccessCount");
        Line line = lineMapper.selectOne(q1);

        QueryWrapper<Line> q2 = new QueryWrapper<>();
        q2.select("id","line_status");
        List<Line> list = lineMapper.selectList(q2);

        Map<Object,Object> map1 =new HashMap<>();
        for (Line line1 : list) {
            map1.put(line1.getId(),line1.getLineStatus());
        }
        map.put("执行成功总数",line.getTotalSuccessCount());
        map.put("执行异常总数",line.getTotalExceptionCount());
        map.put("执行订单总数",line.getTotalSuccessCount()+line.getTotalExceptionCount());
        map.put("流水线状态",map1);
        return map;
    }

    @Override
    public Map<Object, Object> getone(Line line) {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper<Line> q1 = new QueryWrapper<>();
        q1.eq("id",line.getId());
        Line line1 = lineMapper.selectOne(q1);

        map.put("执行成功次数",line1.getSuccessCount());
        map.put("执行异常次数",line1.getExceptionCount());
        map.put("待执行订单次数",line1.getOrderCount());
        map.put("已执行订单总数",line1.getSuccessCount()+line1.getExceptionCount());
        map.put("流水线状态",line1.getLineStatus());


        QueryWrapper<Flow> q2 = new QueryWrapper<>();
        q2.eq("id",line1.getLineFlowId());
        Flow flow = flowMapper.selectOne(q2);
        map.put("流水线名",flow.getFlow());
        map.put("流水线描述",flow.getFlowDesc());

        QueryWrapper<FlowProcessRelation> q3 = new QueryWrapper<>();
        q3.select("sort_num","process_id");
        q3.eq("flow_id",line.getId());
        q3.orderByAsc("sort_num");
        List<FlowProcessRelation> list = flowProcessRelationMapper.selectList(q3);

        Map<Object,Object> map1 = new HashMap<>();
        for (FlowProcessRelation flowProcessRelation : list) {
            QueryWrapper<Process> q4 = new QueryWrapper<>();
            q4.select("id","process","process_desc","exception_count","success_count");
            q4.eq("id",flowProcessRelation.getProcessId());
            Process process = processMapper.selectOne(q4);
            map1.put(flowProcessRelation.getSortNum(),process);
        }
        map.put("流水线流程",map1);

        return map;
    }
}
