package me.sllly.transport.files.logics;

import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import dev.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.transport.objects.LocationBlockData;
import me.sllly.transport.objects.lists.LocationBlockDataList;
import me.sllly.transport.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.configuration.ConfigurationSection;

public class LocationBlockDataListLogic extends ConfigTypeLogic<LocationBlockDataList> {
    @Override
    public LocationBlockDataList getFromConfig(ConfigurationSection config, String path) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        int i = 0;
        LocationBlockDataList locationBlockDataList = new LocationBlockDataList();
        ConfigurationSection configurationSection = config.getConfigurationSection(path);
        for (String count : configurationSection.getKeys(false)) {
            Location location = locationConfigLogic.getFromConfig(configurationSection, count+".location");
            String materialName = configurationSection.getString(count+".data.material");
            Material material = Material.getMaterial(materialName.toUpperCase());
            if (material == null) {
                Util.log("&cFailed to get material: "+materialName);
                continue;
            }
            BlockData blockData = material.createBlockData();
            if (blockData instanceof Slab){
                String slabTypeName = configurationSection.getString(count+".data.slab-type");
                Slab slab = (Slab) blockData;
                Slab.Type slabType = Slab.Type.valueOf(slabTypeName.toUpperCase());
                if (slabType == null) {
                    Util.log("&cFailed to get slab-type: "+slabTypeName);
                }
                slab.setType(slabType);
                blockData = slab;
            }
            locationBlockDataList.add(new LocationBlockData(location, blockData));
            i++;
        }
        Util.log("&aPulled blocks from data: "+i);
        return locationBlockDataList;
    }

    @Override
    public void setInConfig(LocationBlockDataList instance, ConfigurationSection config, String path) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        int count = 0;
        for (LocationBlockData locationBlockData : instance) {
            count++;
            locationConfigLogic.setInConfig(locationBlockData.getLocation(), config, path+"."+count+".location");
            Material material = locationBlockData.getBlockData().getMaterial();
            String materialName = material.name();
            config.set(path+"."+count+".data.material", materialName);
            if (locationBlockData.getBlockData() instanceof Slab){
                Slab slab = (Slab) locationBlockData.getBlockData();
                config.set(path+"."+count+".data.slab-type", slab.getType().toString());
            }
        }
    }
}
