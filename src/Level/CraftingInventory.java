package Level;

import java.util.ArrayList;

import Level.Player;

public class CraftingInventory {
    // static for adding, listing, get item, clearing
    static ArrayList<String> craftingInventory = new ArrayList<String>();

    public static void addItem(String item) {
        if (craftingInventory.size() != 2) {
            craftingInventory.add(item);
        }
    }

    public static String getItem(int spot) {
        String item = craftingInventory.get(spot);
        return item;
    }

    public static int getSize() {
        int size = craftingInventory.size();
        return size;
    }

    public static void clearInventory() {
        craftingInventory = new ArrayList<String>();
    }

    public static boolean contains(String item) {
        for (int i = 0; i < craftingInventory.size(); i++) {
            if (getItem(i) == item) {
                return true;
            }
        }

        return false;
    }

    public static String craft() {
        String item1;
        String item2;
        if (craftingInventory.size() == 2) {
            item1 = craftingInventory.get(0);
            item2 = craftingInventory.get(1);

            if ((item1 == "Axe" && item2 == "steel") || (item2 == "Axe" && item1 == "steel")) {
                return "Axe+1";
            } else if ((item1 == "Katana" && item2 == "steel") || (item2 == "Katana" && item1 == "steel")) {
                return "Katana+1";
            } else if ((item1 == "Spear" && item2 == "steel") || (item2 == "Spear" && item1 == "steel")) {
                return "Spear+1";
            } else if ((item1 == "Machete" && item2 == "steel") || (item2 == "Machete" && item1 == "steel")) {
                return "Machete+1";
            }
        }

        return "N/A";
    }

}
