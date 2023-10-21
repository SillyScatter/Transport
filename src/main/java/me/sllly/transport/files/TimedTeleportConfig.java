package me.sllly.transport.files;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;
import dev.splityosis.configsystem.configsystem.actionsystem.Actions;
import me.sllly.transport.objects.Area;
import me.sllly.transport.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TimedTeleportConfig extends AnnotatedConfig {
    public TimedTeleportConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "teleport-area")
    public static Area teleportArea = new Area("world", 5,5,5,10,10,10);

    @ConfigField(path = "teleport-end-location", comment = "where the players go to once they're teleported")
    public static Location targetLocation = new Location(Bukkit.getWorld("world"), 0, 200, 0);

    @ConfigField(path = "time-between-activations")
    public static int activationTime = 300;

    @ConfigField(path = "you-have-been-teleported", comment = "this actions gets called on every player that gets teleported")
    public static Actions beenTeleported = Util.getDefaultTitleActions(Sound.ENTITY_ENDERMAN_TELEPORT, "&5&lTeleported!", "&6Continue your quest here!", "&aYou have been teleported!");

    @ConfigField(path = "commands", comment = "any extra commands that you want to run when the timer reaches 0")
    public static List<String> commands = Arrays.asList();
}
