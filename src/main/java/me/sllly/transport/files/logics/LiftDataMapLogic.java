package me.sllly.transport.files.logics;

import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import me.sllly.transport.objects.LiftData;
import me.sllly.transport.objects.lists.LiftDataMap;
import me.sllly.transport.objects.lists.LocationBlockDataList;
import me.sllly.transport.utils.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class LiftDataMapLogic extends ConfigTypeLogic<LiftDataMap> {
    @Override
    public LiftDataMap getFromConfig(ConfigurationSection config, String path) {
        LocationBlockDataListLogic locationBlockDataListLogic = new LocationBlockDataListLogic();
        LiftDataMap liftDataMap = new LiftDataMap();
        ConfigurationSection configurationSection = config.getConfigurationSection(path);
        for (String name : configurationSection.getKeys(false)) {
            boolean up = configurationSection.getBoolean(name+".up");
            boolean down = configurationSection.getBoolean(name+".down");
            LocationBlockDataList locationBlockDataList = locationBlockDataListLogic.getFromConfig(configurationSection, name+".blocks");
            LiftData liftData = new LiftData(name, up, down, locationBlockDataList);
            liftDataMap.put(name, liftData);
        }
        return liftDataMap;
    }

    @Override
    public void setInConfig(LiftDataMap instance, ConfigurationSection config, String path) {
        LocationBlockDataListLogic locationBlockDataListLogic = new LocationBlockDataListLogic();
        for (Map.Entry<String, LiftData> stringLiftDataEntry : instance.entrySet()) {
            String name = stringLiftDataEntry.getKey();
            LiftData liftData = stringLiftDataEntry.getValue();
            config.set(path+"."+name+".up", liftData.isDockedUp());
            config.set(path+"."+name+".down", liftData.isDockedDown());
            locationBlockDataListLogic.setInConfig(liftData.getLocationBlockDataList(), config, path+"."+name+".blocks");
        }
    }
}
