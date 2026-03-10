package download;

import org.bkl.download.Download4VersionManifest;
import org.bkl.game.MinecraftPath;
import org.bkl.util.HashUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class Download4VersionManifestTest {
    @Test
    public void downloadVersionManifest() {
        Download4VersionManifest.downloadVersionManifest("1.21.10");
    }

    @Test
    public void downloadClientJar() {
        Download4VersionManifest.downloadClientJar("1.21.10");
    }

    @Test
    public void hashUt() {
        System.out.println(HashUtil.verifyHash(Path.of(MinecraftPath.getMinecraftPath() + "/versions/1.21.10/1.21.10.jar"), "d3bdf582a7fa723ce199f3665588dcfe6bf9aca8", "SHA-1"));
    }

    @Test
    public void downloadLibraries() {
        Download4VersionManifest.downloadLibraries("1.21.10");
    }

    @Test
    public void downloadAssets() {
        Download4VersionManifest.downloadAssets("1.21.10");
    }

}
