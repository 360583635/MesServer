package com.job.dispatchService.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Order;
import com.job.common.pojo.Process;
import com.job.common.pojo.Work;

import com.job.common.redis.RedisCache;
import com.job.dispatchService.work.config.StateConfig;
import com.job.dispatchService.work.mapper.WOrderMapper;
import com.job.dispatchService.work.mapper.WProcessMapper;
import com.job.dispatchService.work.mapper.WorkMapper;
import com.job.dispatchService.work.service.WorkBean;
import com.job.dispatchService.work.service.WorkService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import wiki.xsx.core.snowflake.config.Snowflake;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
//@FeignClient(value = "ORDERSERVICE")
@SuppressWarnings("all")
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WOrderMapper orderMapper;

    @Autowired
    private WProcessMapper processMapper;

    @Autowired
    private RedisCache redisCache;

    //创建工单
    @Override
    public String insertWork(String processId, String  orderId){
        Work work = WorkBean.getWork();

        Order order = orderMapper.selectById("1");

        Process process = processMapper.selectById("11");

        //雪花算法生产工单id、设置状态（创建工单）、工序id、订单id、生产数量、创建时间、设备id
        work.setWId(String.valueOf(snowflake.nextId()));
        work.setWState(StateConfig.CREATE_STATE);
        work.setWProcessId(processId);
        work.setWOrderId(orderId);
        work.setWProdNums(0);
        work.setWCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        work.setWEquipmentId("1");

        int result = workMapper.insert(work);
        if(result != 1){
            return "error";
        }
        return work.getWId();
    }

    //生产中
    @Override
    public String working(String workId){

        Work work = workMapper.selectById(workId);
        String wOrderId = work.getWOrderId();

        Order order = orderMapper.selectById(wOrderId);
        Integer orderNumber = order.getOrderNumber();

        String message = this.product(workId, orderNumber);
        if("error".equals(message)){
            return "error";
        }

        return "ok";
    }

    //生产

    public String product(String workId, int orderNumber){
        int count = 0;
        int sum = 100;
        boolean flag = true;
        long next;
        long pre;

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("w_id", workId);
        Work work = (Work) workMapper.selectList(queryWrapper).get(0);

        pre = System.currentTimeMillis();
        for (int i = 0; i < orderNumber; i++) {
            //生产机器是否报错
            String message = this.productError(workId, count);
            if("error".equals(message)){
                return "error";
            }
            if(flag){
                //修改状态 2 生产中
                work.setWState(StateConfig.WORKING_STATE);
                int update = workMapper.update(work, queryWrapper);
                if(update == 0){
                    return "error";
                }
                flag = false;
            }

            //用休眠时间代替没一件生产所花时间
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count++;

            if(count == sum){
                //生产100件时，记录所需时间到表格中
                next = System.currentTimeMillis();
                UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("w_id", workId)
                        .set("w_need_time", String.valueOf(next - pre));
                boolean update = update(updateWrapper);
                if(update == false){
                    return "error";
                }
            }
        }

        //生产完成，状态改为 4（正常生产）
        while (count == orderNumber){
            UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("w_state", StateConfig.FINISH_STATE)
                    .set("w_prod_nums", count)
                    .set("success_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .eq("w_id", workId);
            boolean update = update(updateWrapper);
            if(update){
                break;
            }
        }

        return "ok";
    }

    //设置机器报错情况
    //三层逻辑
    private String productError(String workId, int prodNum) {
        int num;
        Random random = new Random();

        num = random.nextInt(10);
        if(num > 7){
            for (int i = 0; i < 2; i++) {
                num = (int) Math.random() * 10;
                if(i == 0 && num < 5){
                    break;
                }else if(i == 1 && num > 2){
                    //修改表中信息
                    UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("w_error_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(new Date()))
                            .set("w_prod_nums", prodNum)
                            .set("w_state", StateConfig.EXCEPTION_STATE)
                            .eq("w_id", workId);
                    boolean update = update(null, updateWrapper);
                    while (!update){
                        update = update(null, updateWrapper);
                    }
                    return "error";
                }else {
                    continue;
                }
            }
        }
        return "ok";
    }

    //服务熔断
//    @HystrixCommand(fallbackMethod = "getWorkListByDateTime_fallback", commandProperties = {
//            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")//失败率达到多少后跳闸
//    })
    public List<Work> getWorkListByDateTime(String dateTime){
//        int a = 1/0;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("w_create_time", dateTime);
        List<Work> works = workMapper.selectList(queryWrapper);
        return works;
    }

    public String getWorkListByDateTime_fallback(String dateTime){
        System.out.println("=======================");
        return "从数据库中多次查询数据失败，请稍后再试......";
    }

    public List<Work> getAllWorkList(){
        List<Work> works = workMapper.selectList(null);
        Set typles = new HashSet();
        for (Work work : works) {
            Timestamp timestamp = Timestamp.valueOf(work.getWCreateTime());
            long time = timestamp.getTime();
            String score = String.valueOf(time);
            ZSetOperations.TypedTuple<String> objectDefaultTypedTuple = new DefaultTypedTuple(work, Double.valueOf(score));
            typles.add(objectDefaultTypedTuple);
        }
        redisCache.addAll("work", typles);
        Set set = redisCache.selectByTime("work",
                Double.valueOf(100),
                Double.valueOf(System.currentTimeMillis()));
        for (Object o : set) {
            System.out.println(o.toString());
        }
        return works;
    }
}
