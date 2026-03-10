package modloader;

import org.bkl.modloader.ForgeChecker;
import org.junit.jupiter.api.Test;

public class ForgeTest {

    @Test
    public void isForgeInstall() {
        boolean forgeInstalled = ForgeChecker.isForgeInstalled("1.21.7");
        System.out.println(forgeInstalled);
    }

}
