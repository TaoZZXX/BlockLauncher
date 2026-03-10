package org.bkl.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;

import java.io.InputStream;

public final class FontUtil {
    private static String regularFamily = "System";
    private static String boldFamily = "System";

    public static void loadFonts(Class<?> clazz) {
        try (InputStream isBold = clazz.getResourceAsStream("/fonts/NotoSansSC-Regular.ttf");
             InputStream isReg = clazz.getResourceAsStream("/fonts/NotoSansSC-Bold.ttf")) {

            if (isReg != null) {
                Font reg = Font.loadFont(isReg, 12);
                if (reg != null) regularFamily = reg.getFamily();
            }
            if (isBold != null) {
                Font b = Font.loadFont(isBold, 12);
                if (b != null) boldFamily = b.getFamily();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applySceneDefault(Scene scene, double sizePx) {
        String family = regularFamily != null ? regularFamily : "System";
        scene.getRoot().setStyle("-fx-background-color: transparent; -fx-font-family: '" + family + "'; -fx-font-size: " + (int) sizePx + "px;");

    }

    // 可选：单控件强制使用粗体家族
    public static void applyBoldToNode(Node node, double sizePx) {
        String family = boldFamily != null ? boldFamily : regularFamily;
        node.setStyle("-fx-font-family: '" + family + "'; -fx-font-size: " + (int) sizePx + "px;");
    }

    // 可选：对 Labeled（Label、Button 等）直接设置字体对象
    public static void applyToLabeled(Labeled labeled, double sizePx, boolean bold) {
        String family = bold ? boldFamily : regularFamily;
        labeled.setFont(Font.font(family, sizePx));
    }
}
