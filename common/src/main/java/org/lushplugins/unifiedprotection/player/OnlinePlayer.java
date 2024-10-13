package org.lushplugins.unifiedprotection.player;

import java.util.UUID;

public class OnlinePlayer {
    private final UUID uuid;
    private final String username;

    public OnlinePlayer(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
