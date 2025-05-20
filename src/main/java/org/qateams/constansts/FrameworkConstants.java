package org.qateams.constansts;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    private static final String RESOURCESPATH;
    private static final String CHROMEDRIVER;
    private static final String CONFIGFILEPATH;

    static {
        RESOURCESPATH = System.getProperty("user.dir") + "/src/test/resources/";
    }

    static {
        CONFIGFILEPATH = RESOURCESPATH + "/config/conf.properties";
    }

    static {
        CHROMEDRIVER = RESOURCESPATH + "/executables/chromedriver.exe";
    }

    public static String getConfigFilePath() {
        return CONFIGFILEPATH;
    }

    public static String getChromeDriverPath() {
        return CHROMEDRIVER;
    }

}
