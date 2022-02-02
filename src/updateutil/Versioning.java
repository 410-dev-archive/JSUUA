package updateutil;

public class Versioning {

    public static final int DEFAULT_BUILDNUM = -Integer.MAX_VALUE;
    public static final String DEFAULT_UPDATE_URL = "____";

    private static int buildNum = DEFAULT_BUILDNUM;
    private static String updateCheckerURL = DEFAULT_UPDATE_URL;
    private static boolean useStableChannel = true;

    public static void setInfo(int buildNumber, String URL) {
        setInfo(buildNumber, URL, true);
    }

    public static void setInfo(int buildNumber, String URL, boolean useStableChannel) {
        buildNum = buildNumber;
        updateCheckerURL = URL;
        Versioning.useStableChannel = useStableChannel;
    }

    public static void setBuildNum(int num) {
        buildNum = num;
    }

    public static void setUpdateCheckerURL(String url) {
        updateCheckerURL = url;
    }

    public static void useStableRelease() {
        useStableChannel = true;
    }

    public static void useLatestRelease() {
        useStableChannel = false;
    }

    public static int getBuildNum() {
        return buildNum;
    }

    public static String getUpdateCheckerURL() {
        return updateCheckerURL;
    }

    public static boolean isUsingStableRelease() {
        return useStableChannel;
    }
}
