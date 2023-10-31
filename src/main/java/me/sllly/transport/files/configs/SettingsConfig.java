package me.sllly.transport.files.configs;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;

public class SettingsConfig extends AnnotatedConfig {
    public SettingsConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "teleporter-paused-placeholder")
    public static String pausedPlaceholder = "&cPaused";
}
