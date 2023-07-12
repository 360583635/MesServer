package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dataVisualizationService.mapper.OrderMapper;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.dataVisualizationService.service.OrderService;
import com.job.common.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 菜狗
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public OrderData preData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateString = sdf.format(date);
        String dayString = dateString.substring(8,8+2);
        String mmString = dateString.substring(5,5+2);
        if(dateString.charAt(0) == '0')dayString = dateString.substring(1);
        int day = Integer.parseInt(dayString);
        if(mmString.charAt(0) == '0')mmString = mmString.substring(1);
        int mm = Integer.parseInt(mmString);
        int[] mPre = new int[]{mm%12+1,(mm+1)%12+1,(mm+2)%12+1,(mm+3)%12+1,(mm+4)%12+1};
        Calendar cEnd = Calendar.getInstance();
        Calendar cPre = Calendar.getInstance();
        cEnd.add(Calendar.DAY_OF_MONTH,-day);
        cPre.add(Calendar.DAY_OF_MONTH,-day);
        cPre.add(Calendar.MONTH,-1);
        LambdaQueryWrapper<Order> q = new LambdaQueryWrapper<>();
        q.le(Order::getOrderDate,cEnd);
        List<Order> orders = orderMapper.selectList(q);
        if(orders==null||orders.size()==0){      //工厂的订单只有这个月有数据
            LambdaQueryWrapper<Order> qq = new LambdaQueryWrapper<>();
            qq.le(Order::getOrderDate,new Date());
            List<Order> orders1 = orderMapper.selectList(qq);
            int sumPrice = 0;  //计算金额
            int sumCount = orders1==null ? 0 : orders1.size();  //计算数量
            for (Order order : orders1) {
                sumPrice+=order.getOrderPrice();
            }
            OrderData orderData = new OrderData();
            int atm = (int)(sumPrice*1.0*30/day + 0.5);//向上取整
            int cnt = (int)(sumCount*1.0*30/day + 0.5);
            orderData.setPreAmount(new int[]{atm,atm,atm,atm,atm});
            orderData.setPreTime(mPre);
            orderData.setPreCount(new int[]{cnt,cnt,cnt,cnt,cnt});
            return orderData;
        } else {                    //工厂的订单这个月前也有有数据
            Map<Integer,Long> mapAtm = new HashMap<>();
            Map<Integer,Integer> mapCnt = new HashMap<>();
            double sMm = 0;
            double sPrice = 0;
            double sCount = 0;
            int sm = mm;
            for (int i = 0; i < 5; i++) {
                LambdaQueryWrapper<Order> q1 = new LambdaQueryWrapper<>();
                q1.le(Order::getOrderDate,cEnd);
                q1.ge(Order::getOrderDate,cPre);
                List<Order> orders1 = orderMapper.selectList(q1);
                long sumPrice = 0;
                for (Order order : orders1) {
                    sumPrice+=(long)order.getOrderPrice();
                }
                mapAtm.put(--sm,sumPrice);
                mapCnt.put(sm,orders1.size());
                sMm+=sm;
                sPrice+=sumPrice;
                sCount+=orders1.size();
                LambdaQueryWrapper<Order> q2 = new LambdaQueryWrapper<>();
                q2.le(Order::getOrderDate,cPre);
                List<Order> orders2 = orderMapper.selectList(q2);
                if(orders2==null||orders2.size()==0)break;
                cEnd.add(Calendar.MONTH,-1);
                cPre.add(Calendar.MONTH,-1);
            }
            sMm = sMm/mapAtm.size();
            sPrice = sPrice/mapAtm.size();
            sCount = sCount/mapCnt.size();
            double fz = -sMm*sPrice*mapAtm.size(),fm = -sMm*sMm*mapAtm.size();   //计算金额
            for (Integer m : mapAtm.keySet()){
                fz+= m*mapAtm.get(m);
                fm+= m*m;
            }
            double b = fz/fm;  // 25/0.5
            double a = sPrice - b*sMm;
            int[] arrAtm = new int[]{(int)(b*(1+mm)+a),(int)(b*(2+mm)+a),(int)(b*(3+mm)+a)
            ,(int)(b*(4+mm)+a),(int)(b*(5+mm)+a)};
            fz = -sMm*sCount*mapCnt.size();      //计算数量
            for (Integer m : mapCnt.keySet()){
                fz+= m*mapCnt.get(m);
            }
            b = fz/fm;
            a = sCount - b*sMm;
            int[] arrCnt = new int[]{(int)(b*(1+mm)+a),(int)(b*(2+mm)+a),(int)(b*(3+mm)+a)
                    ,(int)(b*(4+mm)+a),(int)(b*(5+mm)+a)};
            OrderData orderData = new OrderData();
            orderData.setPreTime(mPre);
            orderData.setPreAmount(arrAtm);
            orderData.setPreCount(arrCnt);
            return orderData;
        }
    }

    @Override
    public OrderData countData(OrderData order) {

        OrderData orderData = new OrderData();
        //格式化时间日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = sdf.format(date);
        //取年月
        String dayString = dateString.substring(8,8+2);
        String mmString = dateString.substring(5,5+2);
        //月日消0
        if(dateString.charAt(0) == '0')dayString = dateString.substring(1);
        int day = Integer.parseInt(dayString);
        if(mmString.charAt(0) == '0')mmString = mmString.substring(1);
        int mm = Integer.parseInt(mmString);
        //获取当前时间
        //获得结束时间
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.DAY_OF_MONTH,-day);
        Calendar preTime = Calendar.getInstance();
        //获得开始时间
        preTime.add(Calendar.DAY_OF_MONTH,-day);
        preTime.add(Calendar.MONTH,-order.getSeparate());
        //数量 金额 时间
        int[] time = new int[order.getDataNumber()];
        int[] count = new int[order.getDataNumber()];
        int[] amount = new int[order.getDataNumber()];

        for (int i = 0; i < order.getDataNumber(); i++) {
            LambdaQueryWrapper<Order> q = new LambdaQueryWrapper<>();
            q.le(Order::getOrderDate,endTime);
            q.ge(Order::getOrderDate,preTime);
            long count1 = orderMapper.selectCount(q);
            List<Order> orders = orderMapper.selectList(q);
            time[i] = --mm;
            count[i] = (int)count1;

            int total = 0;
            for (Order order2 :orders
                 ) {
                total+=order2.getOrderNumber()*order2.getOrderPrice();
            }
            endTime.add(Calendar.MONTH,-order.getSeparate());
            preTime.add(Calendar.MONTH,-order.getSeparate());
            amount[i] = total;
        }
        orderData.setCount(count);
        orderData.setTime(time);
        orderData.setAmount(amount);
        return orderData;
    }

    @Override
    public OrderData classification(OrderData order) {
        OrderData orderData = new OrderData();

        LambdaQueryWrapper<Order> q1 = new LambdaQueryWrapper<>();
        q1.select(Order::getTypeName);
        Long typeCount = orderMapper.selectCount(q1);

        String[] type = new String[Math.toIntExact(typeCount)];
        int[] total = new int[Math.toIntExact(typeCount)];

        //sql语句
        QueryWrapper<Order> q = new QueryWrapper<>();
        q.select("product_name","SUM(order_price*order_number)");
        q.between("order_date",order.getStartTime(),order.getEndTime());
        q.groupBy("product_name");
        List<Order> orders = orderMapper.selectList(q);
        int i = 0;
        for (Order order1:orders
             ) {
            type[i] = order1.getProductName();
            total[i] = (order1.getOrderPrice()*order1.getOrderNumber());
            i++;
        }
        orderData.setProductType(type);
        orderData.setTotal(total);
        return orderData;
    }

    @Override
    public OrderData countOneData(OrderData order) {
        return null;
    }
}
