package modloader;

import org.bkl.modloader.OptiFineChecker;
import org.junit.jupiter.api.Test;

public class RiftTest {

    @Test
    public void isRiftInstall() {
        System.out.println(OptiFineChecker.isOptiFineInstalled("1.21.7"));

    }
}
