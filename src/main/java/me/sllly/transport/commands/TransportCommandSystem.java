package me.sllly.transport.commands;

import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import me.sllly.transport.Transport;
import me.sllly.transport.commands.arguments.TeleportPadArgument;
import me.sllly.transport.objects.TeleportPad;
import me.sllly.transport.utils.Util;

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
    }
}
