package com.job.dispatchService.work.controller;

import com.job.common.pojo.Order;
import com.job.common.pojo.Process;
import com.job.common.pojo.Work;
import com.job.dispatchService.work.config.StateConfig;
import com.job.dispatchService.work.mapper.WFlowMapper;
import com.job.dispatchService.work.mapper.WOrderMapper;
import com.job.dispatchService.work.mapper.WProcessMapper;
import com.job.dispatchService.work.service.WorkService;
import com.job.dispatchService.work.util.DateTimeUtil;
import com.job.dispatchService.work.util.StringAndNumberUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wiki.xsx.core.snowflake.config.Snowflake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SuppressWarnings("all")
@RequestMapping("/dispatch")
public class WorkController {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private WorkService workService;

    @Autowired
    private WOrderMapper orderMapper;

    @Autowired
    private WProcessMapper processMapper;

    @Autowired
    private WFlowMapper flowMapper;

    //工序调用接口
    //processId=11&orderId=1676951868304564226
    @RequestMapping(value = "/work")
    public Object working(@RequestParam("processId") String processId,
                          @RequestParam("orderId") String orderId){

        boolean b = StringAndNumberUtil.StingAndNumberTest(processId, orderId);
        if(!b){
            return "工单id或工序id不是只有字符串和数字组成";
        }

        String workId = workService.insertWork(processId, orderId);
        if(workId != null){
            String message = workService.working(workId);
            if("error".equals(message)){
                return "error";
            }
        }else {
            return "error";
        }
        return "ok";
    }

    //前端网页数据呈现
    @PostMapping("/search")
//    @HystrixCommand(fallbackMethod = "searchByDatetime_fallback", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
//    })
    public Map searchByDatetime(@RequestParam(value = "dateTime",required = false) String dateTime){

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Map map = new HashMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(dateTime + "===========");
        if(dateTime != null && !dateTime.isEmpty()){
            boolean state = DateTimeUtil.isValid(dateTime);
            if(state == false){
                map.put("state", "error");
                map.put("message", "时间格式要求yyyy-MM-dd HH:mm:ss");
                return map;
            }
            map.put("state", "ok");

            List<Work> works = workService.getWorkListByDateTime(dateTime);
            System.out.println(works + "=================");
            List pagesList = this.forEachWorks(works);
            map.put("pagesList", pagesList);
        }else if((dateTime != null && dateTime.isEmpty()) || dateTime == null){
            List<Work> works = workService.getAllWorkList();
            List pagesList = this.forEachWorks(works);
            map.put("pagesList", pagesList);
        }

        return map;
    }

    public Object searchByDatetime_fallback(@RequestParam(value = "dateTime",required = false) String dateTime){
        return "从数据库中多次查询数据失败，请稍后再试......";
    }

    public List forEachWorks(List<Work> works){
        List pagesList = new ArrayList();
        for (Work work : works) {
            Map hashMap = new HashMap<>();
            Order order = orderMapper.selectById(work.getWOrderId());
            hashMap.put("workId", work.getWId());
            hashMap.put("orderName", order.getProductName());
            hashMap.put("nums", work.getWProdNums());
            Process process = processMapper.selectById(work.getWProcessId());
            hashMap.put("processName", process.getProcess());
            hashMap.put("flowName", process.getProcess());
            if(StateConfig.EXCEPTION_STATE.equals(work.getWState())){
                hashMap.put("finish", "异常");
                hashMap.put("errorTime", work.getWErrorTime());
            }else {
                hashMap.put("finish", "是");
                hashMap.put("errorTime", "");
            }
            hashMap.put("createTime", work.getWCreateTime());
            pagesList.add(hashMap);
        }
        return pagesList;
    }
}
