package com.job.authenticationService.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Users implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    private Long user_id;
    private String Name;
    private String Salt;
    private String Password;
    private String Phone;
    private String Email;
}
