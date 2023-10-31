package me.sllly.transport.files.data;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;
import me.sllly.transport.objects.lists.LiftDataMap;

import java.io.File;

public class LiftDataFile extends AnnotatedConfig {
    public LiftDataFile(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "lift-data", comment = "Please don't change any of this")
    public LiftDataMap liftDataMap = LiftDataMap.getDefault();
}
