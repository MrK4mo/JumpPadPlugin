package bladesmp.jumppadplugin;

import bladesmp.jumppadplugin.commands.JumpPadCommand;
import bladesmp.jumppadplugin.listeners.PlayerMoveListener;
import bladesmp.jumppadplugin.manager.ConfigManager;
import bladesmp.jumppadplugin.manager.RegionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class JumpPadPlugin extends JavaPlugin {

    private static JumpPadPlugin instance;
    private ConfigManager configManager;
    private RegionManager regionManager;
    private MiniMessage miniMessage;

    @Override
    public void onEnable() {
        instance = this;
        miniMessage = MiniMessage.miniMessage();

        // Initialize managers
        configManager = new ConfigManager(this);
        regionManager = new RegionManager(this);

        // Load configurations
        configManager.loadConfig();
        regionManager.loadRegions();

        // Register commands
        getCommand("jumppad").setExecutor(new JumpPadCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        getLogger().info("JumpPad Plugin wurde erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        if (regionManager != null) {
            regionManager.saveRegions();
        }
        getLogger().info("JumpPad Plugin wurde deaktiviert!");
    }

    public static JumpPadPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
}