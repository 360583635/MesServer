package com.job.dispatchService.lineManager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Flow;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.redis.RedisCache;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.dto.FlowDto;
import com.job.dispatchService.lineManager.vo.ProcessVo;
import com.job.feign.clients.AuthenticationClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FlowProcessRelationService extends IService<FlowProcessRelation> {


    /**
     * 流程与工序关系新增与修改
     *
     * @param FlowDto 流程信息DTO
     * @return 执行结果
     * @throws Exception 异常
     */
    Result addOrUpdate(FlowDto FlowDto) throws Exception;

    /**
     * 全部工序集合
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<ProcessVo> allProcessViewServer() throws Exception;

    /**
     * 绘制当前流程下的工序
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<ProcessVo> currentProcessViewServer(String flowId) throws Exception;


    public Result addOrUpdateService( FlowDto flowDto, HttpServletRequest request) throws Exception;

    public Result deleteByTableNameId(FlowDto req);
}