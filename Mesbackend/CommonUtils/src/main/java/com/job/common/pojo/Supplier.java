package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@Data
@TableName("t_supplier")
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {


      private  int supplierId;
      /**
       * 供应商id
       */
      private String supplierName;
     /**
      * 供应商名称
      */
     private  String  Supplier;
     /**
     * 负责人
     */
     private  String supplierNumber;
    /**
     * 负责人电话
     */
     private  String supplierAddress;
}
