import org.bkl.game.GameLauncher;
import org.bkl.game.MCVersionChecker;
import org.bkl.game.MinecraftPath;
import org.bkl.log.Logger;
import org.bkl.log.LoggerFactory;
import org.bkl.util.SystemUtil;
import org.bkl.ui.FirstPage;

/**
 * @author LongWei
 * @date 2025/9/14 21:36
 */
public class BlockLauncher {
    private final static Logger log = LoggerFactory.getLogger("BlockLauncher");

    public static void main(String[] args) {
        log.info("\n" + """
            BBBBBBBBB     K     K    L
            B       B     K    K     L
            B       B     K   K      L
            B       B     K  K       L
            BBBBBBBBB     KKK        L
            B       B     K  K       L
            B       B     K   K      L
            B       B     K    K     L
            BBBBBBBBB     K     K    LLLLLLLLL
            """);
        log.info("BlockLauncher starting");

         if ((new MCVersionChecker()) != null) {
             log.info("game version checking complete");
         }
        if ((new GameLauncher("", "ID", MinecraftPath.getMinecraftPath())) != null) {
            log.info("game start util initial complete");
        }
        FirstPage.launchUi(args);
    }
}
