package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_users")
public class Users implements Serializable {
    private String Id;
    private String Password;
    private String Salt;
    private String Name;
    private String Phone;
    private String Email;
    private String Address;
    private TimeStampedData EnterTime;
    private String Sex;
    private TimeStampedData CreateTime;
    private String CreateUser;
    private TimeStampedData UpdateTime;
    private String UpdateUser;
    private Integer State;
    private TimeStampedData Birth;
    private Integer isDelete;
}
