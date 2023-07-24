package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_flow")
public class Flow {

   // 流程ID
   @TableId(type = IdType.ASSIGN_ID)
    private String id;


    //流程
    private String flow;


    //流程描述
    private String flowDesc;


    //流程时序绘制
    private String process;


    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;


    //创建人
    private String createUsername;


    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;

    //修改人
    private String updateUsername;

    //删除状态
    private Integer isDelete;



    @Override
    public String toString() {
        return "TFlow{" +
                "id='" + id + '\'' +
                ", flow='" + flow + '\'' +
                ", flowDesc='" + flowDesc + '\'' +
                ", process='" + process + '\'' +
                ", createTime=" + createTime +
                ", createUsername='" + createUsername + '\'' +
                ", updateTime=" + updateTime +
                ", updateUsername='" + updateUsername + '\'' +
                '}';
    }
}
