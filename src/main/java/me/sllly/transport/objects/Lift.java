package me.sllly.transport.objects;

import me.sllly.transport.Transport;
import me.sllly.transport.objects.lists.LocationBlockDataList;
import me.sllly.transport.objects.lists.LocationList;
import me.sllly.transport.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lift {

    public static Map<String, Lift> lifts = new HashMap<>();
    public static Map<String, Lift> movingLifts = new HashMap<>();

    private String name;
    private World world;
    private int cornerOneX;
    private int cornerOneZ;
    private int cornerTwoX;
    private int cornerTwoZ;
    private int bottomYDown;
    private int bottomYTop;
    private int height;
    private int seconds;
    private LocationList buttonLocations;

    private Area noFallDamageArea;
    private double stepMagnitude;
    private Area totalLiftArea;

    private LiftData liftData;

    private boolean movingUp;
    private boolean movingDown;
    private double step;
    private Area currentLiftArea;

    private List<Block> nonAirBlocks;
    private List<BlockDisplay> liftDisplayBlocks;

    private List<Player> playersThatJoined;

    private List<Player> playersThatLeft;
    private Map<Player, BlockDisplay> playerChairMap;

    public Lift(String name, World world, int cornerOneX, int cornerOneZ, int cornerTwoX, int cornerTwoZ, int bottomYDown, int bottomYTop, int height, int seconds, LocationList buttonLocations, LiftData liftData) {
        this.name = name;
        this.world = world;
        this.cornerOneX = cornerOneX;
        this.cornerOneZ = cornerOneZ;
        this.cornerTwoX = cornerTwoX;
        this.cornerTwoZ = cornerTwoZ;
        this.bottomYDown = bottomYDown;
        this.bottomYTop = bottomYTop;
        this.height = height;
        this.seconds = seconds;
        this.buttonLocations = buttonLocations;

        this.totalLiftArea = new Area(new Location(world, cornerOneX, bottomYDown, cornerOneZ), new Location(world, cornerTwoX, bottomYTop+height, cornerTwoZ));
        this.noFallDamageArea = new Area(new Location(world, cornerOneX, 324, cornerOneZ), new Location(world, cornerTwoX, -64, cornerTwoZ));
        this.stepMagnitude = (bottomYTop-bottomYDown)/(seconds*20.0);

        this.liftData = liftData;

        performCrashStability();
    }

    public void startLift(){
        //To be called IF and ONLY IF the lift is ready to move.
        movingLifts.put(name, this);
        List<Block> blocks = totalLiftArea.getBlocksWithinArea();
        setNonAirBlocks(blocks);
        LocationBlockDataList locationBlockDataList = LocationBlockDataList.getLocationBlockDataList(blocks);
        liftData.setLocationBlockDataList(locationBlockDataList);

        //Players
        playerChairMap = new HashMap<>();
        playersThatJoined = new ArrayList<>();

        //Work Out if we're boutta be moving up or down
        if (getLiftData().isDockedDown()){
            getLiftData().setDockedDown(false);
            setMovingUp(true);
            currentLiftArea = new Area(world.getName(), cornerOneX, bottomYDown, cornerOneZ, cornerTwoX, bottomYDown+height, cornerTwoZ);
            currentLiftArea.adjustAreaFromBlocks();
        }
        if (getLiftData().isDockedUp()){
            getLiftData().setDockedUp(false);
            setMovingDown(true);
            currentLiftArea = new Area(world.getName(), cornerOneX, bottomYTop, cornerOneZ, cornerTwoX, bottomYTop+height, cornerTwoZ);
            currentLiftArea.adjustAreaFromBlocks();
        }
        if(movingDown){
            step = stepMagnitude * -1;
        }
        if (movingUp){
            step = stepMagnitude;
        }
//        Util.log("&2Starting lift "+name+" with velocity: "+ step);
//        Util.log("&2Magnitude: "+ stepMagnitude);

        transformIntoBlockDisplays();
        setBlocksToAir();

        moveLift();
        Transport.plugin.liftDataFile.liftDataMap.put(name, liftData);
    }

    public void moveLift(){
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                if ((ticks / 20) == seconds){
                    liftFinished();
                    this.cancel();
                    return;
                }
                for (BlockDisplay liftDisplayBlock : liftDisplayBlocks) {
                    liftDisplayBlock.teleport(liftDisplayBlock.getLocation().add(0, step, 0));
                }
                for (Entity entity : currentLiftArea.getEntitiesWithinArea()) {
                    if (entity instanceof Player){
                        Player player = (Player) entity;
                        if (!playersThatJoined.contains(player)){
                            if (currentLiftArea.withinArea(player.getLocation())){
                                startMovingPlayer(player);
                            }
                        }
                    }
                }
                for (Player player : playerChairMap.keySet()) {
                    BlockDisplay blockDisplay = playerChairMap.get(player);
                    blockDisplay.removePassenger(player);
                    blockDisplay.teleport( blockDisplay.getLocation().add(0,step,0));
                    blockDisplay.addPassenger(player);
                }

                currentLiftArea.moveArea(0,step,0);
            }
        }.runTaskTimer(Transport.plugin, 0, 1);
    }

    public void startMovingPlayer(Player player){
        playersThatJoined.add(player);
        BlockDisplay blockDisplay = Util.createBlockDisplay(Material.AIR.createBlockData(), player.getLocation());
        playerChairMap.put(player, blockDisplay);
        blockDisplay.addPassenger(player);
    }

    public void liftFinished(){
        if (movingUp){
            movingUp = false;
            liftData.setDockedUp(true);
        }
        if (movingDown){
            movingDown = false;
            liftData.setDockedDown(true);
        }
        for (BlockDisplay liftDisplayBlock : liftDisplayBlocks) {
            liftDisplayBlock.remove();
        }
        liftDisplayBlocks.clear();
        transformIntoBlocks();
        movingLifts.remove(name);

        List<Block> blocks = totalLiftArea.getBlocksWithinArea();
        setNonAirBlocks(blocks);
        LocationBlockDataList locationBlockDataList = LocationBlockDataList.getLocationBlockDataList(blocks);
        liftData.setLocationBlockDataList(locationBlockDataList);

        for (Player player : playerChairMap.keySet()) {
            BlockDisplay blockDisplay = playerChairMap.get(player);
            blockDisplay.removePassenger(player);
            blockDisplay.remove();
            player.teleport(player.getLocation().add(0,0.5,0));
        }

        playerChairMap.clear();
        playersThatJoined.clear();

        Transport.plugin.liftDataFile.liftDataMap.put(name, liftData);
    }

    public void transformIntoBlocks(){
        LocationBlockDataList locationBlockDataList = getLiftData().getLocationBlockDataList();
        int blockDisplacement = 0;
        int topMinusBottom = bottomYTop-bottomYDown;

        if (liftData.isDockedUp()){
            blockDisplacement = topMinusBottom;
        }
        if (liftData.isDockedDown()){
            blockDisplacement = topMinusBottom * -1;
        }
        for (LocationBlockData locationBlockData : locationBlockDataList) {
            Location blockLocation = locationBlockData.getLocation().add(0,blockDisplacement,0);
            Block block = blockLocation.getBlock();
            block.setBlockData(locationBlockData.getBlockData());
        }
    }

    public void transformIntoBlockDisplays(){
        List<BlockDisplay> blockDisplays = new ArrayList<>();
        if(nonAirBlocks.size() == 0){
            Util.log("&cUgh Oh, there aren't any blocks to transform into block displays");
        }
        for (Block block : nonAirBlocks) {
            if (block == null) {
                continue;
            }
            if (block.getType()== Material.AIR){
                continue;
            }
            BlockDisplay blockDisplay = Util.createBlockDisplay(block.getBlockData(), block.getLocation());
            blockDisplay.setPersistent(false);
            NamespacedKey key = new NamespacedKey(Transport.plugin, "transport");
            blockDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "yes");
            blockDisplays.add(blockDisplay);
        }
        liftDisplayBlocks = blockDisplays;
    }

    public void setBlocksToAir(){
        for (Block nonAirBlock : nonAirBlocks) {
            nonAirBlock.setType(Material.AIR);
        }
    }

    public void setNonAirBlocks(List<Block> blocks){
        List<Block> nonAirBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (block == null) {
                continue;
            }
            if (block.getType()== Material.AIR){
                continue;
            }
            nonAirBlocks.add(block);
        }
        this.nonAirBlocks = nonAirBlocks;
    }

    public void performCrashStability(){
        for (Block block : totalLiftArea.getBlocksWithinArea()) {
            if (block!=null && block.getType()!=Material.AIR){
                block.setType(Material.AIR);
            }
        }

        int lowestY = 400;
        LocationBlockDataList locationBlockDataList = liftData.getLocationBlockDataList();
        for (LocationBlockData locationBlockData : locationBlockDataList) {
            if (locationBlockData.getLocation().getY() < lowestY){
                lowestY = (int) Math.floor(locationBlockData.getLocation().getY());
            }
            if (locationBlockData.getLocation().getBlock().getType() != locationBlockData.getBlockData().getMaterial()){
                locationBlockData.getLocation().getBlock().setBlockData(locationBlockData.getBlockData());
            }
        }
        int distanceFromLowestYToBottomYDown = Math.abs(lowestY-bottomYDown);
        int distanceFromLowestYToBottomYTop = Math.abs(lowestY-bottomYTop);

        if (distanceFromLowestYToBottomYDown < distanceFromLowestYToBottomYTop){
            liftData.setDockedDown(true);
            liftData.setDockedUp(false);
        }else{
            liftData.setDockedDown(false);
            liftData.setDockedUp(true);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getCornerOneX() {
        return cornerOneX;
    }

    public void setCornerOneX(int cornerOneX) {
        this.cornerOneX = cornerOneX;
    }

    public int getCornerOneZ() {
        return cornerOneZ;
    }

    public void setCornerOneZ(int cornerOneZ) {
        this.cornerOneZ = cornerOneZ;
    }

    public int getCornerTwoX() {
        return cornerTwoX;
    }

    public void setCornerTwoX(int cornerTwoX) {
        this.cornerTwoX = cornerTwoX;
    }

    public int getCornerTwoZ() {
        return cornerTwoZ;
    }

    public void setCornerTwoZ(int cornerTwoZ) {
        this.cornerTwoZ = cornerTwoZ;
    }

    public int getBottomYDown() {
        return bottomYDown;
    }

    public void setBottomYDown(int bottomYDown) {
        this.bottomYDown = bottomYDown;
    }

    public int getBottomYTop() {
        return bottomYTop;
    }

    public void setBottomYTop(int bottomYTop) {
        this.bottomYTop = bottomYTop;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public LocationList getButtonLocations() {
        return buttonLocations;
    }

    public void setButtonLocations(LocationList buttonLocations) {
        this.buttonLocations = buttonLocations;
    }

    public Area getNoFallDamageArea() {
        return noFallDamageArea;
    }

    public void setNoFallDamageArea(Area noFallDamageArea) {
        this.noFallDamageArea = noFallDamageArea;
    }

    public double getStepMagnitude() {
        return stepMagnitude;
    }

    public void setStepMagnitude(double stepMagnitude) {
        this.stepMagnitude = stepMagnitude;
    }

    public LiftData getLiftData() {
        return liftData;
    }

    public void setLiftData(LiftData liftData) {
        this.liftData = liftData;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public Area getCurrentLiftArea() {
        return currentLiftArea;
    }

    public void setCurrentLiftArea(Area currentLiftArea) {
        this.currentLiftArea = currentLiftArea;
    }

    public List<BlockDisplay> getLiftDisplayBlocks() {
        return liftDisplayBlocks;
    }

    public void setLiftDisplayBlocks(List<BlockDisplay> liftDisplayBlocks) {
        this.liftDisplayBlocks = liftDisplayBlocks;
    }

    public Map<Player, BlockDisplay> getPlayerChairMap() {
        return playerChairMap;
    }

    public void setPlayerChairMap(Map<Player, BlockDisplay> playerChairMap) {
        this.playerChairMap = playerChairMap;
    }

    public Area getTotalLiftArea() {
        return totalLiftArea;
    }

    public void setTotalLiftArea(Area totalLiftArea) {
        this.totalLiftArea = totalLiftArea;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public List<Block> getNonAirBlocks() {
        return nonAirBlocks;
    }

    public List<Player> getPlayersThatJoined() {
        return playersThatJoined;
    }

    public void setPlayersThatJoined(List<Player> playersThatJoined) {
        this.playersThatJoined = playersThatJoined;
    }
}
