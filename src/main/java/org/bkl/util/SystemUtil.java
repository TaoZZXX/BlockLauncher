package org.bkl.util;

public class SystemUtil {

    public static String osName = null;
    

    public static String getOsName() {
        return osName = System.getProperty("os.name").toLowerCase();
    }

}
