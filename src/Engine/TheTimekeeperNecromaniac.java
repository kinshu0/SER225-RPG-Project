package Engine;

public class TheTimekeeperNecromaniac {
    
    public static int DEFAULT_UPDATE_INCREMENT = 30;

    private static int updateCount = 0;

    public static void increment() {
        updateCount = (updateCount + DEFAULT_UPDATE_INCREMENT) % (60 * 60 * 24);
    }

    public static void increment(int updateAmount) {
        updateCount = (updateCount + updateAmount) % (60 * 60 * 24);
    }

    public static int getUpdateCount() {
        return updateCount;
    }

    public static int getHours() {
        return updateCount / (60 * 60);
    }

    public static int getMinutes() {
        return (updateCount % 3600) / (60);
    }

    public static String getTime() {
        return String.format("Time: %02d:%02d", getHours(), getMinutes());
    }

    public boolean isDark() {
        int h = getHours();
        return h > 18 || h < 6;
    }


}
