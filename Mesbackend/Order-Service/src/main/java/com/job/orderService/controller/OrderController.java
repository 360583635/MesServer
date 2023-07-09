package com.job.orderService.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.job.common.pojo.Order;
import com.job.orderService.common.result.Result;
import com.job.orderService.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 创建订单
     * @return
     */
    @GetMapping("/addOrder")
    public Result<Order> addOrder(Order order){
        //数据验证
        if (order == null){
            return  Result.error("输入对象不能为空！");
        }else if (order.getOrderNumber() != null  && order.getExpectDate() != null && order.getProductId() !=null
                && order.getType() != null &&  order.getCustomName() !=null && order.getCustomTel() !=null
                && order.getRawName() !=null && order.getRawNum() !=null){

            order.setOrderDate(new Date());
            //TODO: 2023/7/8
            order.setAuditor(null);//获取当前登录用户
            order.setPriority(0);
            order.setStatus(0);
            //TODO: 2023/7/8
            order.setOrderPrice(null);
            order.setDelete(0);
            orderMapper.insert(order);
            return Result.success("success");
        }
        else{
            return  Result.error("error!");
        }


    }

    /**
     * 修改订单页面显示该订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/updateOrder/{orderId}")
    public Result<Order> updateOrder(@PathVariable String orderId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq(orderId,orderId);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order,"success");
    }

    /**
     * 保存修改后的订单数据
     * @param order
     * @return
     */
    @GetMapping("/saveUpdateOrder/{orderId}")
    public Result<Order> saveUpdateOrder(@PathVariable String orderId, Order order){
        Integer status = order.getStatus();
        if (status == 0){
            QueryWrapper<Order> wrapper=new QueryWrapper<>();
            wrapper.eq(orderId,orderId);
            int i = orderMapper.update(order, wrapper);
            if (i>0){
                return Result.success("success");
            }else {
                return Result.error("error:保存失败!");
            }
        }else {
            return Result.error("该状态下的订单无法进行修改!");
        }

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
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq(orderId,orderId);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order,"success");
    }

    /**
     * 展示订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/showOrderDetail/{orderId}")
    public Result<Order> showOrderDetail(@PathVariable String orderId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq(orderId,orderId);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order,"success");
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @GetMapping("/deleteOrder/{orderId}")
    public Result<Order> deleteOrder(@PathVariable String orderId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq(orderId,orderId);
        Order order = orderMapper.selectOne(wrapper);
        Integer status = order.getStatus();
        if (status == 0){
            order.setDelete(1);
            QueryWrapper<Order> wrapper1=new QueryWrapper<>();
            wrapper1.eq(orderId,orderId);
            int i = orderMapper.update(order, wrapper1);
            if (i>0){
                return Result.success("订单删除成功！");
            }else {
                return Result.error("订单删除失败！");
            }

        }else {
            return Result.error("该状态下的订单无法删除！");

        }
    }

    /**
     * 订单的派发
     * @param orderId
     * @return
     */
    @GetMapping("/handOrder/{orderId}")
    public Result<Order> handOrder(@PathVariable String orderId){
        return Result.success(new Order(),"success");
    }

}
