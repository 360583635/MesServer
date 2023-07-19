package com.job.dispatchService.lineManager.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-15:10
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialVo {
    /**
     * 具体的存储的ID值
     */
    private String value;
    /**
     * 显示标题
     */
    private String title;
    /**
     * 原材料数目
     */
    private String number;
}
