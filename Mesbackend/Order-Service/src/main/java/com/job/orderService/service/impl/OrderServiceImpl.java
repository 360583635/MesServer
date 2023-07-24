package com.job.orderService.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.*;
import com.job.common.redis.RedisCache;
import com.job.feign.clients.DispatchClient;
import com.job.feign.clients.ProductionManagementClient;
import com.job.orderService.common.result.Result;
import com.job.orderService.mapper.FlowMapper;
//import com.job.orderService.mapper.LineMapper;
import com.job.orderService.mapper.LineMapper;
import com.job.orderService.mapper.OrderMapper;
import com.job.orderService.mapper.ProduceMapper;
import com.job.orderService.service.OrderService;
import com.job.orderService.vo.FlowVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.rmi.MarshalledObject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private FlowMapper flowMapper;
    @Autowired
    private LineMapper lineMapper;
    @Autowired
    private ProduceMapper produceMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private DispatchClient dispatchClient;
    @Autowired
    private ProductionManagementClient productionManagementClient;
    @Autowired
    private RestTemplate restTemplate;
    private final Lock lock = new ReentrantLock();
    private final String urlFlow = "http://127.0.0.1:6031/dispatch/process/material/queryMaterialsByFlowName";

    /**
     * 创建订单界面初始化
     *
     * @return
     */
    @Override
    public Result<List<FlowVo>> toAddOrder() {
        //获取产品类型名称
        LambdaQueryWrapper wrapper = new LambdaQueryWrapper();
        List<Flow> flowList = flowMapper.selectList(wrapper);
        List<FlowVo> flowVosList = new ArrayList<>();
        for (Flow flow : flowList) {
            FlowVo flowVo = new FlowVo();
            flowVo.setText(flow.getFlow());
            flowVo.setValue(flow.getId());
            System.out.println(1);
            Map<String,String> map = new HashMap<>();
            map.put("flowName",flow.getFlow());
            //Map<String, Integer> materialsList = dispatchClient.queryMaterialsByFlowName(flow.getFlow());
            Map<String, Integer> materialsList = restTemplate.postForObject(urlFlow, map, Map.class);
            System.out.println(materialsList);
            System.out.println(2);
            flowVo.setMaterial(materialsList);
            flowVosList.add(flowVo);
        }
        if (flowVosList.size()!= 0) {
            return Result.success(flowVosList, "success");
        } else {
            return Result.error("error");
        }
    }


    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    @Override
    public Result<Order> addOrder(Order order) {
        //数据验证
        if (order == null) {
            return Result.error("输入对象不能为空！");
        } else if (order.getOrderNumber() != null && order.getExpectDate() != null && order.getProductId() != null
                && order.getProductName() != null && order.getCustomName() != null && order.getCustomTel() != null
                && order.getRawName() != null && order.getRawNum() != null && order.getPriority() != null) {

            order.setOrderDate(new Date());
            order.setProductionStatus(0);
            //查询产品单价
            LambdaQueryWrapper<Produce> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Produce::getProcessName, order.getProductName());
            Produce produce = produceMapper.selectOne(wrapper);
            int producePrice = produce.getProducePrice();
            order.setOrderPrice(order.getOrderNumber() * producePrice);
            order.setIsDelete(0);
            //计算订单原材料
            Map<String, Integer> rawMap = dispatchClient.queryMaterialsByFlowName(order.getProductName());
            Set<Map.Entry<String, Integer>> rawSet = rawMap.entrySet();
            for (Map.Entry<String, Integer> rawset : rawSet) {
                rawMap.put(rawset.getKey(), rawset.getValue() * order.getOrderNumber());
            }
            //查询原材料库存
            Map<String, Integer> materials = dispatchClient.queryMaterialsByFlowName(order.getProductName());
            Set<String> keySet = materials.keySet();
            for (String material : keySet) {
                Integer materialNum = productionManagementClient.queryMaterialNumberByMaterialName(material);
                Integer rawNum = rawMap.get(material);
                if (materialNum < rawNum) {
                    return Result.error("error:原材料数量不足，创建失败！");
                }
            }
            //创建完成
            int i = orderMapper.insert(order);

            if (i > 0) {
                //此时开始派发订单
                LambdaQueryWrapper<Flow> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Flow::getFlow, order.getProductName());
                Flow flow = flowMapper.selectOne(wrapper1);
                System.out.println(flow);
                String flowId = flow.getId();
                LambdaQueryWrapper<Line> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(Line::getLineFlowId, flowId).orderByAsc(Line::getOrderCount);
                List<Line> lineList = lineMapper.selectList(wrapper2);
                Line line = lineList.get(0);
                String lineId = line.getId();
                order.setProductLine(lineId);
                order.setProductionStatus(1);
                int j = orderMapper.updateById(order);
                PriorityQueue<Order> qq = new PriorityQueue<>(
                        (o1, o2) -> !Objects.equals(o1.getPriority(), o2.getPriority())
                                ? o1.getPriority() - o2.getPriority()
                                : (o1.getExpectDate().getTime() < o2.getExpectDate().getTime())
                                ? -1
                                : 1);
                //创建优先队列并存入redis
                lock.lock();
                JSONArray orderPQ;
                try {
                    orderPQ = redisCache.getCacheObject("orderPQ");
                } finally {
                    lock.unlock();
                }
                if(!orderPQ.isEmpty()){
                    for (Object o : orderPQ) qq.offer((Order) o);
                }
                lock.lock();
                try {
                    redisCache.setCacheObject("orderPQ", qq);
                } finally {
                    lock.unlock();
                }
                return Result.success("success，订单创建且派发成功");
            } else {
                return Result.error("error,订单创建成功但派发失败!");
            }
        } else {
            return Result.error("error!");
        }
    }

    /**
     * 修改订单页面显示该订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public Result<Order> updateOrder(String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderId, orderId);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order, "success");
    }

    /**
     * 保存修改后的订单数据
     *
     * @param order
     * @return
     */
    @Override
    public Result<Order> saveUpdateOrder(Order order) {
        Order oldOrder = this.getById(order.getOrderId());
        Integer status = oldOrder.getProductionStatus();
        if ("0".equals(status.toString())) {
            System.out.println(order);
            boolean b = this.updateById(order);
            if (b) {
                return Result.success("success");
            } else {
                return Result.error("error:保存失败!");
            }
        } else {
            return Result.error("该状态下的订单无法进行修改!");
        }

    }

    /**
     * 根据订单id查询单个订单
     *
     * @param productName
     * @return
     */
    @Override
    public Result<List<Order>> selectOrderByName(String productName) {
        System.out.println(productName);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //wrapper.like(Order::getProductName,productName);
        wrapper.like(Order::getProductName, productName);
        List<Order> orderList = orderMapper.selectList(wrapper);
        if (orderList != null) {
            return Result.success(orderList, "success");
        } else
            return Result.error("查询失败");
    }

    /**
     * 展示订单详情
     *
     * @param orderId
     * @return
     */
    @Override
    public Result<Order> showOrderDetail(String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderId, orderId);
        Order order = orderMapper.selectOne(wrapper);
        return Result.success(order, "success");
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Result<Order> deleteOrder(String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderId, orderId);
        Order order = orderMapper.selectOne(wrapper);
        System.out.println(order);
        Integer status = order.getProductionStatus();
        if (status == 0) {
            order.setIsDelete(1);
            LambdaQueryWrapper<Order> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Order::getOrderId, orderId);
            int i = orderMapper.update(order, wrapper1);
            if (i > 0) {
                return Result.success("订单删除成功！");
            } else {
                return Result.error("订单删除失败！");
            }

        } else {
            return Result.error("该状态下的订单无法删除！");

        }
    }


    /**
     * 订单派发
     * @param orderId
     * @return
     */
