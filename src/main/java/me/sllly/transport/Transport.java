package me.sllly.transport;

import me.sllly.transport.files.TimedTeleportConfig;
import me.sllly.transport.files.logics.AreaConfigLogic;
import me.sllly.transport.objects.TimedTeleportTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class Transport extends JavaPlugin {

    public static Transport plugin;


    //Files
    public static TimedTeleportConfig timedTeleportConfig;

    @Override
    public void onEnable() {
        plugin = this;

        new AreaConfigLogic().register();

        reloadPlugin();

        TimedTeleportTask.startTeleportTaskTimerFromMax();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin(){
        timedTeleportConfig = new TimedTeleportConfig(getDataFolder(), "timed-teleport");
        timedTeleportConfig.initialize();
    }
}
