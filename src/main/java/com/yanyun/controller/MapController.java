package com.yanyun.controller;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yanyun.entity.RestResponse;
import com.yanyun.entity.UserInfo;
import com.yanyun.utiils.ExcelListener;
import com.yanyun.utiils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/map")
@Slf4j
public class MapController {
    @Value("${baiduMap.path}")
    private String path;

    @PostMapping("/upload1")
    public RestResponse upload1(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponse.bad(-1, "file empty");
        }
        File destFile = new File(path + file.getOriginalFilename());
        try {
            file.transferTo(destFile);
            log.info(String.format("uploading file: %s \n", file.getOriginalFilename()));
//            readMap(destFile);
            return RestResponse.good(destFile.getAbsolutePath());
        } catch (IOException e) {
            log.info(String.format("上传失败 file: %s \n", file.getOriginalFilename()));
        }
        return RestResponse.bad(-1, "上传失败");
    }


    @PostMapping("/upload")
    public RestResponse upload(@RequestParam("file") MultipartFile file,
                               @RequestParam("groupNum") Integer groupNum,
                               HttpServletResponse response) throws IOException {
        //文件名校验
        String[] str = file.getOriginalFilename().split("\\.");
        if (!"xls".equals(str[1]) && !"xlsx".equals(str[1])) {
            return RestResponse.bad(-1, "请上传xls或者xlxs文件！");
        }
        if (groupNum == null || groupNum == 0) {
            return RestResponse.bad(-2, "请输入分组数！");
        }

        //读Excel
        InputStream inputStream = file.getInputStream();
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
        List<List<UserInfo>> subLists = ListUtil.averageList(datas, groupNum);

        //写入输出流并下载
        String filename = String.format("%s-%s.%s", str[0], LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")), str[1]);
        try {
            response.setHeader("Content-disposition", String.format("attachment;filename=%s", URLEncoder.encode(filename, "UTF-8")));
            //写Excel
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLS);
            for (int i = 0; i < groupNum; i++) {
                Sheet sheet = new Sheet(i + 1, 0, UserInfo.class);
                sheet.setSheetName("名单".concat(String.valueOf(i + 1)));
                writer.write(subLists.get(i), sheet);
            }
            //解析结束销毁不用的资源
            writer.finish();
            datas.clear();
            response.flushBuffer();
            return RestResponse.good("文件下载成功！");
        } catch (IOException e) {
            response.flushBuffer();
            e.printStackTrace();
            return RestResponse.bad(-2, "Error");
        }
    }
}
