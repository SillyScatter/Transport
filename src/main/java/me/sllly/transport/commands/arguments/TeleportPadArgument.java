package me.sllly.transport.commands.arguments;

import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import me.sllly.transport.objects.TeleportPad;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportPadArgument extends SYSArgument {
    @Override
    public boolean isValid(String input) {
        return TeleportPad.teleportPads.containsKey(input);
    }

    @Override
    public List<String> getInvalidInputMessage(String input) {
        return Arrays.asList("&cThat is not a valid teleport pad ID");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> complete = new ArrayList<>();
        for (String name : TeleportPad.teleportPads.keySet()) {
            if (name.startsWith(input)){
                complete.add(name);
            }
        }
        return complete;
    }
}
