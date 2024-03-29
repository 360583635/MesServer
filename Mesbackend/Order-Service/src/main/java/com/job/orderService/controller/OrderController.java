package com.job.orderService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Order;
import com.job.common.redis.RedisCache;

import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.orderService.mapper.OrderMapper;
import com.job.orderService.service.OrderService;
import com.job.orderService.service.UsersService;
import com.job.orderService.vo.FlowVo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.LongAdder;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderService orderService;
    private RedisCache redisCache;
    @Autowired
    public void setRedisCache(RedisCache redisCache){
        this.redisCache = redisCache;
    }
    @Autowired
    private UsersService usersService;



    @PostMapping("/updateByOne")
    public Result<Order> updateByOne(@RequestBody Order order){
        boolean b = orderService.updateById(order);
        if(b){
            return Result.success(null,"修改成功");
        }
        return Result.error("修改失败");
    }

    @GetMapping("/toAddOrder")
   public Result<List<FlowVo>> toAddOrder(){
        System.out.println("开始初始化~~");
        //LongAdder
       Result<List<FlowVo>> result = orderService.toAddOrder();
        System.out.println(result);
       return result;
   }

    /**
     * 创建订单
     * @return
     */
    @PostMapping("/addOrder")
    public Result<Object> addOrder(@RequestBody Order order,HttpServletRequest request){
        System.out.println(order);
        String token=request.getHeader("token");
        System.out.println(token);
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            System.out.println(userId);
            String name = usersService.getById(userId).getName();
            order.setAuditor(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        Result<Object> result = orderService.addOrder(order);
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
    @GetMapping("/updateOrder")
    public Result<Order> updateOrder(@RequestParam String orderId){
        Result<Order> result = orderService.updateOrder(orderId);
        return result;


    }

    /**
     * 保存修改后的订单数据
     * @param order
     * @return
     */
    @PostMapping("/saveUpdateOrder")
    public Result<Order> saveUpdateOrder(@RequestBody Order order){
        Result<Order> result = orderService.saveUpdateOrder(order);
        return result;


    }

    /**
     * 查询所有订单
     * @return
     */
    @GetMapping("/selectAllOrder")
    public Result<List<Order>> selectAllOrder(){
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getIsDelete,0);
        List<Order> orderList = orderMapper.selectList(wrapper);
        return Result.success(orderList,"success");
    }

    /**
     * 根据产品名称查询订单
     * @return
     */
    @GetMapping("/selectOrderByName")
    public Result<List<Order>> selectOrderByName(@RequestParam String productName){
        Result<List<Order>> result = orderService.selectOrderByName(productName);
        return result;

    }

    /**
     * 展示订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/showOrderDetail")
    public Result<Order> showOrderDetail(@RequestParam String orderId){
        Result<Order> result = orderService.showOrderDetail(orderId);
        return result;

    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @GetMapping("/deleteOrder")
    public Result<Order> deleteOrder(@RequestParam String orderId){
        Result<Order> result = orderService.deleteOrder(orderId);
        return result;

    }

    /**
     * 批量逻辑删除
     * @param
     * @return
     */
    @PostMapping("/deleteMuch")
    public Result deleteMuchById(@RequestBody Map<String,List<String>> map){
        List<String> idList = map.get("idList");
        // 获取需要逻辑删除的记录的ID列表
        Vector<Order> recordList = new Vector<>();

        for (String id : idList) {
            Order getById = orderService.getById(id);
            Integer productionStatus = getById.getProductionStatus();
            if (productionStatus==0 || productionStatus==1){
                Order order=new Order();
                order.setOrderId(id);
                order.setIsDelete(1);  // 设置要更新的字段和值
                recordList.add(order);
            }
        }
        boolean b = orderService.updateBatchById(recordList);
        if(b){
            return Result.success(null,"删除成功");
        }
        return Result.error("删除失败");

    }
    /**
     * 订单的派发
     * @return
     */
//    @GetMapping("/handOrder")
////    @Scheduled(cron = "0 0/5 * ? * ?")
//    public Result<Order> handOrder(){
//        PriorityQueue<Integer> a = new PriorityQueue<>();
//        a.offer(2);
//        a.offer(5);
//        a.offer(6);
//        redisCache.setCacheObject("a",a);
//        JSONArray jsonArray  = redisCache.getCacheObject("a");
//        System.out.println(jsonArray.size());
//        PriorityQueue<Integer> b = new PriorityQueue<>();
//        for (Object o : jsonArray) {
//            System.out.println(o);
//            b.offer((Integer) o);
//        }
//        while (!b.isEmpty()){
//            System.out.println(b.poll());
//        }
//        return null;
//    }

}
