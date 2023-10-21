package me.sllly.transport.files.logics;

import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import me.sllly.transport.objects.Area;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class AreaConfigLogic extends ConfigTypeLogic<Area> {
    @Override
    public Area getFromConfig(ConfigurationSection config, String path) {

        ConfigTypeLogic<Location> locationConfigTypeLogic = (ConfigTypeLogic<Location>) ConfigTypeLogic.getConfigTypeLogic(Location.class, "raiding-worldless");

        Location cornerOne = locationConfigTypeLogic.getFromConfig(config, path + "corner-one");
        Location cornerTwo = locationConfigTypeLogic.getFromConfig(config, path + "corner-two");
        return new Area(cornerOne, cornerTwo);
    }

    @Override
    public void setInConfig(Area instance, ConfigurationSection config, String path) {

        ConfigTypeLogic<Location> locationConfigTypeLogic = (ConfigTypeLogic<Location>) ConfigTypeLogic.getConfigTypeLogic(Location.class, "raiding-worldless");

        locationConfigTypeLogic.setInConfig(instance.getCornerOne(), config, path + ".corner-one");
        locationConfigTypeLogic.setInConfig(instance.getCornerTwo(), config, path + ".corner-two");
    }
}