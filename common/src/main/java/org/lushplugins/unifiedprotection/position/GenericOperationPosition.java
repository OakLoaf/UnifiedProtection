package org.lushplugins.unifiedprotection.position;

import net.william278.cloplib.operation.OperationChunk;
import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationWorld;
import org.jetbrains.annotations.NotNull;

public class GenericOperationPosition implements OperationPosition {
    private final double x;
    private final double y;
    private final double z;
    private final OperationWorld world;
    private final OperationChunk chunk;

    public GenericOperationPosition(double x, double y, double z, @NotNull OperationWorld world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.chunk = new GenericOperationChunk(
            (int) Math.floor(x / 16),
            (int) Math.floor(z / 16)
        );
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public @NotNull OperationWorld getWorld() {
        return world;
    }

    @Override
    public @NotNull OperationChunk getChunk() {
        return chunk;
    }

    public record GenericOperationChunk(int x, int z) implements OperationChunk {

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getZ() {
            return z;
        }
    }
}
