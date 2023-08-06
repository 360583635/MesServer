package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Process;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.ProcessDto;
import com.job.dispatchService.lineManager.request.ProcessPageReq;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface ProcessService extends IService<Process> {
    Result updateProcess(ProcessDto processDto, HttpServletRequest request);

    Result saveProcess(ProcessDto processDto, HttpServletRequest request);

    Result removePeocess(String processId);

    Result batchDeleteById(List<String> idList);

    Result<ProcessPageReq> likeQuery(String searchName, int size, int current);

    Result queryEquipmentMap(HttpServletRequest request);

    Result queryProcessUpdateDtoById(String id);

    Result queryById(String id);


}