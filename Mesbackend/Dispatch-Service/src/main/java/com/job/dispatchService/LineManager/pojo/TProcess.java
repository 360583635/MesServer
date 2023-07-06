package com.job.dispatchService.LineManager.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_process")
public class TProcess {
    /**
     * 工序ID
     */
    private String id;

    /**
     * 工序
     */
    private String process;

    /**
     * 工序描述
     */
    private String processDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUsername;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUsername;
}