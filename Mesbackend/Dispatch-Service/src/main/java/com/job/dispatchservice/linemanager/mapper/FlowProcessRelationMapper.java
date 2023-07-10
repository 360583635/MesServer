package com.job.dispatchservice.linemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.FlowProcessRelation;
import com.job.dispatchservice.linemanager.vo.ProcessVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-06-17:44
 * @description
 */
public interface FlowProcessRelationMapper extends BaseMapper<FlowProcessRelation> {
    /**
     * 根据流程ID查询下挂的全部工序
     * @param flowId 流程关联ID
     * @return 工序关系集合
     */
    List<ProcessVo> queryOperRelationByFlowId(@Param("flowId") String flowId);

    /**
     * 删除流程下挂的全部工序
     * @param flowId 流程关联ID
     */
    void  deleteProcessRelationByFlowId(@Param("flowId") String flowId);

}
