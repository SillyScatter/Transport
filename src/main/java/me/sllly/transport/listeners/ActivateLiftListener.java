package me.sllly.transport.listeners;

import me.sllly.transport.objects.Lift;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActivateLiftListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block.getType().toString().contains("BUTTON")){
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
                for (Lift value : Lift.lifts.values()) {
                    if (value.getButtonLocations().contains(block.getLocation())){
                        if (Lift.movingLifts.values().contains(value)){
                            return;
                        }
                        value.startLift();
                    }
                }
            }
        }

    }
}
