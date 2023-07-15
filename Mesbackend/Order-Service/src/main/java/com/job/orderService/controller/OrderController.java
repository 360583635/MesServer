package com.job.orderService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Order;
import com.job.orderService.common.result.Result;
import com.job.orderService.mapper.OrderMapper;
import com.job.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @return
     */
    @GetMapping("/addOrder")
    public Result<Order> addOrder(Order order){
        Result<Order> result = orderService.addOrder(order);
        return result;
//        //数据验证
//        if (order == null){
//            return  Result.error("输入对象不能为空！");
//        }else if (order.getOrderNumber() != null  && order.getExpectDate() != null && order.getProductId() !=null
//                && order.getTypeName() != null &&  order.getCustomName() !=null && order.getCustomTel() !=null
//                && order.getRawName() !=null && order.getRawNum() !=null){
//
//            order.setOrderDate(new Date());
//            //TODO: 2023/7/8
//            order.setAuditor(null);//获取当前登录用户
//            order.setPriority(0);
//            order.setProductionStatus(0);
//            //TODO: 2023/7/8
//            order.setOrderPrice(null);
//            order.setIsDelete(0);
//            int i = orderMapper.insert(order);
//
//            if (i>0)
//            {
//                return Result.success("success");
//            }else {
//                return  Result.error("error!");
//            }
//        }
//        else{
//            return  Result.error("error!");
//        }

    }

    /**
     * 修改订单页面显示该订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/updateOrder/{orderId}")
    public Result<Order> updateOrder(@PathVariable String orderId){
        Result<Order> result = orderService.updateOrder(orderId);
        return result;

//        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getOrderId,orderId);
//        Order order = orderMapper.selectOne(wrapper);
//        return Result.success(order,"success");
    }

    /**
     * 保存修改后的订单数据
     * @param order
     * @return
     */
    @GetMapping("/saveUpdateOrder")
    public Result<Order> saveUpdateOrder(Order order){
        Result<Order> result = orderService.saveUpdateOrder(order);
        return result;
//        Order oldOrder = orderService.getById(order.getOrderId());
//        Integer status = oldOrder.getProductionStatus();
//        if ("0".equals(status.toString())){
//            System.out.println(order);
//            boolean b = orderService.updateById(order);
//            if (b){
//                return Result.success("success");
//            }else {
//                return Result.error("error:保存失败!");
//            }
//        }else {
//            return Result.error("该状态下的订单无法进行修改!");
//        }

    }

    /**
     * 查询所有订单
     * @return
     */
    @GetMapping("/selectAllOrder")
    public Result<List<Order>> selectAllOrder(){
        List<Order> orderList = orderMapper.selectList(null);
        return Result.success(orderList,"success");
    }

    /**
     * 根据订单id查询单个订单
     * @return
     */
    @GetMapping("/selectOrderById/{orderId}")
    public Result<Order> selectOrderById(@PathVariable String orderId){
        Result<Order> result = orderService.selectOrderById(orderId);
        return result;

//        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getOrderId,orderId);
//        Order order = orderMapper.selectOne(wrapper);
//        return Result.success(order,"success");
    }

    /**
     * 展示订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/showOrderDetail/{orderId}")
    public Result<Order> showOrderDetail(@PathVariable String orderId){
        Result<Order> result = orderService.showOrderDetail(orderId);
        return result;
//        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getOrderId,orderId);
//        Order order = orderMapper.selectOne(wrapper);
//        return Result.success(order,"success");
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @GetMapping("/deleteOrder/{orderId}")
    public Result<Order> deleteOrder(@PathVariable String orderId){
        Result<Order> result = orderService.deleteOrder(orderId);
        return result;
//        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getOrderId,orderId);
//        Order order = orderMapper.selectOne(wrapper);
//        System.out.println(order);
//        Integer status = order.getProductionStatus();
//        if (status == 0){
//            order.setIsDelete(1);
//            LambdaQueryWrapper<Order> wrapper1=new LambdaQueryWrapper<>();
//            wrapper1.eq(Order::getOrderId,orderId);
//            int i = orderMapper.update(order, wrapper1);
//            if (i>0){
//                return Result.success("订单删除成功！");
//            }else {
//                return Result.error("订单删除失败！");
//            }
//
//        }else {
//            return Result.error("该状态下的订单无法删除！");
//
//        }
    }

    /**
     * 订单的派发
     * @param orderId
     * @return
     */
    @GetMapping("/handOrder/{orderId}")
    @Scheduled(cron = "0 0/5 * ? * ?")
    public Result<Order> handOrder(@PathVariable String orderId){

        return Result.success(new Order(),"success");
    }

}
