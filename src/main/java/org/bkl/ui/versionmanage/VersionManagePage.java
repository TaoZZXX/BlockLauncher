package org.bkl.ui.versionmanage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.bkl.game.GameLauncher;
import org.bkl.modloader.ModLoaderType;
import org.bkl.ui.FirstPage;

public class VersionManagePage extends StackPane {

    private double xOffset = 0;
    private double yOffset = 0;
    private String mcVersion;
    private ModLoaderType modLoaderType;
    private String modLoaderVersion;

    public VersionManagePage(Stage primaryStage) {
        this.mcVersion = GameLauncher.getVersion();
        this.modLoaderType = GameLauncher.getModLoaderType();
        this.modLoaderVersion =  GameLauncher.getModLoaderVersion();

        this.setStyle("""
                -fx-background-color: pink;
                -fx-background-radius: 10;
                -fx-padding: 0;
                """);

        initUi(primaryStage);
    }

    private void initUi(Stage primaryStage) {
        HBox titleBar = new HBox();
        titleBar.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-background-radius: 5 5 0 0;"
        );
        titleBar.setAlignment(Pos.CENTER);
        titleBar.setMinHeight(30);

        Button returnButton = new Button("⇦");
        returnButton.setStyle("""
                -fx-background-color: #2196F3;
                -fx-text-fill: white;
                """);
        returnButton.setOnMouseClicked(e -> {
            FirstPage.show();
        });

        Label title = new Label("游戏版本管理");
        title.setStyle(
                "-fx-text-fill: white;" +
                " -fx-font-size: 16px;" +
                " -fx-font-weight: bold;" +
                "-fx-background-radius: 10 0 0 0;"
        );


        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, javafx.scene.layout.Priority.ALWAYS);

        Button closeButton = new Button("\uD83D\uDDD9");
        closeButton.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 900;" +
                "-fx-background-radius: 0 10 0 0;" +
                "-fx-padding: 0 10 0 0;"
        );
        closeButton.setOnAction(e -> primaryStage.close());

        Button minimizeButton = new Button("━");
        HBox.setMargin(minimizeButton, new Insets(0, 10, 0, 0));
        minimizeButton.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-padding: 0 10 0 0;"
        );
        minimizeButton.setOnAction(e -> primaryStage.setIconified(true));

        titleBar.getChildren().addAll(returnButton, title, titleSpacer, minimizeButton, closeButton);
        titleBar.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titleBar.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });


        BorderPane root = new BorderPane();
        root.setStyle("""
            -fx-background-image: url('image/bgc.jpg');
            -fx-background-size: cover;
            -fx-background-position: center;
            -fx-background-insets: 0;
        """);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        clip.setArcWidth(10);   // 根据需要调整圆角大小
        clip.setArcHeight(10);
        this.setClip(clip);

        this.getChildren().addAll(root);
        root.setTop(titleBar);
        VersionManageLeftPane versionManageLeftPane = new VersionManageLeftPane(root, mcVersion, modLoaderVersion, modLoaderType);
        root.setLeft(versionManageLeftPane);
    }
}
