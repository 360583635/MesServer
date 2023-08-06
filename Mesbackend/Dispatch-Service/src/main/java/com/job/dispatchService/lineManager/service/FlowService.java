package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Flow;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FlowService extends IService<Flow> {


    public Result flowPage(FlowPageReq req);
    public Result flowList();
    public Result flowSave(Flow flow, HttpServletRequest request);

    public Result FlowRemove(String flowId);

    public Result batchDeleteById(List<String> idList);

    public Result flowQueryByid(String flowId);

    public Result likeSearch(String searchName,int size,int current );

    public Result queryFlowType();

    public Result flowProcessByFlowId(String flowId);

    public Result updateFlowById(Flow flow,HttpServletRequest request);

    public Result queryProduceName(HttpServletRequest request);
}