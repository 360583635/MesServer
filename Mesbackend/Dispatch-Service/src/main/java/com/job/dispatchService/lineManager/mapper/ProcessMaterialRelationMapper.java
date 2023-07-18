package com.job.dispatchService.lineManager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Material;
import com.job.common.pojo.ProcessMaterialRelation;
import com.job.dispatchService.lineManager.vo.MaterialVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-14:51
 * @description
 */
public interface ProcessMaterialRelationMapper extends BaseMapper<ProcessMaterialRelation> {
    /**
     * 根据工序ID查询下挂的全部原材料
     * @param processId
     * @return 工序与原材料关系集合
     */
    List<MaterialVo> queryMaterialRelationByProcessId(@Param("processId") String processId);

    /**
     * 删除流程下挂的全部工序
     * @param processId
     */
    void  deleteMaterialRelationByProcessId(@Param("processId") String processId);
}
