package bladesmp.jumppadplugin.manager;

import bladesmp.jumppadplugin.JumpPadPlugin;
import bladesmp.jumppadplugin.model.JumpPadRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private final JumpPadPlugin plugin;
    private final Map<String, JumpPadRegion> regions;
    private File regionsFile;
    private FileConfiguration regionsConfig;

    public RegionManager(JumpPadPlugin plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
        this.regionsFile = new File(plugin.getDataFolder(), "regions.yml");
    }

    public void loadRegions() {
        if (!regionsFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                regionsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Fehler beim Erstellen der regions.yml: " + e.getMessage());
                return;
            }
        }

        regionsConfig = YamlConfiguration.loadConfiguration(regionsFile);

        ConfigurationSection regionsSection = regionsConfig.getConfigurationSection("regions");
        if (regionsSection == null) return;

        for (String regionName : regionsSection.getKeys(false)) {
            ConfigurationSection regionSection = regionsSection.getConfigurationSection(regionName);
            if (regionSection == null) continue;

            try {
                JumpPadRegion region = JumpPadRegion.fromConfig(regionSection);
                regions.put(regionName, region);
            } catch (Exception e) {
                plugin.getLogger().warning("Fehler beim Laden der Region '" + regionName + "': " + e.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + regions.size() + " regions.");
    }

    public void saveRegions() {
        try {
            regionsConfig = new YamlConfiguration();

            for (Map.Entry<String, JumpPadRegion> entry : regions.entrySet()) {
                String regionName = entry.getKey();
                JumpPadRegion region = entry.getValue();

                ConfigurationSection regionSection = regionsConfig.createSection("regions." + regionName);
                region.saveToConfig(regionSection);
            }

            regionsConfig.save(regionsFile);

        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Speichern der regions.yml: " + e.getMessage());
        }
    }

    public boolean createRegion(String name, Location pos1, Location pos2) {
        if (regions.containsKey(name)) {
            return false;
        }

        ConfigManager config = plugin.getConfigManager();
        JumpPadRegion region = new JumpPadRegion(
                name,
                pos1,
                pos2,
                config.getDefaultJumpPadBlock(),
                config.getDefaultJumpStrength(),
                config.getDefaultSound()
        );

        regions.put(name, region);
        return true;
    }

    public boolean deleteRegion(String name) {
        return regions.remove(name) != null;
    }

    public JumpPadRegion getRegion(String name) {
        return regions.get(name);
    }

    public JumpPadRegion getRegionAt(Location location) {
        for (JumpPadRegion region : regions.values()) {
            if (region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public boolean regionExists(String name) {
        return regions.containsKey(name);
    }

    public Map<String, JumpPadRegion> getAllRegions() {
        return new HashMap<>(regions);
    }

    public boolean setRegionBlock(String regionName, Material block) {
        JumpPadRegion region = regions.get(regionName);
        if (region == null) return false;

        region.setJumpPadBlock(block);
        return true;
    }

    public boolean setRegionStrength(String regionName, double strength) {
        JumpPadRegion region = regions.get(regionName);
        if (region == null) return false;

        region.setJumpStrength(strength);
        return true;
    }

    public boolean setRegionSound(String regionName, Sound sound) {
        JumpPadRegion region = regions.get(regionName);
        if (region == null) return false;

        region.setSound(sound);
        return true;
    }
}