package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

@Data
@TableName("t_users_roles")
public class UsersRoles {
    private String UserId;
    private String RoleId;
    private TimeStampedData CreateTime;
    private String CreateUser;
    private TimeStampedData UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
