package download;

import org.bkl.download.GameVersionManifest;
import org.junit.jupiter.api.Test;

public class GameVersionManifestTest {

    @Test
    public void getManifestFetcher() {
        System.out.println(GameVersionManifest.getManifestFetcher());
    }

    @Test
    public void getVersionInfo() {
        System.out.println(GameVersionManifest.getVersionInfo("1.20.4"));
    }


}
