package me.sllly.transport.objects.lists;

import me.sllly.transport.objects.LocationBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.BlockDisplay;

import java.util.ArrayList;
import java.util.List;

public class LocationBlockDataList extends ArrayList<LocationBlockData> {


    public static LocationBlockDataList getDefault(){
        LocationBlockDataList locationBlockDataList = new LocationBlockDataList();
        locationBlockDataList.add(new LocationBlockData(new Location(Bukkit.getWorlds().get(0), 5,5,5), Material.OAK_PLANKS.createBlockData()));
        return locationBlockDataList;
    }

    public static LocationBlockDataList getLocationBlockDataList(List<Block> blocks){
        LocationBlockDataList locationBlockDataList = new LocationBlockDataList();
        for (Block block : blocks) {
            if (block == null) {
                continue;
            }
            if (block.getType()==Material.AIR){
                continue;
            }
            locationBlockDataList.add(new LocationBlockData(block.getLocation(), block.getBlockData()));
        }
        return locationBlockDataList;
    }
}
