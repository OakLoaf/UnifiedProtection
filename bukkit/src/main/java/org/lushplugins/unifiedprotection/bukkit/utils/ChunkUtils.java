package org.lushplugins.unifiedprotection.bukkit.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChunkUtils {

    /**
     * @param loc1 first location
     * @param loc2 second location
     * @return a list of chunks in the defined area
     */
    public static List<Chunk> getChunksInArea(Location loc1, Location loc2) {
        org.bukkit.World world = loc1.getWorld();
        if (world == null) {
            return Collections.emptyList();
        }

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX()) / 16;
        int maxX = Math.min(loc1.getBlockZ(), loc2.getBlockZ()) / 16;
        int minZ = Math.max(loc1.getBlockX(), loc2.getBlockX()) / 16;
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ()) / 16;

        List<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }

        return chunks;
    }

    /**
     * @param location center location
     * @param range range in blocks
     * @return a list of chunks in range
     */
    public static List<Chunk> getChunksInRange(Location location, int range) {
        org.bukkit.World world = location.getWorld();
        if (world == null) {
            return Collections.emptyList();
        }

        int minX = (int) Math.floor((location.getBlockX() - range) / 16D);
        int maxX = (int) Math.floor((location.getBlockX() + range) / 16D);
        int minZ = (int) Math.floor((location.getBlockZ() - range) / 16D);
        int maxZ = (int) Math.floor((location.getBlockZ() + range) / 16D);

        List<Chunk> chunks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }

        return chunks;
    }
}
