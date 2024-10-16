package org.lushplugins.unifiedprotection;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.lushplugins.unifiedprotection.player.OnlinePlayer;

public class BukkitConverter {

    public static World convert(OperationWorld world) {
        return Bukkit.getWorld(world.getUuid());
    }

    public static Location convert(OperationPosition position) {
        return new Location(convert(position.getWorld()), position.getX(), position.getY(), position.getZ());
    }

    public static Player convert(OnlinePlayer player) {
        return Bukkit.getPlayer(player.getUniqueId());
    }
}