//    @Override
//    public Result<Order> handOrder(String orderId) {
//        LambdaQueryWrapper<Order> wrapper=new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getOrderId,orderId);
//        Order order = orderMapper.selectOne(wrapper);
//        Integer status=order.getProductionStatus();
//        String typeName = order.getTypeName();
//        if (status==0){
//            LambdaQueryWrapper<Flow> wrapper1=new LambdaQueryWrapper<>();
//            wrapper1.eq(Flow::getFlow,typeName);
//            Flow flow = flowMapper.selectOne(wrapper1);
//            String flowId = flow.getId();
//            LambdaQueryWrapper<Line> wrapper2=new LambdaQueryWrapper<>();
//            wrapper2.eq(Line::getLineFlowId,flowId).orderByAsc(Line::getOrderCount);
//            Line line = lineMapper.selectOne(wrapper2);
//            if (line!=null){
//                String lineId = line.getId();
//                order.setProductLine(lineId);
//                order.setProductionStatus(1);
//
//                //TODO 修改redis里面的数据
//                RedisCache redisCache=new RedisCache();
//
//                return Result.success("流水线派发成功！");
//            }else {
//                return Result.error("未匹配到流水线，派发失败！");
//            }
//        }else {
//            return Result.error("该状态下的订单不在派发范围内！");
//        }
//    }


}
