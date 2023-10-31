package me.sllly.transport.commands;

import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import me.sllly.transport.Transport;
import me.sllly.transport.commands.arguments.TeleportPadArgument;
import me.sllly.transport.objects.TeleportPad;
import me.sllly.transport.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class TransportCommandSystem extends SYSCommandBranch {
    public TransportCommandSystem(String... names) {
        super(names);
        setPermission("transport.admin");

        addCommand(new SYSCommand("reload")
                .setArguments()
                .executes((sender, args) -> {
                    Transport.plugin.reloadPlugin();
                    Util.sendMessage(sender, "&aReloaded Transport.");
                }));

        SYSCommandBranch teleportBranch = new SYSCommandBranch("teleportpad");
        addBranch(teleportBranch);

        teleportBranch.addCommand(new SYSCommand("pause")
                .setArguments(new TeleportPadArgument())
                .executes((sender, args) -> {
                    TeleportPad teleportPad = TeleportPad.teleportPads.get(args[0]);
                    if (teleportPad == null) {
                        Util.sendMessage(sender, Arrays.asList("&cThere's a bug in the code!  Contact Charlllie/Sllly", "&cFailed to find TeleportPad"));
                        return;
                    }
                    if (!teleportPad.isOn()){
                        Util.sendMessage(sender, "&cThe teleport pad is already off!");
                        return;
                    }
                    teleportPad.setOn(false);
                    Util.sendMessage(sender, "&aSuccessfully stopped pad "+args[0]);
                }));

        teleportBranch.addCommand(new SYSCommand("unpause")
                .setArguments(new TeleportPadArgument())
                .executes((sender, args) -> {
                    TeleportPad teleportPad = TeleportPad.teleportPads.get(args[0]);
                    if (teleportPad == null) {
                        Util.sendMessage(sender, Arrays.asList("&cThere's a bug in the code!  Contact Charlllie/Sllly", "&cFailed to find TeleportPad"));
                        return;
                    }
                    if (teleportPad.isOn()){
                        Util.sendMessage(sender, "&cThe teleport pad is already on!");
                        return;
                    }
                    teleportPad.startFromSave();
                    Util.sendMessage(sender, "&aSuccessfully unpaused pad "+args[0]);
                }));

        teleportBranch.addCommand(new SYSCommand("start")
                .setArguments(new TeleportPadArgument())
                .executes((sender, args) -> {
                    TeleportPad teleportPad = TeleportPad.teleportPads.get(args[0]);
                    if (teleportPad == null) {
                        Util.sendMessage(sender, Arrays.asList("&cThere's a bug in the code!  Contact Charlllie/Sllly", "&cFailed to find TeleportPad"));
                        return;
                    }
                    if (teleportPad.isOn()){
                        Util.sendMessage(sender, "&cThe teleport pad is already on!");
                        return;
                    }
                    teleportPad.startFromMax();
                    Util.sendMessage(sender, "&aSuccessfully started pad "+args[0]);
                }));

        addCommand(new SYSCommand("test")
                .executesPlayer((sender, args) -> {
                    World world = sender.getWorld();
                    BlockData blockData = Material.PRISMARINE_BRICK_SLAB.createBlockData();
                    BlockDisplay blockDisplay = Util.createBlockDisplay(blockData, sender.getLocation());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            blockDisplay.removePassenger(sender);
                            blockDisplay.teleport(blockDisplay.getLocation().add(0,0.03,0));
                            blockDisplay.addPassenger(sender);
                        }
                    }.runTaskTimer(Transport.plugin, 0, 1);
                }));

        addCommand(new SYSCommand("vanquish")
                .executesPlayer((sender, args) -> {
                    World world = sender.getWorld();
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof BlockDisplay){
                            entity.remove();
                        }
                    }
                }));
    }
}
