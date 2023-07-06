package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

@Data
@TableName("t_menus")
public class Menus {
    private String MenuID;
    private String Name;
    private String url;
    private Integer type;
    private Integer Sort;
    private String Remark;
    private String ParentId;
    private String permission;
    //timestamp
    private TimeStampedData CreateTime;
    private String CreateUser;
    //timestamp
    private TimeStampedData UpdateTime;
    private String UpdateUser;
    private Integer Is_delete;
}
