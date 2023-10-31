package me.sllly.transport.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Area {

    private Location cornerOne;
    private Location cornerTwo;

    public Area(Location cornerOne, Location cornerTwo) {
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
    }

    public Area(String world, double x1, double y1, double z1, double x2, double y2, double z2) {
        this.cornerOne = new Location(Bukkit.getWorld(world), x1, y1, z1);
        this.cornerTwo = new Location(Bukkit.getWorld(world), x2, y2, z2);
    }

    public Location getCornerOne() {
        return cornerOne;
    }

    public void setCornerOne(Location cornerOne) {
        this.cornerOne = cornerOne;
    }

    public Location getCornerTwo() {
        return cornerTwo;
    }

    public void setCornerTwo(Location cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    //ChatGPT Method
    public boolean withinArea(Location location) {
        double x1 = Math.min(cornerOne.getX(), cornerTwo.getX());
        double x2 = Math.max(cornerOne.getX(), cornerTwo.getX());
        double y1 = Math.min(cornerOne.getY(), cornerTwo.getY());
        double y2 = Math.max(cornerOne.getY(), cornerTwo.getY());
        double z1 = Math.min(cornerOne.getZ(), cornerTwo.getZ());
        double z2 = Math.max(cornerOne.getZ(), cornerTwo.getZ());

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    public static Location findMiddleLocation(Location location1, Location location2) {
        if (location1.getWorld() == null || location2.getWorld() == null) {
            // Check if both locations have valid worlds
            return null;
        }

        double x = (location1.getX() + location2.getX()) / 2.0;
        double y = (location1.getY() + location2.getY()) / 2.0;
        double z = (location1.getZ() + location2.getZ()) / 2.0;

        Location middleLocation = new Location(location1.getWorld(), x, y, z);

        return middleLocation;
    }

    public List<Block> getBlocksWithinArea(){
        World world = cornerOne.getWorld();
        List<Block> blocks = new ArrayList<>();

        double x1 = Math.min(cornerOne.getX(), cornerTwo.getX());
        double x2 = Math.max(cornerOne.getX(), cornerTwo.getX());
        double y1 = Math.min(cornerOne.getY(), cornerTwo.getY());
        double y2 = Math.max(cornerOne.getY(), cornerTwo.getY());
        double z1 = Math.min(cornerOne.getZ(), cornerTwo.getZ());
        double z2 = Math.max(cornerOne.getZ(), cornerTwo.getZ());

        for (double x = x1; x <= x2; x++) {
            for (double y = y1; y <= y2; y++) {
                for (double z = z1; z <= z2; z++) {
                    Location location = new Location(world, x, y, z);
                    blocks.add(world.getBlockAt(location));
                }
            }
        }
        return blocks;
    }
}