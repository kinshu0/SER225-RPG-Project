package Level;

import java.util.ArrayList;

import Level.Player;

public class CurrentWeapon {
    // static for adding, listing, get item, clearing
    static String currentWeapon;

    public static void equipWeapon(String item) 
    {
        currentWeapon = item;
    }

    public static String getWeapon() {
        return currentWeapon;
    }

   

    public static void clearWeapon() {
        currentWeapon = "";
    }

    

}
