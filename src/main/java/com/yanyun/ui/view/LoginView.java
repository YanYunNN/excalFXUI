package com.yanyun.ui.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

import javax.annotation.PostConstruct;

@FXMLView(value = "/views/Login.fxml", css = "/css/MainStage.css", title = "随机分组")
public class LoginView extends AbstractFxmlView {

    @PostConstruct
    protected void initUI() throws Exception {
    }
}