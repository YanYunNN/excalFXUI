package com.yanyun.utiils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.yanyun.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析监听器，
 * 每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener {

    public static List<UserInfo> datas = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        System.out.println("当前行：" + context.getCurrentRowNum());
        System.out.println(object);
        UserInfo userInfo = getRandom(object);
        datas.add(userInfo);
    }

    private UserInfo getRandom(Object object) {
        UserInfo user = (UserInfo) object;
        user.setRandom(BigDecimal.valueOf(Math.random()));
        return user;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        datas.clear();//解析结束销毁不用的资源
    }

    private void readMap(File file) throws FileNotFoundException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        FileInputStream input = new FileInputStream(file);
        List<Object> data = EasyExcelFactory.read(input, new Sheet(1, 0));
        System.out.println(data);
    }

}