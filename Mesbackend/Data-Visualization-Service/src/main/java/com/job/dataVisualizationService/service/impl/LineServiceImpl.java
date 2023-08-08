package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.*;
import com.job.common.pojo.Process;
import com.job.dataVisualizationService.mapper.*;
import com.job.dataVisualizationService.service.LineService;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.util.ArrayList;
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
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private WorkMapper workMapper;
    @Override
    public Map<Object, Object> getall() {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper<Line> q1 = new QueryWrapper<>();
        q1.select("sum(exception_count) as totalExceptionCount","sum(success_count) as totalSuccessCount");
        Line line = lineMapper.selectOne(q1);

        QueryWrapper<Line> q2 = new QueryWrapper<>();
        q2.select("line","id","line_status");
        List<Line> list = lineMapper.selectList(q2);

        String[] linenames = new String[list.size()];
        String[] states = new String[list.size()];
        String[] ids = new String[list.size()];

        int i = 0;
        for (Line line1 : list) {
            linenames[i] = line1.getLine();
            states[i] = line1.getLineStatus();
            ids[i] = line1.getId();
            i++;
        }
        map.put("success_counts",line.getTotalSuccessCount());
        map.put("exception_counts",line.getTotalExceptionCount());
        map.put("counts",line.getTotalSuccessCount()+line.getTotalExceptionCount());
        map.put("linenames",linenames);
        map.put("linestates",states);
        map.put("ids",ids);
        return map;
    }

    @Override
    public Map<Object, Object> getone(Line line) {
        Map<Object,Object> map = new HashMap<>();
        QueryWrapper<Line> q1 = new QueryWrapper<>();
        q1.eq("id",line.getId());
        Line line1 = lineMapper.selectOne(q1);

        map.put("success_count",line1.getSuccessCount());
        map.put("exception_count",line1.getExceptionCount());
        map.put("waiting_count",line1.getOrderCount());
        map.put("已执行订单总数",line1.getSuccessCount()+line1.getExceptionCount());
        map.put("line_status",line1.getLineStatus());


        QueryWrapper<Flow> q2 = new QueryWrapper<>();
        q2.eq("id",line1.getLineFlowId());
        Flow flow = flowMapper.selectOne(q2);
        map.put("flowname",flow.getFlow());
        map.put("flow_desc",flow.getFlowDesc());

        QueryWrapper<FlowProcessRelation> q3 = new QueryWrapper<>();
        q3.select("sort_num","process_id");
        q3.eq("flow_id",line1.getLineFlowId());
        q3.orderByAsc("sort_num");
        List<FlowProcessRelation> list = flowProcessRelationMapper.selectList(q3);

        List<Process> processes = new ArrayList<>();
        for (FlowProcessRelation flowProcessRelation : list) {
            System.out.println(flowProcessRelation.getProcessId());
            QueryWrapper<Process> q4 = new QueryWrapper<>();
            q4.select();
            q4.eq("id",flowProcessRelation.getProcessId());
            Process process = processMapper.selectOne(q4);
            System.out.println(process);
            processes.add(process);
        }
        map.put("line_processes",processes);

        return map;
    }
}
