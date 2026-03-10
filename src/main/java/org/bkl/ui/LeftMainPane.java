package org.bkl.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bkl.game.GameLauncher;
import org.bkl.modloader.ModLoaderManager;
import org.bkl.modloader.ModLoaderType;

import java.util.List;


public class LeftMainPane {

    private static VBox _versionManageImgBox = null;
    private static Label _currentVersion = null;

    public LeftMainPane(VBox parent) {

        initUi(parent);
    }

    private void initUi(VBox parent) {
        parent.setStyle("""
                -fx-padding: 10;
                -fx-background-color: rgba(255, 255, 255, 0.5);
                """);

        Label accountLabel = new Label("账号");
        accountLabel.setStyle("""
                -fx-padding: 20 0 0 0;
                -fx-font-weight: 500;
                """);
        parent.getChildren().add(accountLabel);
        Separator separator1 = new Separator();
        parent.getChildren().add(separator1);

        HBox avatarHBox = new HBox();
        avatarHBox.setPrefSize(200, 80);
        avatarHBox.setAlignment(Pos.CENTER);
        avatarHBox.setStyle("""
                -fx-padding: 10 10 10 10;
                """);
        avatarHBox.setOnMouseEntered(e -> {
            avatarHBox.setStyle("""
                    -fx-background-color: rgba(0, 0, 0, 0.1);
                    """);
        });
        avatarHBox.setOnMouseExited(e -> {
            avatarHBox.setStyle("""
                    -fx-background-color: transparent;
                    """);
        });
        VBox avatarContainer = new VBox(1);
        avatarContainer.setPrefSize(35, 35);
        avatarContainer.setMaxSize(35, 35);
        avatarContainer.setMinSize(35, 35);
        avatarContainer.setStyle("""
                -fx-background-image: url("image/avatar/avatar1.png");
                -fx-background-size: cover;
                """);
        avatarHBox.getChildren().add(avatarContainer);
        VBox accountContainer = new VBox();
        accountContainer.setPrefSize(120, 35);
        accountContainer.setMaxSize(120, 35);
        accountContainer.setMinSize(120, 35);
        accountContainer.setStyle("""
                -fx-padding: 0 0 0 10;
                """);
        avatarHBox.getChildren().add(accountContainer);
        Label accountName = new Label("没有游戏账户");
        accountName.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                """);
        Label addAccount = new Label("点击添加游戏账户");
        addAccount.setStyle("""
                -fx-font-size: 12;
                """);
        accountContainer.getChildren().addAll(accountName, addAccount);
        parent.getChildren().addAll(avatarHBox);

        Label gameLabel = new Label("游戏");
        gameLabel.setStyle("""
                -fx-padding: 20 0 0 0;
                """);
        parent.getChildren().add(gameLabel);
        Separator separator2 = new Separator();
        parent.getChildren().add(separator2);

        HBox versionManageBox = new HBox();
        versionManageBox.setPrefSize(200, 50);
        versionManageBox.setMinSize(200, 50);
        versionManageBox.setMaxSize(200, 50);
        VBox.setMargin(versionManageBox, new Insets(10, 0, 0, 0));
        versionManageBox.setAlignment(Pos.CENTER);
        versionManageBox.setStyle("""
                -fx-background-color: transparent;
                """);
        versionManageBox.setOnMouseEntered(e -> {
            versionManageBox.setStyle("""
                    -fx-background-color: rgba(0, 0, 0, 0.1);
                    """);
        });
        versionManageBox.setOnMouseExited(e -> {
            versionManageBox.setStyle("""
                    -fx-background-color: transparent;
                    """);
        });
        versionManageBox.setOnMouseClicked(e -> {
            FirstPage.showVersionManegePane();
        });

        VBox versionManageImgBox = new VBox();
        versionManageImgBox.setPrefSize(35, 35);
        versionManageImgBox.setMaxSize(35, 35);
        versionManageImgBox.setMinSize(35, 35);
        _versionManageImgBox = versionManageImgBox;

        VBox versionManageLabelBox = new VBox();
        HBox.setMargin(versionManageLabelBox, new Insets(0, 0, 0, 10));
        versionManageLabelBox.setPrefSize(120, 35);
        versionManageLabelBox.setMaxSize(120, 35);
        versionManageLabelBox.setMinSize(120, 35);
        Label versionManageLabel = new Label("版本管理");
        versionManageLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: 800 !important;
                """);
        _currentVersion = new Label(GameLauncher.getVersion());

        _currentVersion.setStyle("""
                -fx-font-size: 10px;
                -fx-font-weight: 500;
                """);
        versionManageLabelBox.getChildren().addAll(versionManageLabel, _currentVersion);
        versionManageBox.getChildren().addAll(versionManageImgBox, versionManageLabelBox);
        parent.getChildren().add(versionManageBox);

        alertVersion(GameLauncher.getVersion());
    }

    /**
     * 更新主界面左侧当前版本显示
     * @param mcVersion 当前游戏版本
     */
    public static void alertVersion(String mcVersion) {
        Platform.runLater(() -> {
            _currentVersion.setText(mcVersion);

            /**
             *  检查已经安装的模组加载器
             *  如果已经安装的模组加载器的数量大于一个或者没有就使用默认照片
             */
            List<ModLoaderType> modLoaderTypes = ModLoaderManager.checkInstalledModLoaders(GameLauncher.getVersion());
            if (modLoaderTypes.size() != 1) {
                GameLauncher.setModLoader(null);
                _versionManageImgBox.setStyle("""
                -fx-background-image: url("image/versionlogo.png");
                -fx-background-size: cover;
                """);
            } else {
                GameLauncher.setModLoader(modLoaderTypes.get(0));

                switch (modLoaderTypes.get(0)){
                    case FABRIC -> {
                        _versionManageImgBox.setStyle("""
                        -fx-background-image: url("image/modloaderlogo/fabric.png");
                        -fx-background-size: cover;
                        """);
                        break;
                    }
                    case FORGE -> {
                        _versionManageImgBox.setStyle("""
                        -fx-background-image: url("image/modloaderlogo/forge.jpeg");
                        -fx-background-size: cover;
                        """);
                        break;
                    }
                    case QUILT -> {
                        _versionManageImgBox.setStyle("""
                        -fx-background-image: url("image/modloaderlogo/quilt.png");
                        -fx-background-size: cover;
                        """);
                        break;
                    }
                    case OPTIFINE -> {
                        _versionManageImgBox.setStyle("""
                        -fx-background-image: url("image/modloaderlogo/optifine.png");
                        -fx-background-size: cover;
                        """);
                        break;
                    }
                    default -> {

                    }
                }
            }
        });
    }

}
