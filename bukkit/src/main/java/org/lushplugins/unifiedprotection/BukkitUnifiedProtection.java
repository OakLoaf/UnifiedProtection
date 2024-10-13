package org.lushplugins.unifiedprotection;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.lushplugins.unifiedprotection.hook.*;
import org.lushplugins.unifiedprotection.position.BukkitOperationPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public class BukkitUnifiedProtection {
    private static final List<AbstractHook> hooks = new ArrayList<>();
    private static boolean initialized = false;

    public static void ensureInit() {
        if (initialized) {
            return;
        }
        initialized = true;

        addHook("HuskClaims", HuskClaimsHook::new);
        addHook("HuskTowns", HuskTownsHook::new);
        addHook("GriefPrevention", GriefPreventionHook::new);
        addHook("WorldGuard", WorldGuardHook::new);
    }

    /**
     * @param operationType The operation type to check
     * @param location The location to check
     * @return Whether the operation is allowed at the given position, if a mapping is not applied it will be
     *         assumed to be allowed.
     */
    public static boolean isOperationAllowed(OperationType operationType, Location location) {
        ensureInit();

        OperationPosition position = new BukkitOperationPosition(location);
        for (AbstractHook hook : hooks) {
            if (!hook.getMappings().isOperationAllowed(operationType, position)) {
                return false;
            }
        }

        return true;
    }

    public static void addHook(AbstractHook hook) {
        hooks.add(hook);
    }

    private static void addHook(String pluginName, Callable<AbstractHook> hook) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null && plugin.isEnabled()) {
            try {
                addHook(hook.call());
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, "[UnifiedProtection] Error whilst loading hook: ", e);
            }
        }
    }
}
