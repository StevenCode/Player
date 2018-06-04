package com.steven;

import com.steven.utils.ResourceManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Player extends Application{
    TopContainer top;

    @Override
    public void start(Stage pstage) throws Exception {
        ViewsContext.initStage(pstage);

        pstage.initStyle(StageStyle.TRANSPARENT);
        pstage.setResizable(false);
        pstage.setTitle("ZPlayer");
        pstage.getIcons().add(ResourceManager.loadClasspathImage("icon.png"));

        Rectangle clip = new Rectangle(300, 635);
        clip.setArcHeight(10.0);
        clip.setArcWidth(10.0);
        VBox root = new VBox();
        root.setId("bg");
        root.setPrefSize(300, 635);
        root.getStylesheets().add(ResourceManager.getResourceUrl("/com/steven/index.css"));

        top = new TopContainer();
        root.getChildren().add(top.getView());

        TrayManager.getTrayManager().initTray();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        pstage.setScene(scene);

        // 启用移动stage
        StageDragListener listener = new StageDragListener(ViewsContext.stage());
        listener.enableDrag(top.getView());
        pstage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
