package com.yanyun.ui.config;

import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.Group;
import javafx.scene.Parent;

/**
 * @description
 * @create 2017-05-20 下午2:54
 * @email spartajet.guo@gmail.com
 */
public class CustomSplash extends SplashScreen {
    /**
     * Use your own splash image instead of the default one
     * @return "/splash/javafx.png"
     */
    @Override
    public String getImagePath() {
        return super.getImagePath();
    }

    // 返回闪屏的界面
//    @Override
//    public Parent getParent() {
//        Group gp = new Group();
//        return gp;
//    }

    /**
     * Customize if the splash screen should be visible at all
     * @return true by default
     */
    @Override
    public boolean visible() {
        return true;
    }
}