package com.yanyun.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author xcai
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfo extends BaseRowModel implements Cloneable {

    /**
     * value: 表头名称
     * index: 列的号, 0表示第一列
     */
    @ExcelProperty(value = "编号", index = 0)
    private String id;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    private BigDecimal random;

    @Override
    public UserInfo clone() throws CloneNotSupportedException {
        return (UserInfo) super.clone();
    }
}