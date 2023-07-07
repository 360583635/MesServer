package com.job.dispatchService.LineManager.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 庸俗可耐
 * @create 2023-07-06-19:34
 * @description 流程与工序穿梭数据展示对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessVo {
    /**
     * 具体的存储的ID值
     */
    private String value;
    /**
     * 显示标题
     */
    private String title;
}
