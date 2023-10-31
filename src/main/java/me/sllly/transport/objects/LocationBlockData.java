package me.sllly.transport.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;

public class LocationBlockData {

    private Location location;

    private BlockData blockData;

    public LocationBlockData(Location location, BlockData blockData) {
        this.location = location;
        this.blockData = blockData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
