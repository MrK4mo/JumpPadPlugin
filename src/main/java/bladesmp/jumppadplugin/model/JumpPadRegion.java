package bladesmp.jumppadplugin.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class JumpPadRegion {

    private final String name;
    private final Location pos1;
    private final Location pos2;
    private Material jumpPadBlock;
    private double jumpStrength;
    private Sound sound;

    // Cached min/max values for performance
    private final int minX, maxX, minY, maxY, minZ, maxZ;
    private final World world;

    public JumpPadRegion(String name, Location pos1, Location pos2, Material jumpPadBlock, double jumpStrength, Sound sound) {
        this.name = name;
        this.pos1 = pos1.clone();
        this.pos2 = pos2.clone();
        this.jumpPadBlock = jumpPadBlock;
        this.jumpStrength = jumpStrength;
        this.sound = sound;

        // Calculate boundaries
        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        this.minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        this.maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        this.world = pos1.getWorld();
    }

    public boolean contains(Location location) {
        if (!location.getWorld().equals(world)) return false;

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public void saveToConfig(ConfigurationSection section) {
        // Save positions
        section.set("world", world.getName());
        section.set("pos1.x", pos1.getBlockX());
        section.set("pos1.y", pos1.getBlockY());
        section.set("pos1.z", pos1.getBlockZ());
        section.set("pos2.x", pos2.getBlockX());
        section.set("pos2.y", pos2.getBlockY());
        section.set("pos2.z", pos2.getBlockZ());

        // Save settings
        section.set("jump-pad-block", jumpPadBlock.name());
        section.set("jump-strength", jumpStrength);
        section.set("sound", sound.name());
    }

    public static JumpPadRegion fromConfig(ConfigurationSection section) {
        String worldName = section.getString("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("World '" + worldName + "' not found");
        }

        Location pos1 = new Location(world,
                section.getInt("pos1.x"),
                section.getInt("pos1.y"),
                section.getInt("pos1.z")
        );

        Location pos2 = new Location(world,
                section.getInt("pos2.x"),
                section.getInt("pos2.y"),
                section.getInt("pos2.z")
        );

        Material jumpPadBlock = Material.valueOf(section.getString("jump-pad-block"));
        double jumpStrength = section.getDouble("jump-strength");
        Sound sound = Sound.valueOf(section.getString("sound"));

        return new JumpPadRegion(section.getName(), pos1, pos2, jumpPadBlock, jumpStrength, sound);
    }

    // Getters and Setters
    public String getName() { return name; }
    public Location getPos1() { return pos1.clone(); }
    public Location getPos2() { return pos2.clone(); }
    public Material getJumpPadBlock() { return jumpPadBlock; }
    public void setJumpPadBlock(Material jumpPadBlock) { this.jumpPadBlock = jumpPadBlock; }
    public double getJumpStrength() { return jumpStrength; }
    public void setJumpStrength(double jumpStrength) { this.jumpStrength = jumpStrength; }
    public Sound getSound() { return sound; }
    public void setSound(Sound sound) { this.sound = sound; }
    public World getWorld() { return world; }
}