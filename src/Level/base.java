package Level;

public class base {

    // base health
    static int baseHealth = 200;

    public static int getBaseHealth() {
        return baseHealth;
    }

    public static void setBaseHealth(int baseHealth) {
        baseHealth = baseHealth;
    }

    public static String getBaseHealthS(){
        return ("Base Health: " + String.valueOf(baseHealth));
    }

    // interaction with base
}
