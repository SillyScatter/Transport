package me.sllly.transport.commands;

import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import me.sllly.transport.Transport;
import me.sllly.transport.objects.TimedTeleportTask;
import me.sllly.transport.utils.Util;

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

        teleportBranch.addCommand(new SYSCommand("off")
                .setArguments()
                .executes((sender, args) -> {
                    if (TimedTeleportTask.on){
                        TimedTeleportTask.on = false;
                        Util.sendMessage(sender, "&aSet the teleportpad timer to &c&ldisabled.");
                        return;
                    }
                    Util.sendMessage(sender, "&cThe timer is already off!");
                }));

        teleportBranch.addCommand(new SYSCommand("start")
                .setArguments()
                .executes((sender, args) -> {
                    if (!TimedTeleportTask.on){
                        TimedTeleportTask.on = true;
                        TimedTeleportTask.startTeleportTaskTimerFromMax();
                        Util.sendMessage(sender, "&aSet the teleportpad timer to &c&ldisabled.");
                        return;
                    }
                    Util.sendMessage(sender, "&cThe timer is already on!");
                }));
    }
}
