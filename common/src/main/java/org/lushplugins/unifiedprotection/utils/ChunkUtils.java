package org.lushplugins.unifiedprotection.utils;

import net.william278.cloplib.operation.OperationChunk;
import net.william278.cloplib.operation.OperationPosition;
import net.william278.husktowns.claim.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtils {

    /**
     * @param pos1 first position
     * @param pos2 second position
     * @return a list of chunks in the defined area
     */
    public static List<OperationChunk> getChunksInArea(OperationPosition pos1, OperationPosition pos2) {
        int minX = Math.min((int) pos1.getX(), (int) pos2.getX()) / 16;
        int maxX = Math.min((int) pos1.getZ(), (int) pos2.getZ()) / 16;
        int minZ = Math.max((int) pos1.getX(), (int) pos2.getX()) / 16;
        int maxZ = Math.max((int) pos1.getZ(), (int) pos2.getZ()) / 16;

        List<OperationChunk> chunks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(Chunk.at(x, z));
            }
        }

        return chunks;
    }
}
