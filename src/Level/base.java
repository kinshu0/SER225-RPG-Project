package Level;

public class base {

    // base health
    static int baseHealth = 50;

    public static int getBaseHealth() {
        return baseHealth;
    }

    public static void setBaseHealth(int baseHealth1) {
        baseHealth = baseHealth1;
    }

    public static void baseDam() {
        baseHealth = baseHealth - 1;
    }

    public static void baseDam3() {
        baseHealth = baseHealth - 3;
    }

    public static String getBaseHealthS() {
        return ("Base Health: " + String.valueOf(baseHealth));
    }

    // interaction with base
}
