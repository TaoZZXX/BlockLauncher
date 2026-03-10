package org.bkl.ui.versionmanage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bkl.game.MinecraftPath;
import org.bkl.modloader.ModLoaderType;

public class VersionManageGlobalPane extends VBox {
    public static String mcVersion = null;
    public static ModLoaderType modLoaderType = null;
    public static String modLoaderVersion = null;
    public static String mcPath = null;

    public VersionManageGlobalPane() {
        mcPath = MinecraftPath.getMinecraftPath();

        this.setPrefSize(600, 470);
        this.setMinSize(600, 470);
        this.setMaxSize(600, 470);
        this.setStyle("""
                    -fx-background-color: rgba(245, 245, 245, 0.8);
                """);
        initUi();
    }

    private void initUi() {
        // 顶部条
        HBox top = new HBox();
        top.setPrefSize(600, 60);
        top.setMinSize(600, 60);
        top.setMaxSize(600, 60);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("""
                    -fx-background-color: rgba(255, 255, 255, 0.5);
                    -fx-border-radius: 5 5 0 0;
                """);

        Label titleLabel = new Label("全局管理");
        titleLabel.setStyle("""
                    -fx-font-size: 18px;
                    -fx-font-weight: 700;
                    -fx-text-fill: #333333;
                """);
        top.getChildren().add(titleLabel);

        VBox content = new VBox();
        content.setPrefSize(580, 390);
        content.setMinSize(580, 390);
        content.setMaxSize(580, 390);
        VBox.setMargin(content, new Insets(10, 10, 10, 10));
        content.setStyle("""
                    -fx-background-color: rgba(255, 255, 255, 0.5);
                    -fx-background-radius: 5;
                    -fx-padding: 10;
                """);

        Label gameVersionLabel = new Label("游戏版本: " + mcVersion);
        gameVersionLabel.setStyle("""
                    -fx-font-size: 16px;
                    -fx-text-fill: #444444;
                """);

        Label modLoaderVersionLabel = new Label("模组加载器版本：" + modLoaderType + "：" + modLoaderVersion);
        modLoaderVersionLabel.setStyle("""
                    -fx-font-size: 16px;
                    -fx-text-fill: #444444;
                """);

        Label gameInstallPath = new Label("游戏安装路径：" + this.mcPath);
        gameInstallPath.setStyle("""
                    -fx-font-size: 16px;
                    -fx-text-fill: #444444;
                """);

        content.getChildren().addAll(gameVersionLabel, modLoaderVersionLabel, gameInstallPath);

        this.getChildren().addAll(top, content);
    }
}
