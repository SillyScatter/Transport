package me.sllly.transport;

import me.sllly.transport.commands.TransportCommandSystem;
import me.sllly.transport.files.TimedTeleportConfig;
import me.sllly.transport.files.logics.AreaConfigLogic;
import me.sllly.transport.objects.TeleportPad;
import me.sllly.transport.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Transport extends JavaPlugin {

    public static Transport plugin;


    //Files
    public File padDirectory;


    @Override
    public void onEnable() {
        plugin = this;

        //Config Logics
        new AreaConfigLogic().register();

        //Make pads file
        padDirectory = new File(getDataFolder(), "Teleport Pads");
        if (!padDirectory.exists()) {
            padDirectory.mkdirs();
        }

        //Register Command System
        new TransportCommandSystem("transport").registerCommandBranch(this);

        //Load Files
        reloadPlugin();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin(){
        reloadTeleportPads();
    }

    public void reloadTeleportPads(){
        int count = 0;
        TeleportPad.teleportPads.clear();
        File[] padFiles = padDirectory.listFiles();
        if (padFiles == null || padFiles.length == 0){
            new TimedTeleportConfig(new File(padDirectory, "example.yml")).initialize();
        }
        padFiles = padDirectory.listFiles();
        for (File kitFile : padFiles) {
            TimedTeleportConfig padConfig = new TimedTeleportConfig(kitFile);
            padConfig.initialize();
            TeleportPad teleportPad = new TeleportPad(padConfig.teleportPadId, padConfig.teleportArea, padConfig.targetLocation, padConfig.activationTime, padConfig.beenTeleported,padConfig.commands);
            teleportPad.startFromSave();
            TeleportPad.teleportPads.put(padConfig.teleportPadId, teleportPad);
            count++;
        }
        Util.log("&aSuccessfully registered teleport pads: &2&l"+count);
    }
}
