package bladesmp.jumppadplugin.manager;

import bladesmp.jumppadplugin.JumpPadPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JumpPadPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    // Default config values
    private Material defaultJumpPadBlock = Material.SLIME_BLOCK;
    private double defaultJumpStrength = 1.5;
    private Sound defaultSound = Sound.ENTITY_SLIME_SQUISH;
    private boolean messagesEnabled = true;
    private String jumpMessage = "<green>Du wurdest von einem JumpPad katapultiert!";
    private String regionCreatedMessage = "<green>Region '<region>' wurde erfolgreich erstellt!";
    private String regionDeletedMessage = "<red>Region '<region>' wurde gelöscht!";
    private String noPermissionMessage = "<red>Du hast keine Berechtigung für diesen Befehl!";
    private String regionNotFoundMessage = "<red>Region '<region>' wurde nicht gefunden!";

    public ConfigManager(JumpPadPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
            createDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        loadValues();
    }

    private void createDefaultConfig() {
        try {
            plugin.getDataFolder().mkdirs();
            configFile.createNewFile();

            config = YamlConfiguration.loadConfiguration(configFile);

            // JumpPad settings
            config.set("jumppad.default-block", defaultJumpPadBlock.name());
            config.set("jumppad.default-strength", defaultJumpStrength);
            config.set("jumppad.default-sound", defaultSound.name());

            // Message settings
            config.set("messages.enabled", messagesEnabled);
            config.set("messages.jump", jumpMessage);
            config.set("messages.region-created", regionCreatedMessage);
            config.set("messages.region-deleted", regionDeletedMessage);
            config.set("messages.no-permission", noPermissionMessage);
            config.set("messages.region-not-found", regionNotFoundMessage);

            config.save(configFile);

        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Erstellen der config.yml: " + e.getMessage());
        }
    }

    private void loadValues() {
        try {
            defaultJumpPadBlock = Material.valueOf(config.getString("jumppad.default-block", "SLIME_BLOCK"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Ungültiger Block in config.yml, verwende SLIME_BLOCK");
            defaultJumpPadBlock = Material.SLIME_BLOCK;
        }

        defaultJumpStrength = config.getDouble("jumppad.default-strength", 1.5);

        try {
            defaultSound = Sound.valueOf(config.getString("jumppad.default-sound", "ENTITY_SLIME_SQUISH"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Ungültiger Sound in config.yml, verwende ENTITY_SLIME_SQUISH");
            defaultSound = Sound.ENTITY_SLIME_SQUISH;
        }

        messagesEnabled = config.getBoolean("messages.enabled", true);
        jumpMessage = config.getString("messages.jump", "<green>Du wurdest von einem JumpPad katapultiert!");
        regionCreatedMessage = config.getString("messages.region-created", "<green>Region '<region>' wurde erfolgreich erstellt!");
        regionDeletedMessage = config.getString("messages.region-deleted", "<red>Region '<region>' wurde gelöscht!");
        noPermissionMessage = config.getString("messages.no-permission", "<red>Du hast keine Berechtigung für diesen Befehl!");
        regionNotFoundMessage = config.getString("messages.region-not-found", "<red>Region '<region>' wurde nicht gefunden!");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Speichern der config.yml: " + e.getMessage());
        }
    }

    // Getters
    public Material getDefaultJumpPadBlock() { return defaultJumpPadBlock; }
    public double getDefaultJumpStrength() { return defaultJumpStrength; }
    public Sound getDefaultSound() { return defaultSound; }
    public boolean areMessagesEnabled() { return messagesEnabled; }
    public String getJumpMessage() { return jumpMessage; }
    public String getRegionCreatedMessage() { return regionCreatedMessage; }
    public String getRegionDeletedMessage() { return regionDeletedMessage; }
    public String getNoPermissionMessage() { return noPermissionMessage; }
    public String getRegionNotFoundMessage() { return regionNotFoundMessage; }
}