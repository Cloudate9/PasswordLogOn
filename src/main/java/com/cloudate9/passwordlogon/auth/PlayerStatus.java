package com.cloudate9.passwordlogon.auth;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatus {

    private static final Map<String, Location> nonAuthPlayers = new HashMap<>();

    public static Map<String, Location> getNonAuthPlayers() {
        return nonAuthPlayers;
    }

    public static void addNonAuthPlayer(String username, Location location) {
        nonAuthPlayers.put(username, location);
    }

    public static void removeNonAuthPlayer(String username) {
        nonAuthPlayers.remove(username);
    }


}
