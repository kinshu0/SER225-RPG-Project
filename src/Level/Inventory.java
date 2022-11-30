package Level;

import java.util.ArrayList;

import Level.Player;

public class Inventory {
    // static for adding, listing, get item, clearing
    static ArrayList<String> playerInventory = new ArrayList<String>();
    static int axeDamage = 0;
    static int katanaDamage = 0;
    static int macheteDamage = 0;
    static int spearDamage = 0;

    public static void upgradeAxe() {
        axeDamage = axeDamage + 5;
    }

    public static void upgradeMachete() {
        macheteDamage = macheteDamage + 5;
    }

    public static void upgradeKatana() {
        katanaDamage = katanaDamage + 5;
    }

    public static void upgradeSpear() {
        spearDamage = spearDamage + 5;
    }

    public static int returnSpear() {
        return spearDamage;
    }

    public static int returnAxe() {
        return axeDamage;
    }

    public static int returnMachete() {
        return macheteDamage;
    }

    public static int returnKatana() {
        return katanaDamage;
    }

    public static void addItem(String item) {
        playerInventory.add(item);
    }

    public static void replace(String originalItem, String newItem) {
        int position = getPostion(originalItem);

        // playerInventory[position] = newItem;
        playerInventory.set(position, newItem);
    }

    public static int getPostion(String item) {
        for (int i = 0; i < playerInventory.size(); i++) {
            if (getItem(i) == item) {
                return i;
            }
        }

        return 0;
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

    public static boolean contains(String item) {
        for (int i = 0; i < playerInventory.size(); i++) {
            if (getItem(i) == item) {
                return true;
            }
        }

        return false;
    }

}
