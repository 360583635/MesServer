package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

@Data
@TableName("t_roles")
public class Roles {
    private String RoleID;
    private String RoleName;
    private TimeStampedData CreateTime;
    private String CreateUser;
    private TimeStampedData UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
