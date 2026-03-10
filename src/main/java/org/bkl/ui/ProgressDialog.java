package org.bkl.ui;


import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class ProgressDialog extends HBox {

    private final static int ANIMATION_DURATION = 300;
    private static Label progressName = null;
    private static ProgressBar progressBar = null;

    public ProgressDialog() {
        setPrefSize(400, 40);
        setMinSize(400, 40);
        setMaxSize(400, 40);
        setAlignment(Pos.CENTER);
        setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-width: 2;"
        );
        setOpacity(0);

        this.progressName = new Label("加载中...");
        this.progressBar = new ProgressBar();
        initUi();
    }

    private void initUi() {
        VBox vBox = new VBox();
        vBox.setPrefSize(400, 20);
        vBox.setStyle(
                "-fx-background-color: transparent"
        );
        this.getChildren().addAll(vBox);

        this.progressBar.setPrefWidth(400);
        this.progressBar.setPrefHeight(10);
        this.progressBar.setProgress(0);
        vBox.getChildren().add(this.progressName);
        vBox.getChildren().add(this.progressBar);
    }

    public static void updateProgress(Double v) {
        progressBar.setProgress(v);
    }

    public void show(StackPane parent) {
        parent.getChildren().remove(this);
        StackPane.setAlignment(this, Pos.CENTER);
        parent.getChildren().add(this);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(ANIMATION_DURATION), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void hide() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(ANIMATION_DURATION), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            if (this.getParent() instanceof Pane) {
                ((Pane) this.getParent()).getChildren().remove(this);
            }
        });

        fadeOut.play();
    }

}
