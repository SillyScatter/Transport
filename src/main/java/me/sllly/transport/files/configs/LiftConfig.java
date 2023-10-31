package me.sllly.transport.files.configs;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;
import dev.splityosis.configsystem.configsystem.InvalidConfigFileException;
import me.sllly.transport.objects.lists.LocationList;

import java.io.File;

public class LiftConfig extends AnnotatedConfig {
    public LiftConfig(File file) throws InvalidConfigFileException {
        super(file);
        this.name = file.getName().substring(0,file.getName().length()-4);
    }
    public String name;

    @ConfigField(path = "world")
    public String world = "world";

    @ConfigField(path = "lift.corner-one.x")
    public int cornerOneX = 22;

    @ConfigField(path = "lift.corner-one.z")
    public int cornerOneZ = 22;

    @ConfigField(path = "lift.corner-two.x")
    public int cornerTwoX = 20;

    @ConfigField(path = "lift.corner-two.z")
    public int cornerTwoZ = 20;

    @ConfigField(path = "lift.bottom-block.down.y", comment = "the y level of the bottom block of the lift, whilst its at the bottom")
    public int bottomBlockDownY = 100;

    @ConfigField(path = "lift.bottom-block.up.y", comment = "the y level of the bottom block of the lift, whilst its at the top")
    public int bottomBlockUpY = 100;

    @ConfigField(path = "lift.height")
    public int height = 3;

    @ConfigField(path = "lift.travel-time", comment = "how many seconds should the lift take to go from the top to the bottom")
    public int time = 40;

    @ConfigField(path = "button-locations")
    public LocationList buttonLocations = LocationList.getDefault();
}
