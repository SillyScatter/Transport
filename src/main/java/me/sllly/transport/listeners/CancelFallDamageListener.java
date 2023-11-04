package me.sllly.transport.listeners;

import me.sllly.transport.objects.Area;
import me.sllly.transport.objects.Lift;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class CancelFallDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
            for (Lift value : Lift.lifts.values()) {
                Area liftArea = value.getNoFallDamageArea();
                if (liftArea.withinArea(event.getEntity().getLocation())){
                    event.setCancelled(true);
                }
            }
        }
    }
}
