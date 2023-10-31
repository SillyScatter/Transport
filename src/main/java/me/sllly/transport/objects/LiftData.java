package me.sllly.transport.objects;

import me.sllly.transport.objects.lists.LocationBlockDataList;
import org.bukkit.block.Block;

import java.util.List;

public class LiftData {

    private String name;
    private boolean dockedUp;
    private boolean dockedDown;
    private LocationBlockDataList locationBlockDataList;

    public LiftData(String name, boolean dockedUp, boolean dockedDown, LocationBlockDataList locationBlockDataList) {
        this.name = name;
        this.dockedUp = dockedUp;
        this.dockedDown = dockedDown;
        this.locationBlockDataList = locationBlockDataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDockedUp() {
        return dockedUp;
    }

    public void setDockedUp(boolean dockedUp) {
        this.dockedUp = dockedUp;
    }

    public boolean isDockedDown() {
        return dockedDown;
    }

    public void setDockedDown(boolean dockedDown) {
        this.dockedDown = dockedDown;
    }

    public LocationBlockDataList getLocationBlockDataList() {
        return locationBlockDataList;
    }

    public void setLocationBlockDataList(LocationBlockDataList locationBlockDataList) {
        this.locationBlockDataList = locationBlockDataList;
    }
}