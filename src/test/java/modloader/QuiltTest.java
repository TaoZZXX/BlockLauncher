package modloader;

import org.bkl.modloader.QuiltChecker;
import org.junit.jupiter.api.Test;

public class QuiltTest {

    @Test
    public void isQuiltInstall() {
        boolean quiltInstalled = QuiltChecker.isQuiltInstalled("1.21.7");
        System.out.println(quiltInstalled);
    }
}
