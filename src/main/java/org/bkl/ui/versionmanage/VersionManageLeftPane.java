package org.bkl.ui.versionmanage;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;
import org.bkl.modloader.ModLoaderType;


/**
 * @author LongWei
 * @date 2025/10/2 22:38
 */
public class VersionManageLeftPane extends VBox {
    private static final Logger log = LoggerFactory.getLogger(VersionManageLeftPane.class.getName());
    private static final String BOX_SELECTED_STYLE = "-fx-background-color: rgba(0, 0, 0, 0.1);";
    private static final String BUTTON_SELECTED_STYLE = """
            -fx-background-color: transparent;
            -fx-text-fill: #000000;
            -fx-font-weight: bold;
            -fx-font-size: 14;

            """;
    private static final String BOX_UNSELECTED_STYLE = "-fx-background-color: transparent;";
    private static final String BUTTON_UNSELECTED_STYLE = """
            -fx-background-color: transparent;
            -fx-text-fill: #000000;
            -fx-font-size: 14;
            """;

    private HBox selectedBox;
    private Button selectedButton;
    private BorderPane root;
    private int selectedIndex = 0;
    public static String mcVersion;
    public static String modLoaderVersion;
    public static ModLoaderType modLoaderType;

    /**
     * @param root
     * @param version 游戏版本
     * @param modLoaderVersion 模组加载器版本
     * @param modLoaderType 模组加载器类型
     */
    public VersionManageLeftPane(BorderPane root, String version, String modLoaderVersion, ModLoaderType modLoaderType) {
        this.root = root;
        VersionManageLeftPane.mcVersion = version;
        VersionManageLeftPane.modLoaderVersion = modLoaderVersion;
        VersionManageLeftPane.modLoaderType = modLoaderType;

        this.setPrefWidth(200);
        this.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        initUi();
    }

    public void initUi() {
        HBox versionNameBox = new HBox();
        Button versionNameLabel = new Button("    版本：" + this.mcVersion);
        versionNameBox.getChildren().add(versionNameLabel);
        mouseEnAndExStyle(versionNameBox, versionNameLabel);
        versionNameBox.setOnMouseClicked(e -> {
            selectedIndex = 0;
            setSelected(versionNameBox, versionNameLabel);
        });
        versionNameLabel.setOnMouseClicked(e -> {
            selectedIndex = 0;
            setSelected(versionNameBox, versionNameLabel);
        });

        HBox autoInstallBox = new HBox();
        Button autoInstallButton = new Button("♕   自动安装");

        autoInstallBox.setStyle(BOX_UNSELECTED_STYLE);
        autoInstallButton.setStyle(BUTTON_UNSELECTED_STYLE);
        selectedBox = autoInstallBox;
        selectedButton = autoInstallButton;

        autoInstallBox.getChildren().addAll(autoInstallButton);
        autoInstallBox.setAlignment(Pos.CENTER);
        autoInstallBox.setPrefSize(200, 40);

        mouseEnAndExStyle(autoInstallBox, autoInstallButton);
        autoInstallButton.setOnMouseClicked(e -> {
            selectedIndex = 1;
            setSelected(autoInstallBox, autoInstallButton); // 切换选中态
        });

        HBox modManageBox = new HBox();
        Button modManageButton = new Button("❦   模组管理");

        labelInitial(modManageBox, modManageButton);
        modManageButton.setOnMouseClicked(e -> {
            selectedIndex = 2;
            setSelected(modManageBox, modManageButton); // 切换选中态
        });

        HBox gameInstallBox = new HBox();
        Button gameInstallButton = new Button("☯   游戏安装");
        labelInitial(gameInstallBox, gameInstallButton);
        gameInstallButton.setOnMouseClicked(e -> {
            selectedIndex = 3;
            setSelected(gameInstallBox, gameInstallButton); // 切换选中态
        });
        if (this.mcVersion == null || "".equals(this.mcVersion)) {
            versionNameBox.setStyle(BOX_UNSELECTED_STYLE);
            versionNameLabel.setStyle(BUTTON_UNSELECTED_STYLE);
            gameInstallBox.setStyle(BOX_SELECTED_STYLE);
            gameInstallButton.setStyle(BUTTON_SELECTED_STYLE);
        } else {
            versionNameBox.setStyle(BOX_SELECTED_STYLE);
            versionNameLabel.setStyle(BUTTON_SELECTED_STYLE);
            gameInstallBox.setStyle(BOX_UNSELECTED_STYLE);
            gameInstallButton.setStyle(BUTTON_UNSELECTED_STYLE);
        }

        this.getChildren().addAll(versionNameBox, autoInstallBox, modManageBox, gameInstallBox);

        if (mcVersion != null && !"".equals(mcVersion)) {
            log.info("open version manage global pane for version: " + mcVersion);
            VersionManageGlobalPane.mcVersion = mcVersion;
            VersionManageGlobalPane.modLoaderType = modLoaderType;
            VersionManageGlobalPane.modLoaderVersion = modLoaderVersion;
            root.setCenter(new VersionManageGlobalPane());
        } else {
            log.info("open game install pane");
            root.setCenter(new VersionManageGameInstallPane());
        }
    }

