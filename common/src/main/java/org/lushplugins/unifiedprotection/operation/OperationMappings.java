package org.lushplugins.unifiedprotection.operation;

import net.william278.cloplib.operation.OperationPosition;
import net.william278.cloplib.operation.OperationType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

// TODO: Remove mappings in favour of AbstractHook implementing PositionChecker
public class OperationMappings {
    private final EnumMap<OperationType, OldPositionChecker> mappings = new EnumMap<>(OperationType.class);
    private OldPositionChecker defaultChecker = (ignored1, ignored2) -> true;

    /**
     * @param operationType The operation type to check
     * @param position The position to check
     * @return Whether the operation is allowed at the given position, if a mapping is not applied it will be
     *         assumed to be allowed.
     */
    public boolean isOperationAllowed(OperationType operationType, OperationPosition position) {
       return mappings.getOrDefault(operationType, defaultChecker).check(operationType, position);
    }

    public void addMapping(OperationType operationType, OldPositionChecker positionChecker) {
        mappings.put(operationType, positionChecker);
    }

    public void setDefaultChecker(OldPositionChecker defaultChecker) {
        this.defaultChecker = defaultChecker;
    }

    @FunctionalInterface
    public interface OldPositionChecker {
        boolean check(@NotNull OperationType operationType, @NotNull OperationPosition position);
    }
}
