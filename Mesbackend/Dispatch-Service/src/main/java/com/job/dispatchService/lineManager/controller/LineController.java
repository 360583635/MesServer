package com.job.dispatchService.lineManager.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.job.common.pojo.Line;
import com.job.common.pojo.Users;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.common.utils.JwtUtil;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.request.LinePageReq;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.feign.clients.AuthenticationClient;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.job.dispatchService.lineManager.controller.LineTaskController.findThreadByName;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-18:06
 * @description
 */

@RestController
@RequestMapping("/dispatch/line")
public class LineController {

    @Autowired
    private LineService lineService;

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private RedisCache redisCache;

    //逻辑删除1未删除0已删除
    private static int IS_DELETE_NO=1;
    private static int IS_DELETE_YES=0;

    /**
     * 流水线分页查询
     * @param req
     * @return
     */
    @PostMapping("/page")
    public Result linePage(LinePageReq req){
        return lineService.linePage(req);
    }

    @PostMapping("/likeSearch")
    public Result likeSearch(@RequestParam String searchName,@RequestParam int size,@RequestParam int current){
        return lineService.likeSearch(searchName,size,current);
    }

    /**
     * 添加流水线
     * @param pipeLine
     * @return
     */
    @PostMapping("/saveLine")
    public Result saveLine(@RequestBody Line pipeLine, HttpServletRequest request){
        return lineService.saveLine(pipeLine,request);
    }

    /**
     * 修改流水线
     * @param pipeLine
     * @return
     */
    @RequestMapping("/updateLine")
    public Result updateLine(@RequestBody Line pipeLine, HttpServletRequest request){
        return lineService.updateLine(pipeLine,request);
    }
    /**
     * 删除流水线（逻辑删除）
     * @param lineId
     * @return
     */
    @PostMapping ("/removeLine")
    public Result removeLine(@RequestParam String lineId){
        return lineService.removeLine(lineId);

    }

    /**
     * 批量逻辑删除根据id
     * @param idList
     * @return
     */
    @PostMapping("/batchRmove")
    public Result batchRemoveById(@RequestParam List<String> idList ){
        return lineService.batchRemoveById(idList);

    }

    /**
     * 查询全部流水线
     * @return
     */
    @RequestMapping("/list")
    public Result lineList(){
        return lineService.lineList();
    }

    /**
     * 根据流水线id查询流水线
     */
    @PostMapping("/queryLineById")
    public Result selectLineById(@RequestParam("lineId") String id){
        return lineService.selectLineById(id);
    }

    /**
     * 根据流程id查询流水线
     */
    @PostMapping("/queryLineByFlowId")
    public Result selectLineByFlowId(@RequestParam String flowId,@RequestParam int size,@RequestParam int current){
        return lineService.selectLineByFlowId(flowId,size,current);
    }


    /**
     * 停止流水线，将流水线状态改为 “停机” 且 将流水线实体线程停止
     * @param id
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/haltLine/{id}")
    public Result haltLine(@PathVariable("id") String id) throws InterruptedException {
        return lineService.haltLine(id);
    }

    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestParam String id) {
        return lineService.updateStatus(id);
    }


}
