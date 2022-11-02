package Level;

public class base {

    // base health
    static int baseHealth = 200;

    public static int getBaseHealth() {
        return baseHealth;
    }

    public static void baseDam() {
        baseHealth = baseHealth - 1;
    }

    public static String getBaseHealthS(){
        return ("Base Health: " + String.valueOf(baseHealth));
    }

    // interaction with base
}
