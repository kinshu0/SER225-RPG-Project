package Level;

import java.util.ArrayList;

import Level.Player;

public class Deaths {
    static int ghostDeaths = 0;
    static int zombieDeaths = 0;
    static int priorDeath = 0;
    static double ghostX = 0;
    static double ghostY = 0;

    public static void addGhostDeath() {
        ghostDeaths = ghostDeaths + 1;

    }

    public static boolean didGhostDie() {
        if (ghostDeaths != priorDeath) {
            priorDeath = ghostDeaths;
            return true;
        } else {
            return false;
        }
    }

    public static void lastGhostX(double x) {
        ghostX = x;
    }

    public static void lastGhostY(double y) {
        ghostY = y;
    }

    public static double getXGhost() {
        return ghostX;
    }

    public static double getYGhost() {
        return ghostY;
    }

    public static void addZombieDeath() {
        ghostDeaths = ghostDeaths + 1;
    }
}