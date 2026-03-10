package org.bkl.ui.versionmanage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.bkl.game.MinecraftPath;
import org.bkl.modloader.ModLoaderManager;
import org.bkl.modloader.ModLoaderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.bkl.modloader.ModLoaderManager.loadRemoteVersions;

public class VersionManageAutoInstallPane extends VBox {

    private ListView<String> versionListView;
    private final List<Button> topButtons = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ProgressIndicator loadingIndicator;
    private String mcVersion;
    private ModLoaderType modLoaderType;
    private String modLoaderVersion;
    private ModLoaderType currentModLoaderType =  ModLoaderType.FABRIC;

    public VersionManageAutoInstallPane(String mcVersion, ModLoaderType modLoaderType, String modLoaderVersion) {
        this.mcVersion = mcVersion;
        this.modLoaderType = modLoaderType;
        this.modLoaderVersion = modLoaderVersion;

        this.setPrefSize(600, 470);
        this.setMinSize(600, 470);
        this.setMaxSize(600, 470);
        this.setStyle("""
                    -fx-background-color: rgba(245, 245, 245, 0.8);
                """);
        initUi();
    }

    private void initUi() {
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

        Button btn1 = createSelectableButton("Fabric", ModLoaderType.FABRIC);
        Button btn2 = createSelectableButton("Forge", ModLoaderType.FORGE);
        Button btn3 = createSelectableButton("Quilt", ModLoaderType.QUILT);
        Button btn4 = createSelectableButton("OptiFine", ModLoaderType.OPTIFINE);
        topButtons.addAll(Arrays.asList(btn1, btn2, btn3, btn4));
        top.getChildren().addAll(topButtons);

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

        Button titleBtn = new Button("版本列表");
        titleBtn.setPrefSize(560, 30);
        titleBtn.setStyle("""
                    -fx-background-color: rgba(230, 230, 230, 0.8);
                    -fx-text-fill: #333333;
                    -fx-font-weight: 700;
                    -fx-border-radius: 3;
                    -fx-cursor: default;
                """);
        titleBtn.setDisable(true);
        VBox.setMargin(titleBtn, new Insets(0, 0, 10, 0));

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        loadingIndicator.setPrefSize(80, 80);
        loadingIndicator.setStyle("""
            -fx-progress-color: #4a90e2; 
            -fx-background-color: transparent; 
        """);
        loadingIndicator.setVisible(false);

        versionListView = new ListView<>();
        versionListView.setPrefSize(560, 330);
        versionListView.setMinSize(560, 330);
        versionListView.setMaxSize(560, 330);
        versionListView.setStyle("""
                    -fx-font-size: 14px;
                    -fx-background-color: white;
                    -fx-border-color: #dddddd;
                    -fx-border-radius: 3;
                    -fx-selection-bar: #f0f0f0;
                    -fx-selection-bar-text: #000000;
                """);

        versionListView.setCellFactory(listView -> new ListCell<>() {
            private final HBox itemBox = new HBox();
            private final Label versionLabel = new Label();
            private final Region spacer = new Region();
            private final Button downloadBtn = new Button();

            {
                if (modLoaderType != null) {
                    downloadBtn.setText("移除");
                } else {
                    downloadBtn.setText("下载");
                }

                versionLabel.setStyle("-fx-padding: 0 0 0 10;");

                HBox.setHgrow(spacer, Priority.ALWAYS);

                downloadBtn.setPrefSize(80, 25);
                downloadBtn.setStyle("""
                            -fx-background-color: #4a90e2;
                            -fx-text-fill: white;
                            -fx-font-weight: 500;
                            -fx-border-radius: 3;
                            -fx-cursor: hand;
                        """);
                downloadBtn.setPadding(new Insets(0, 10, 0, 0));

                itemBox.getChildren().addAll(versionLabel, spacer, downloadBtn);
                itemBox.setPadding(new Insets(5, 0, 5, 0));

                downloadBtn.setOnAction(event -> {
                    if (modLoaderType != null) {
                        Platform.runLater(() -> {
                            loadingIndicator.setVisible(true);
                            versionListView.setVisible(false);
                        });

                        Task<Void> task = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                switch (modLoaderType) {
                                    case FABRIC -> ModLoaderManager.removeModLoader(mcVersion, ModLoaderType.FABRIC);
                                    case FORGE -> ModLoaderManager.removeModLoader(mcVersion, ModLoaderType.FORGE);
                                    case QUILT -> ModLoaderManager.removeModLoader(mcVersion, ModLoaderType.QUILT);
                                    case OPTIFINE -> ModLoaderManager.removeModLoader(mcVersion, ModLoaderType.OPTIFINE);
                                }
                                // TODO
                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                super.succeeded();
                                Platform.runLater(() -> {
                                    modLoaderType = null;
                                    modLoaderVersion = null;

                                    VersionManageLeftPane.modLoaderType = null;
                                    VersionManageLeftPane.modLoaderVersion = null;

                                    loadingIndicator.setVisible(false);
                                    getRemoteVersions(ModLoaderType.FABRIC);
                                });
                            }

                            @Override
                            protected void failed() {
                                versionListView.setVisible(true);
                            }
                        };
                        executor.execute(task);
                    } else {
                        Platform.runLater(() -> {
                            loadingIndicator.setVisible(true);
                            versionListView.setVisible(false);
                        });

                        Task<Void> task = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                ModLoaderManager.installModLoader(MinecraftPath.getMinecraftPath(), mcVersion, versionLabel.getText(), currentModLoaderType);
                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                super.succeeded();
                                Platform.runLater(() -> {
                                    modLoaderType = ModLoaderManager.getModLoaderType(mcVersion);
                                    modLoaderVersion = ModLoaderManager.getModLoaderVersion(mcVersion);

                                    VersionManageLeftPane.modLoaderType = modLoaderType;
                                    VersionManageLeftPane.modLoaderVersion = modLoaderVersion;

                                    ArrayList<String> value = new ArrayList<>();
                                    value.add(modLoaderType.name() + "：" + modLoaderVersion);
                                    updateVersionList(value);

                                    loadingIndicator.setVisible(false);
                                    versionListView.setVisible(true);
                                });
                            }

                            @Override
                            protected void failed() {
                                versionListView.setVisible(true);
                            }
                        };
                        executor.submit(task);
                    }
                });
            }

            @Override
            protected void updateItem(String versionText, boolean empty) {
                super.updateItem(versionText, empty);
                if (empty || versionText == null) {
                    setGraphic(null);
                } else {
                    versionLabel.setText(versionText);
                    if (modLoaderType != null) {
                        downloadBtn.setText("移除");
                    } else {
                        downloadBtn.setText("下载");
                    }
                    setGraphic(itemBox);
                }
            }
        });

        StackPane listAndLoadingPane = new StackPane();
        listAndLoadingPane.setPrefSize(560, 340);
        listAndLoadingPane.getChildren().addAll(versionListView, loadingIndicator);
        content.getChildren().addAll(titleBtn, listAndLoadingPane);

        setSelectedButton(btn1);

        this.getChildren().addAll(top, content);

        if (modLoaderType != null) {
            ArrayList<String> value = new ArrayList<>();
            value.add(modLoaderType.name() + "：" + modLoaderVersion);
            updateVersionList(value);
        } else {
            getRemoteVersions(ModLoaderType.FABRIC);
        }

    }

    private Button createSelectableButton(String text, ModLoaderType modLoaderType1) {
        Button btn = new Button(text);
        btn.setPrefSize(100, 40);
        setButtonUnselected(btn);

        btn.setOnAction(e -> {
            currentModLoaderType = modLoaderType1;
            for (Button b : topButtons) {
                setButtonUnselected(b);
            }
            setButtonSelected(btn);

            if (modLoaderType != null) {
                ArrayList<String> value = new ArrayList<>();
                value.add(modLoaderType.name() + "：" + modLoaderVersion);
                updateVersionList(value);
            } else {
                getRemoteVersions(modLoaderType1);
            }

        });
        return btn;
    }

    private List<String> getRemoteVersions(ModLoaderType modLoaderType) {
        switch (modLoaderType) {
            case FABRIC -> {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(true);
                    versionListView.setVisible(false);
                });

                Task<List<String>> task = new Task<>() {
                    @Override
                    protected List<String> call() throws Exception {
                        return loadRemoteVersions(mcVersion, ModLoaderType.FABRIC);
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        List<String> value = getValue();
                        Platform.runLater(() -> {
                            loadingIndicator.setVisible(false);
                            versionListView.setVisible(true);
                            updateVersionList(value);
                        });
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        Throwable exception = getException();
                        Platform.runLater(() -> {
                            versionListView.setVisible(false);
                        });
                    }

                };
                executor.submit(task);
            }
        }
        return new ArrayList<>();
    }

    private void setButtonSelected(Button btn) {
        btn.setStyle("""
                    -fx-background-color: #4a90e2;
                    -fx-text-fill: white;
                    -fx-font-weight: 700;
                    -fx-border-radius: 5;
                """);
    }

    private void setButtonUnselected(Button btn) {
        btn.setStyle("""
                    -fx-background-color: #ffffff;
                    -fx-text-fill: #000000;
                    -fx-font-weight: 500;
                    -fx-border-radius: 5;
                """);
    }

    private void setSelectedButton(Button btn) {
        for (Button b : topButtons) {
            setButtonUnselected(b);
        }
        setButtonSelected(btn);
    }

    private void updateVersionList(List<String> versions) {
        ObservableList<String> observableVersions = FXCollections.observableArrayList(versions);
        versionListView.setItems(observableVersions);
    }
}