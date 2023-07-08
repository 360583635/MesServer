package com.job.dispatchService.Work.controller;

import com.job.common.pojo.Order;
import com.job.common.pojo.Process;
import com.job.common.pojo.Work;
import com.job.dispatchService.Work.mapper.FlowMapper;
import com.job.dispatchService.Work.mapper.OrderMapper;
import com.job.dispatchService.Work.mapper.ProcessMapper;
import com.job.dispatchService.Work.service.WorkService;
import com.job.dispatchService.Work.util.DateTimeUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work")
public class WorkController {

    @Resource
    private WorkService workService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private FlowMapper flowMapper;

    @GetMapping(value = "/{processId}/{orderId}")
    public Object working(@PathVariable("processId") String processId,
                          @PathVariable("orderId") String orderId){
        String workId = workService.insertWork(processId, orderId);
        if(workId != null){
            String message = workService.working(workId);
            if("error".equals(message)){
                return "error";
            }
        }else {
            return "error";
        }
        return "error";
    }

    @GetMapping("/search/{dateTime}")
    public Map searchByDatetime(@PathVariable("dateTime") String dateTime){

        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(dateTime != null && !dateTime.isEmpty()){
            boolean state = DateTimeUtil.isValid(dateTime);
            if(state == false){
                map.put("state", "error");
                map.put("message", "时间格式要求yyyy-MM-dd HH:mm:ss");
                return map;
            }
            map.put("state", "ok");

            List<Work> works = workService.getWorkList(dateTime);
            for (Work work : works) {
                Map hashMap = new HashMap<>();
                Order order = orderMapper.selectById(work.getWOrderID());
                hashMap.put("workId", work.getWID());
                hashMap.put("orderName", "小米");
                hashMap.put("nums", work.getWProdNums());
                Process process = processMapper.selectById(work.getWProcessId());
                hashMap.put("processName", "工序1");
                hashMap.put("flowName", "流水线1");
                if("3".equals(work.getWState())){
                    hashMap.put("finish", "异常");
                    hashMap.put("errorTime", work.getWErrorTime());
                }else {
                    hashMap.put("finish", "是");
                    hashMap.put("errorTime", "");
                }
                hashMap.put("createTime", work.getWCreateTime());
            }
        }

        return map;
    }
}
