package me.sllly.transport.objects;

import me.sllly.transport.Transport;
import me.sllly.transport.files.TimedTeleportConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TimedTeleportTask {

    public static int timeUntilTeleport;
    public static boolean on;

    public static void startTeleportTaskTimerFromMax(){
        timeUntilTeleport = TimedTeleportConfig.activationTime;
        startTeleportTaskFromPrevious();
    }

    public static void startTeleportTaskFromPrevious(){
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
                timeUntilTeleport = TimedTeleportConfig.activationTime;
                Area area = TimedTeleportConfig.teleportArea;
                Location centreLocation = Area.findMiddleLocation(area.getCornerOne(), area.getCornerTwo());
                List<Player> playersInWorld = centreLocation.getWorld().getPlayers();
                for (Player player : playersInWorld) {
                    if (area.withinArea(player.getLocation())){
                        TimedTeleportConfig.beenTeleported.perform(player, null);
                        player.teleport(TimedTeleportConfig.targetLocation);
                    }
                }
            }
        }.runTaskTimer(Transport.plugin, 0, 20);
    }
}
