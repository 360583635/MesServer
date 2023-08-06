package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Line;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.request.LinePageReq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface LineService extends IService<Line> {
    public Result linePage(LinePageReq req);

    public Result likeSearch(String searchName, int size, int current);

    public Result saveLine(Line pipeLine, HttpServletRequest request);

    public Result updateLine(Line pipeLine, HttpServletRequest request);

    public Result removeLine(String lineId);

    public Result batchRemoveById(List<String> idList);

    public Result lineList();

    public Result selectLineById(String id);

    public Result selectLineByFlowId(String flowId,int size,int current);

    public Result haltLine(String id);

    public Result updateStatus(@RequestParam String id);
}