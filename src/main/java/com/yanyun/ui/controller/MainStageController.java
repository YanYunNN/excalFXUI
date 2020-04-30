package com.yanyun.ui.controller;


import com.yanyun.ui.MainLauncher;
import com.yanyun.ui.view.MainStageView;
import com.yanyun.utiils.ListUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class MainStageController implements Initializable {

    @FXML
    private TextField groupNum;
    @FXML
    private Label showFile;

    private static File file;

    private ResourceBundle resourcebundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourcebundle = resources;
    }

    /**
     * 取值
     */
    public void btnOnClear(ActionEvent actionEvent) {
        groupNum.setText("");
    }

    public void btnOnSelect(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择Excel文件");
        Stage selectFile = new Stage();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        file = fileChooser.showOpenDialog(selectFile);
        if (file == null) {
        } else {
            showFile.setVisible(true);
            showFile.setAlignment(Pos.CENTER_RIGHT);
            showFile.setText(file.getName());
        }
    }

    @FXML
    public void btnOnSubmit(ActionEvent actionevent) throws RuntimeException {
        try {
            //校验
            String text = groupNum.getText();
            if ("".equals(text) || !ListUtil.isNumeric(text)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("错误！");
                alert.setContentText("请您输入正确的分组数！");
                alert.showAndWait();
                throw new RuntimeException("error num");
            }
            Integer num = Integer.parseInt(groupNum.getText());
            if (num == null || num == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("错误！");
                alert.setContentText("请您输入正确的分组数！");
                alert.showAndWait();
                throw new RuntimeException("error num");
            }
            log.info("groupNum:{}", groupNum.getText());

            //保存分组文件
            String saveFilePath = saveFile(file, num);
            if (saveFilePath != null) {
                log.info("---------分组成功-------");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("OK");
                alert.setHeaderText("成功！");
                alert.setContentText(String.format("保存到：%s", saveFilePath));
                alert.showAndWait();
            }
        } catch (RuntimeException | IOException e) {
            log.error("btnOnSubmit:{}", e.getCause());
        }
    }


    /**
     * 切换视图
     * @param actionevent
     */
    @FXML
    public void btnloginclick(ActionEvent actionevent) {
        MainLauncher.showView(MainStageView.class);
    }


    private String saveFile(File file, Integer num) throws IOException {
        //保存文件选择器
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Excel");
        Stage saveFile = new Stage();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx")
        );
        String[] str = file.getName().split("\\.");
        String filename = String.format("%s-%s.%s", str[0], LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")), str[1]);
        chooser.setInitialFileName(filename);
        File outFile = chooser.showSaveDialog(saveFile);
        if (outFile != null) {
            //写入文件
            ListUtil.outputExcel(file, num, outFile.getCanonicalPath());
            return filename;
        } else return null;

    }

}
