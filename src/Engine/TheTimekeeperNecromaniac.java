package Engine;

public class TheTimekeeperNecromaniac {
    
    public static int DEFAULT_UPDATE_INCREMENT = 30;

    private static int updateCount = 0;
    public static int night = 1;

    public static void increment() {
        updateCount = (updateCount + DEFAULT_UPDATE_INCREMENT) % (60 * 60 * 24);
    }
    public static void nightCheck() {
        if(updateCount == 0) {
            night += 1;
        }
    }
    public static String getNight(){
        return "Night: " + Integer.toString(night);
    }

    public static String getNightTitle() {
        if (getHours() == 0 && getMinutes() < 25) {
            return getNight();
        } else {
            return "";
        }
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

    public static boolean isDark() {
        int h = getHours();
        return h > 18 || h < 6;
    }


}
