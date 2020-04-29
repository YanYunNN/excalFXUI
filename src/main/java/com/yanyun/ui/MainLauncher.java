package com.yanyun.ui;


import com.yanyun.ui.config.CustomSplash;
import com.yanyun.ui.view.LoginView;
import com.yanyun.ui.view.MainStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MainLauncher extends AbstractJavaFxApplicationSupport {

    /**
     * The entry point of application.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(MainLauncher.class, MainStageView.class, new CustomSplash(), args);
    }

    /**
     * Start.
     * @param stage the stage
     * @throws Exception the exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        setTitle("Excel随机分组V1.0");
    }


    public void relaunch() {
        Platform.runLater(() -> {
            getStage().close();
            try {
                this.stop();
                this.init();
                this.start(new Stage());
            } catch (Exception e) {
                log.error("e:{}", e);
            }
        });
    }
}