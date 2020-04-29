package com.yanyun.utiils;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yanyun.entity.UserInfo;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListUtil {
    /**
     * @param source
     * @param n      每次分割的个数
     * @return java.util.List<java.util.List < T>>
     * @Title: 将list按照指定元素个数(n)分割
     * @methodName: partList
     * @Description: 如果指定元素个数(n)>list.size(),则返回list;这时候商:0；余数:list.size()
     */
    public static <T> List<List<T>> partList(List<T> source, int n) {
        if (source == null) {
            return null;
        }
        if (n == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        // 集合长度
        int size = source.size();
        // 余数
        int remaider = size % n;
        System.out.println("余数:" + remaider);
        // 商
        int number = size / n;
        System.out.println("商:" + number);
        for (int i = 0; i < number; i++) {
            List<T> value = source.subList(i * n, (i + 1) * n);
            result.add(value);
        }
        if (remaider > 0) {
            List<T> subList = source.subList(size - remaider, size);
            result.add(subList);
        }
        return result;
    }

    /**
     * @param source
     * @param n      等分个数
     * @return java.util.List<java.util.List < T>>
     * @Title: 将一个list均分成n个list, 主要通过偏移量来实现的
     * @methodName: averageList
     */
    public static <T> List<List<T>> averageList(List<T> source, int n) {
        if (source == null) {
            return null;
        }
        if (n == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        // 集合长度
        int size = source.size();
        // 余数
        int remaider = size % n;
        System.out.println("余数:" + remaider);
        // 商
        int number = size / n;
        System.out.println("商:" + number);

        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static void outputExcel(File inFile, int groupNum, String outFile) throws FileNotFoundException {
        //读Excel
        FileInputStream inputStream = new FileInputStream(inFile);
        AnalysisEventListener listener = new ExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLS, null, listener);
        excelReader.read(new Sheet(1, 1, UserInfo.class));
        List<UserInfo> datas = ExcelListener.datas;

        //随机种子
        datas.sort((o1, o2) -> {
            double value = o1.getRandom().subtract(o2.getRandom()).doubleValue();
            return (int) (value * 100000000);
        });

        //分组
        List<List<UserInfo>> subLists = averageList(datas, groupNum);

        //写Excel
        OutputStream out = new FileOutputStream(outFile);
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLS);
        for (int i = 0; i < groupNum; i++) {
            Sheet sheet = new Sheet(i + 1, 0, UserInfo.class);
            sheet.setSheetName("名单".concat(String.valueOf(i + 1)));
            writer.write(subLists.get(i), sheet);
        }
        //解析结束销毁不用的资源
        writer.finish();
        datas.clear();
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
