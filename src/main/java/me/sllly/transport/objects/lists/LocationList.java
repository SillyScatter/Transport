package me.sllly.transport.objects.lists;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class LocationList extends ArrayList<Location> {

    public static LocationList getDefault(){
        LocationList locations = new LocationList();
        locations.add(new Location(Bukkit.getWorlds().get(0),19,100,19));
        locations.add(new Location(Bukkit.getWorlds().get(0), 19,101,19, 0, 0));
        return locations;
    }
}
