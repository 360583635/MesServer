package com.job.dispatchService.Work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Order;
import com.job.common.pojo.Process;
import com.job.common.pojo.Work;
import com.job.dispatchService.Work.mapper.OrderMapper;
import com.job.dispatchService.Work.mapper.ProcessMapper;
import com.job.dispatchService.Work.mapper.WorkMapper;
import com.job.dispatchService.Work.service.WorkBean;
import com.job.dispatchService.Work.service.WorkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
//@FeignClient(value = "ORDERSERVICE")
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {

    @Resource
    private WorkMapper workMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ProcessMapper processMapper;

    @Override
    public String insertWork(String processId, String  orderId){
        Work work = WorkBean.getWork();

        Order order = orderMapper.selectById("1");

        Process process = processMapper.selectById("11");

        work.setWID("1");
        work.setWState("1");
        work.setWProcessId("11");
        work.setWOrderID("1");
        work.setWProdNums(0);
        work.setWCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        work.setWEquipmentID("1");

        int result = workMapper.insert(work);
        if(result != 1){
            return "error";
        }
        return work.getWID();
    }

    @Override
    public String working(String workId){

        Work work = workMapper.selectById(workId);
        String wOrderId = work.getWOrderID();

        Order order = orderMapper.selectById(wOrderId);
        Integer orderNumber = order.getOrderNumber();

        String message = product(workId, orderNumber);

        return "ok";
    }

    public String product(String workId, int orderNumber){
        int count = 1;
        int sum = 100;
        boolean flag = true;
        long next;
        long pre = System.currentTimeMillis();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("w_id", workId);
        Work work = (Work) workMapper.selectList(queryWrapper).get(0);

        for (int i = 0; i < orderNumber; i++) {
            String message = productError(workId);
            if("error".equals(message)){
                return "error";
            }
            if(flag){
                work.setWState("2");
                int update = workMapper.update(work, queryWrapper);
                if(update == 0){
                    return "error";
                }
                flag = false;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count++;

            if(count == sum){
                next = System.currentTimeMillis();
                UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("w_id", workId).set("w_need_time", String.valueOf(next - pre));
                boolean update = update(updateWrapper);
                if(update == false){
                    return "error";
                }
            }
        }

        while (count == orderNumber){
            UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("w_state", "4")
                    .eq("w_id", workId);
            boolean update = update(updateWrapper);
            if(update){
                break;
            }
        }

        return "ok";
    }

    private String productError(String workId) {
        int num;
        Random random = new Random();

        num = random.nextInt(10);
        if(num > 8){
            for (int i = 0; i < 2; i++) {
                num = (int) Math.random() * 10;
                if(i == 0 && num < 5){
                    break;
                }else if(i == 1 && num > 2){
                    UpdateWrapper<Work> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("w_error_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(new Date()))
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

    public List<Work> getWorkList(String dateTime){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.gt("w_create_time", dateTime);
        List<Work> works = workMapper.selectList(queryWrapper);
        return works;
    }
}
