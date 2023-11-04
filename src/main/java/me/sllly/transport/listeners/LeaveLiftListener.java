package me.sllly.transport.listeners;

import me.sllly.transport.objects.Lift;
import me.sllly.transport.utils.Util;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class LeaveLiftListener implements Listener {

    @EventHandler
    public void onVehicleExit(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        for (Lift lift : Lift.movingLifts.values()) {
            if (lift.getPlayerChairMap().containsKey(player)){
                BlockDisplay blockDisplay = lift.getPlayerChairMap().get(player);
                lift.getPlayerChairMap().remove(player);
                blockDisplay.remove();

            }
        }
    }




}
