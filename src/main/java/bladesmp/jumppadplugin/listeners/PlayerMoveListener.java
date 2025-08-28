package bladesmp.jumppadplugin.listeners;

import bladesmp.jumppadplugin.JumpPadPlugin;
import bladesmp.jumppadplugin.manager.ConfigManager;
import bladesmp.jumppadplugin.manager.RegionManager;
import bladesmp.jumppadplugin.model.JumpPadRegion;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final JumpPadPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 1000; // 1 second cooldown

    public PlayerMoveListener(JumpPadPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();

        // Check if player actually moved to a new block
        if (to.getBlockX() == from.getBlockX() &&
                to.getBlockY() == from.getBlockY() &&
                to.getBlockZ() == from.getBlockZ()) {
            return;
        }

        // Check cooldown
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerId)) {
            long lastJump = cooldowns.get(playerId);
            if (currentTime - lastJump < COOLDOWN_TIME) {
                return;
            }
        }

        RegionManager regionManager = plugin.getRegionManager();
        JumpPadRegion region = regionManager.getRegionAt(to);

        if (region == null) {
            return;
        }

        // Check if player is standing on the correct block
        Location blockBelow = to.clone().subtract(0, 1, 0);
        Material blockType = blockBelow.getBlock().getType();

        if (blockType != region.getJumpPadBlock()) {
            return;
        }

        // Perform the jump using Folia-compatible scheduling
        plugin.getServer().getGlobalRegionScheduler().run(plugin, (task) -> {
            performJump(player, region);
            cooldowns.put(playerId, currentTime);
        });
    }

    private void performJump(Player player, JumpPadRegion region) {
        // Calculate jump vector
        double strength = region.getJumpStrength();
        Vector jumpVector = new Vector(0, strength, 0);

        // Apply velocity
        player.setVelocity(jumpVector);

        // Play sound
        player.playSound(player.getLocation(), region.getSound(), 1.0f, 1.0f);

        // Send message if enabled
        ConfigManager config = plugin.getConfigManager();
        if (config.areMessagesEnabled()) {
            Component message = plugin.getMiniMessage().deserialize(config.getJumpMessage());
            player.sendMessage(message);
        }
    }
}