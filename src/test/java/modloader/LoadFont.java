package modloader;

import javafx.scene.text.Font;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class LoadFont {

    @Test
    public void loadCustomFont() {
        // 直接通过类路径资源加载（无需判断File是否存在）
        try (InputStream fontStream = LoadFont.class.getResourceAsStream("/fonts/NotoSansCJKsc-Bold.otf")) {
            // 检查资源流是否为null（路径错误时会返回null）
            if (fontStream == null) {
                System.err.println("字体资源不存在！请检查路径是否正确：/fonts/NotoSansCJKsc-Bold.otf");
                return;
            }
            // 加载字体（第二个参数是默认大小，不影响后续使用）
            Font.loadFont(fontStream, 10);
            System.out.println("字体加载成功！");
        } catch (IOException e) {
            System.err.println("字体加载失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
