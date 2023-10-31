package me.sllly.transport.objects.lists;

import me.sllly.transport.objects.LiftData;

import java.util.HashMap;

public class LiftDataMap extends HashMap<String, LiftData> {

    public static LiftDataMap getDefault(){
        LiftDataMap liftDataMap = new LiftDataMap();
        LiftData liftData = new LiftData("test", false, true, LocationBlockDataList.getDefault());
        liftDataMap.put("test", liftData);
        return liftDataMap;
    }
}
