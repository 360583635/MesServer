package com.job.dispatchService.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Order;
import com.job.common.pojo.Process;
import com.job.common.pojo.Work;
import com.job.dispatchService.work.mapper.WOrderMapper;
import com.job.dispatchService.work.mapper.WProcessMapper;
import com.job.dispatchService.work.mapper.WorkMapper;
import com.job.dispatchService.work.service.WorkBean;
import com.job.dispatchService.work.service.WorkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import wiki.xsx.core.snowflake.config.Snowflake;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
//@FeignClient(value = "ORDERSERVICE")
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {

    @Resource
    private Snowflake snowflake;

    @Resource
    private WorkMapper workMapper;

    @Resource
    private WOrderMapper orderMapper;

    @Resource
    private WProcessMapper processMapper;

    //创建工单
    @Override
    public String insertWork(String processId, String  orderId){
        Work work = WorkBean.getWork();

        Order order = orderMapper.selectById("1");

        Process process = processMapper.selectById("11");

        //雪花算法生产工单id、设置状态（创建工单）、工序id、订单id、生产数量、创建时间、设备id
        work.setWId(String.valueOf(snowflake.nextId()));
        work.setWState("1");
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
                work.setWState("2");
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
            updateWrapper.set("w_state", "4")
                    .set("w_prod_nums", count)
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
                            .set("w_state", "3")
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

    public List<Work> getWorkListByDateTime(String dateTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("w_create_time", dateTime);
        List<Work> works = workMapper.selectList(queryWrapper);
        return works;
    }

    public List<Work> getAllWorkList(){
        return workMapper.selectList(null);
    }
}
