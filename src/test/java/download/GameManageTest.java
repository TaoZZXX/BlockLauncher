package download;

import org.bkl.download.GameManage;
import org.junit.jupiter.api.Test;

public class GameManageTest {

    @Test
    public void downloaderGame() {
        GameManage.downloaderGame("1.21.10");
    }

    @Test
    public void deleteGame() {
        GameManage.deleteGame("1.21.10");
    }
}
