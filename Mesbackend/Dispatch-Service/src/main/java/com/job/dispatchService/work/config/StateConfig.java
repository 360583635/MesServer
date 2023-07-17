package com.job.dispatchService.work.config;

import org.springframework.stereotype.Component;

@Component
public class StateConfig {
    public static final String CREATE_STATE = "1";
    public static final String WORKING_STATE = "2";
    public static final String EXCEPTION_STATE = "3";
    public static final String FINISH_STATE = "4";
}
