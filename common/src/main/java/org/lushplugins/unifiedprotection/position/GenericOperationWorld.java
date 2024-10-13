package org.lushplugins.unifiedprotection.position;

import net.william278.cloplib.operation.OperationWorld;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GenericOperationWorld implements OperationWorld {
    private final String name;
    private final UUID uuid;

    public GenericOperationWorld(@NotNull String name, @NotNull UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull UUID getUuid() {
        return uuid;
    }
}
