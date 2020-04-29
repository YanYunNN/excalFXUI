package com.yanyun;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yanyun.entity.UserInfo;
import com.yanyun.ui.MainLauncher;
import com.yanyun.utiils.ExcelListener;
import com.yanyun.utiils.ListUtil;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExcelApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1() {
        double random = Math.random();
        log.info("random:{}", random);
    }

    @Test
    public void readMap1() throws FileNotFoundException {
        File file = new File("c:/map/示例分班表.xls");

        //读Excel
        FileInputStream inputStream = new FileInputStream(file);
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
        int groupNum = 5;
        List<List<UserInfo>> subLists = ListUtil.averageList(datas, groupNum);
        //写Excel
        OutputStream out = new FileOutputStream("c:/map/分班表.xls");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLS);
        for (int i = 0; i < groupNum; i++) {
            Sheet sheet = new Sheet(i + 1, 0, UserInfo.class);
            sheet.setSheetName("名单".concat(String.valueOf(i+1)));
            writer.write(subLists.get(i), sheet);
        }
        writer.finish();
    }

}
