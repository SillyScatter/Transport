package me.sllly.transport.objects;

import dev.splityosis.configsystem.configsystem.actionsystem.Actions;
import me.sllly.transport.Transport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeleportPad {

    public static Map<String, TeleportPad> teleportPads = new HashMap<>();

    private String name;
    private Area area;
    private Location location;
    private int maxTime;
    private Actions actions;
    private List<String> commands;

    private int timeUntilTeleport;
    private boolean on;

    public TeleportPad(String name, Area area, Location location, int maxTime, Actions actions, List<String> commands) {
        this.name = name;
        this.area = area;
        this.location = location;
        this.maxTime = maxTime;
        this.actions = actions;
        this.commands = commands;
    }

    public void startFromMax(){
        timeUntilTeleport = maxTime;
        startFromSave();
    }

    public void startFromSave(){
        on = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!on){
                    this.cancel();
                    return;
                }
                timeUntilTeleport--;
                if (timeUntilTeleport > 0){
                    return;
                }
                timeUntilTeleport = maxTime;
                Location centreLocation = Area.findMiddleLocation(area.getCornerOne(), area.getCornerTwo());
                List<Player> playersInWorld = centreLocation.getWorld().getPlayers();
                for (Player player : playersInWorld) {
                    if (area.withinArea(player.getLocation()) && player.hasPermission("transport."+name)){
                        actions.perform(player, null);
                        player.teleport(location);
                    }
                }
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }.runTaskTimer(Transport.plugin, 0, 20);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getTimeUntilTeleport() {
        return timeUntilTeleport;
    }

    public void setTimeUntilTeleport(int timeUntilTeleport) {
        this.timeUntilTeleport = timeUntilTeleport;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
