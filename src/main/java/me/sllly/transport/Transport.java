package me.sllly.transport;

import me.sllly.transport.commands.TransportCommandSystem;
import me.sllly.transport.files.configs.LiftConfig;
import me.sllly.transport.files.configs.TimedTeleportConfig;
import me.sllly.transport.files.configs.SettingsConfig;
import me.sllly.transport.files.data.LiftDataFile;
import me.sllly.transport.files.logics.AreaConfigLogic;
import me.sllly.transport.files.logics.LiftDataMapLogic;
import me.sllly.transport.files.logics.LocationBlockDataListLogic;
import me.sllly.transport.files.logics.LocationListLogic;
import me.sllly.transport.listeners.ActivateLiftListener;
import me.sllly.transport.objects.Lift;
import me.sllly.transport.objects.LiftData;
import me.sllly.transport.objects.TeleportPad;
import me.sllly.transport.objects.lists.LocationBlockDataList;
import me.sllly.transport.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Transport extends JavaPlugin {

    public static Transport plugin;


    //Files
    public File padDirectory;
    public File liftDirectory;
    public SettingsConfig settingsConfig;
    public LiftDataFile liftDataFile;

    @Override
    public void onEnable() {
        plugin = this;

        //Config Logics
        new AreaConfigLogic().register();
        new LocationListLogic().register();
        new LocationBlockDataListLogic().register();
        new LiftDataMapLogic().register();

        //Make pads file
        padDirectory = new File(getDataFolder(), "Teleport Pads");
        if (!padDirectory.exists()) {
            padDirectory.mkdirs();
        }

        liftDirectory = new File(getDataFolder(), "Lifts");
        if (!liftDirectory.exists()){
            liftDirectory.mkdirs();
        }

        //Register Command System
        new TransportCommandSystem("transport").registerCommandBranch(this);

        //Register Listeners
        getServer().getPluginManager().registerEvents(new ActivateLiftListener(), this);

        //Load Files
        liftDataFile = new LiftDataFile(getDataFolder(), "lift-data");
        liftDataFile.initialize();
        reloadPlugin();
    }

    @Override
    public void onDisable() {
        Util.log("&aSuccessfully saved lift data to file: "+liftDataFile.liftDataMap.size());
        liftDataFile.saveToFile();
        for (Lift value : Lift.movingLifts.values()) {
            value.liftFinished();
        }
    }

    public void reloadPlugin(){
        settingsConfig = new SettingsConfig(getDataFolder(), "settings");
        settingsConfig.initialize();

        reloadTeleportPads();
        reloadLifts();
    }

    public void reloadTeleportPads(){
        int count = 0;
        TeleportPad.teleportPads.clear();
        File[] padFiles = padDirectory.listFiles();
        if (padFiles == null || padFiles.length == 0){
            new TimedTeleportConfig(new File(padDirectory, "example.yml")).initialize();
        }
        padFiles = padDirectory.listFiles();
        for (File padFile : padFiles) {
            TimedTeleportConfig padConfig = new TimedTeleportConfig(padFile);
            padConfig.initialize();
            TeleportPad teleportPad = new TeleportPad(padConfig.teleportPadId, padConfig.teleportArea, padConfig.targetLocation, padConfig.activationTime, padConfig.beenTeleported,padConfig.commands);
            teleportPad.startFromSave();
            TeleportPad.teleportPads.put(padConfig.teleportPadId, teleportPad);
            count++;
        }
        Util.log("&aSuccessfully registered teleport pads: &2&l"+count);
    }

    public void reloadLifts(){
        int count = 0;
        liftDataFile.saveToFile();
        Lift.lifts.clear();
        File[] liftFiles = liftDirectory.listFiles();
        if (liftFiles == null || liftFiles.length == 0){
            new LiftConfig(new File(liftDirectory, "example.yml")).initialize();
            Util.log("&bMaking first lift file");
        }
        liftFiles = liftDirectory.listFiles();
        for (File liftFile : liftFiles) {
            LiftConfig liftConfig = new LiftConfig(liftFile);
            liftConfig.initialize();
            LiftData liftData = liftDataFile.liftDataMap.get(liftConfig.name);
            if (liftData == null) {
                Util.log("&cError getting liftData: "+liftConfig.name+". Creating default liftData for this lift.");
                liftData = new LiftData(liftConfig.name, false, true, new LocationBlockDataList());
            }
            World world = Bukkit.getWorld(liftConfig.world);
            if (world == null) {
                Util.log("&cError getting world: "+liftConfig.world);
                continue;
            }
            Lift lift = new Lift(liftConfig.name, world, liftConfig.cornerOneX, liftConfig.cornerOneZ, liftConfig.cornerTwoX, liftConfig.cornerTwoZ, liftConfig.bottomBlockDownY, liftConfig.bottomBlockUpY, liftConfig.height, liftConfig.time, liftConfig.buttonLocations, liftData);
            Lift.lifts.put(liftConfig.name, lift);
            count++;
        }
        Util.log("&aSuccessfully registered lifts: "+count);
    }
}
