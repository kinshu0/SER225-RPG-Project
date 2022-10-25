package Level;

import java.util.ArrayList;

import Level.Player;

public class Inventory {
    // static for adding, listing, get item, clearing
    static ArrayList<String> playerInventory = new ArrayList<String>();

    public static void addItem(String item) {
        playerInventory.add(item);
    }

    public static String getItem(int spot) {
        String item = playerInventory.get(spot);
        return item;
    }

    public static int getSize() {
        int size = playerInventory.size();
        return size;
    }

    public static void clearInventory() {
        playerInventory = new ArrayList<String>();
    }

}