    private void mouseEnAndExStyle(HBox versionNameBox, Button versionNameLabel) {
        versionNameBox.setOnMouseEntered(e -> {
            versionNameBox.setStyle(BOX_SELECTED_STYLE);
            versionNameLabel.setStyle(BUTTON_SELECTED_STYLE);
        });
        versionNameBox.setOnMouseExited(e -> {
            if (selectedBox == versionNameBox) { // 仍选中则保持样式
                versionNameBox.setStyle(BOX_SELECTED_STYLE);
                versionNameLabel.setStyle(BUTTON_SELECTED_STYLE);
            } else { // 已切换到其他按钮，恢复未选中
                versionNameBox.setStyle(BOX_UNSELECTED_STYLE);
                versionNameLabel.setStyle(BUTTON_UNSELECTED_STYLE);
            }
        });
    }

    private void labelInitial(HBox gameInstallBox, Button gameInstallButton) {
        gameInstallBox.setStyle(BOX_UNSELECTED_STYLE);
        gameInstallButton.setStyle(BUTTON_UNSELECTED_STYLE);

        gameInstallBox.getChildren().add(gameInstallButton);
        gameInstallBox.setAlignment(Pos.CENTER);
        gameInstallBox.setPrefSize(200, 40);

        mouseEnAndExStyle(gameInstallBox, gameInstallButton);
    }

    private void setSelected(HBox targetBox, Button targetButton) {
        if (selectedBox != null && selectedButton != null) {
            selectedBox.setStyle(BOX_UNSELECTED_STYLE);
            selectedButton.setStyle(BUTTON_UNSELECTED_STYLE);
        }
        targetBox.setStyle(BOX_SELECTED_STYLE);
        targetButton.setStyle(BUTTON_SELECTED_STYLE);
        selectedBox = targetBox;
        selectedButton = targetButton;

        switch (selectedIndex) {
            case 0: {
                VersionManageGlobalPane.mcVersion = mcVersion;
                VersionManageGlobalPane.modLoaderType = modLoaderType;
                VersionManageGlobalPane.modLoaderVersion = modLoaderVersion;
                root.setCenter(new VersionManageGlobalPane());
                break;
            }
            case 1: {
                if (mcVersion != null && !"".equals(mcVersion)) {
                    root.setCenter(new VersionManageAutoInstallPane(mcVersion, modLoaderType, modLoaderVersion));
                    log.info("open modloader auto install pane for version: " + mcVersion);
                } else {
                    log.info("current game version is null, can not open modloader auto install pane");
                }
                break;
            }
            case 3: {
                root.setCenter(new VersionManageGameInstallPane());
                log.info("open game install pane");
            }
            default: {}
        }
    }
}