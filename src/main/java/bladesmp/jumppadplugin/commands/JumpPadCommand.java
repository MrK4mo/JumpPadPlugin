package bladesmp.jumppadplugin.commands;

import bladesmp.jumppadplugin.JumpPadPlugin;
import bladesmp.jumppadplugin.manager.ConfigManager;
import bladesmp.jumppadplugin.manager.RegionManager;
import bladesmp.jumppadplugin.model.JumpPadRegion;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JumpPadCommand implements CommandExecutor, TabCompleter {

    private final JumpPadPlugin plugin;
    private final Map<Player, Location> pos1Map = new HashMap<>();
    private final Map<Player, Location> pos2Map = new HashMap<>();

    public JumpPadCommand(JumpPadPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("jumppad.admin")) {
            sendMessage(player, plugin.getConfigManager().getNoPermissionMessage());
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "pos1":
                setPosition(player, 1);
                break;
            case "pos2":
                setPosition(player, 2);
                break;
            case "create":
                if (args.length < 2) {
                    sendMessage(player, "<red>Verwendung: /jumppad create <name>");
                    return true;
                }
                createRegion(player, args[1]);
                break;
            case "delete":
                if (args.length < 2) {
                    sendMessage(player, "<red>Verwendung: /jumppad delete <name>");
                    return true;
                }
                deleteRegion(player, args[1]);
                break;
            case "list":
                listRegions(player);
                break;
            case "info":
                if (args.length < 2) {
                    sendMessage(player, "<red>Verwendung: /jumppad info <name>");
                    return true;
                }
                showRegionInfo(player, args[1]);
                break;
            case "setblock":
                if (args.length < 3) {
                    sendMessage(player, "<red>Verwendung: /jumppad setblock <region> <block>");
                    return true;
                }
                setRegionBlock(player, args[1], args[2]);
                break;
            case "setstrength":
                if (args.length < 3) {
                    sendMessage(player, "<red>Verwendung: /jumppad setstrength <region> <stärke>");
                    return true;
                }
                setRegionStrength(player, args[1], args[2]);
                break;
            case "setforward": // Neuer Befehl für Vorwärtssprung
                if (args.length < 3) {
                    sendMessage(player, "<red>Verwendung: /jumppad setforward <region> <vorwärtsstärke>");
                    return true;
                }
                setRegionForwardStrength(player, args[1], args[2]);
                break;
            case "setsound":
                if (args.length < 3) {
                    sendMessage(player, "<red>Verwendung: /jumppad setsound <region> <sound>");
                    return true;
                }
                setRegionSound(player, args[1], args[2]);
                break;
            case "reload":
                reloadPlugin(player);
                break;
            default:
                sendHelpMessage(player);
                break;
        }

        return true;
    }

    private void setPosition(Player player, int pos) {
        Location location = player.getLocation();

        if (pos == 1) {
            pos1Map.put(player, location);
            sendMessage(player, "<green>Position 1 gesetzt: <yellow>" +
                    location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        } else {
            pos2Map.put(player, location);
            sendMessage(player, "<green>Position 2 gesetzt: <yellow>" +
                    location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        }
    }

    private void createRegion(Player player, String regionName) {
        Location pos1 = pos1Map.get(player);
        Location pos2 = pos2Map.get(player);

        if (pos1 == null || pos2 == null) {
            sendMessage(player, "<red>Du musst erst beide Positionen setzen! Verwende /jumppad pos1 und /jumppad pos2");
            return;
        }

        if (!pos1.getWorld().equals(pos2.getWorld())) {
            sendMessage(player, "<red>Beide Positionen müssen in der gleichen Welt sein!");
            return;
        }

        RegionManager regionManager = plugin.getRegionManager();

        if (regionManager.regionExists(regionName)) {
            sendMessage(player, "<red>Eine Region mit diesem Namen existiert bereits!");
            return;
        }

        if (regionManager.createRegion(regionName, pos1, pos2)) {
            String message = plugin.getConfigManager().getRegionCreatedMessage()
                    .replace("<region>", regionName);
            sendMessage(player, message);

            // Clear positions
            pos1Map.remove(player);
            pos2Map.remove(player);
        } else {
            sendMessage(player, "<red>Fehler beim Erstellen der Region!");
        }
    }

    private void deleteRegion(Player player, String regionName) {
        RegionManager regionManager = plugin.getRegionManager();

        if (!regionManager.regionExists(regionName)) {
            String message = plugin.getConfigManager().getRegionNotFoundMessage()
                    .replace("<region>", regionName);
            sendMessage(player, message);
            return;
        }

        if (regionManager.deleteRegion(regionName)) {
            String message = plugin.getConfigManager().getRegionDeletedMessage()
                    .replace("<region>", regionName);
            sendMessage(player, message);
        } else {
            sendMessage(player, "<red>Fehler beim Löschen der Region!");
        }
    }

    private void listRegions(Player player) {
        Map<String, JumpPadRegion> regions = plugin.getRegionManager().getAllRegions();

        if (regions.isEmpty()) {
            sendMessage(player, "<yellow>Keine Regionen gefunden.");
            return;
        }

        sendMessage(player, "<green>JumpPad Regionen (" + regions.size() + "):");
        for (String regionName : regions.keySet()) {
            sendMessage(player, "<gray>- <yellow>" + regionName);
        }
    }

    private void showRegionInfo(Player player, String regionName) {
        JumpPadRegion region = plugin.getRegionManager().getRegion(regionName);

        if (region == null) {
            String message = plugin.getConfigManager().getRegionNotFoundMessage()
                    .replace("<region>", regionName);
            sendMessage(player, message);
            return;
        }

        sendMessage(player, "<green>=== Region Info: " + regionName + " ===");
        sendMessage(player, "<gray>Welt: <yellow>" + region.getWorld().getName());
        sendMessage(player, "<gray>Position 1: <yellow>" +
                region.getPos1().getBlockX() + ", " +
                region.getPos1().getBlockY() + ", " +
                region.getPos1().getBlockZ());
        sendMessage(player, "<gray>Position 2: <yellow>" +
                region.getPos2().getBlockX() + ", " +
                region.getPos2().getBlockY() + ", " +
                region.getPos2().getBlockZ());
        sendMessage(player, "<gray>JumpPad Block: <yellow>" + region.getJumpPadBlock().name());
        sendMessage(player, "<gray>Sprung Stärke: <yellow>" + region.getJumpStrength());
        sendMessage(player, "<gray>Vorwärts Stärke: <yellow>" + region.getForwardStrength()); // Neue Info
        sendMessage(player, "<gray>Sound: <yellow>" + region.getSound().name());
    }

    private void setRegionBlock(Player player, String regionName, String blockName) {
        try {
            Material block = Material.valueOf(blockName.toUpperCase());

            if (plugin.getRegionManager().setRegionBlock(regionName, block)) {
                sendMessage(player, "<green>JumpPad Block für Region '" + regionName + "' wurde auf " + block.name() + " gesetzt!");
            } else {
                String message = plugin.getConfigManager().getRegionNotFoundMessage()
                        .replace("<region>", regionName);
                sendMessage(player, message);
            }
        } catch (IllegalArgumentException e) {
            sendMessage(player, "<red>Ungültiger Block: " + blockName);
        }
    }

    private void setRegionStrength(Player player, String regionName, String strengthString) {
        try {
            double strength = Double.parseDouble(strengthString);

            if (strength <= 0) {
                sendMessage(player, "<red>Die Stärke muss größer als 0 sein!");
                return;
            }

            if (plugin.getRegionManager().setRegionStrength(regionName, strength)) {
                sendMessage(player, "<green>Sprung Stärke für Region '" + regionName + "' wurde auf " + strength + " gesetzt!");
            } else {
                String message = plugin.getConfigManager().getRegionNotFoundMessage()
                        .replace("<region>", regionName);
                sendMessage(player, message);
            }
        } catch (NumberFormatException e) {
            sendMessage(player, "<red>Ungültige Zahl: " + strengthString);
        }
    }

    private void setRegionForwardStrength(Player player, String regionName, String strengthString) {
        try {
            double strength = Double.parseDouble(strengthString);

            if (strength < 0) {
                sendMessage(player, "<red>Die Vorwärts-Stärke kann nicht negativ sein!");
                return;
            }

            if (plugin.getRegionManager().setRegionForwardStrength(regionName, strength)) {
                sendMessage(player, "<green>Vorwärts-Stärke für Region '" + regionName + "' wurde auf " + strength + " gesetzt!");
                if (strength == 0) {
                    sendMessage(player, "<yellow>Hinweis: Bei Stärke 0 gibt es keinen Vorwärtsschub, nur vertikalen Sprung.");
                }
            } else {
                String message = plugin.getConfigManager().getRegionNotFoundMessage()
                        .replace("<region>", regionName);
                sendMessage(player, message);
            }
        } catch (NumberFormatException e) {
            sendMessage(player, "<red>Ungültige Zahl: " + strengthString);
        }
    }

    private void setRegionSound(Player player, String regionName, String soundName) {
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());

            if (plugin.getRegionManager().setRegionSound(regionName, sound)) {
                sendMessage(player, "<green>Sound für Region '" + regionName + "' wurde auf " + sound.name() + " gesetzt!");
            } else {
                String message = plugin.getConfigManager().getRegionNotFoundMessage()
                        .replace("<region>", regionName);
                sendMessage(player, message);
            }
        } catch (IllegalArgumentException e) {
            sendMessage(player, "<red>Ungültiger Sound: " + soundName);
        }
    }

    private void reloadPlugin(Player player) {
        plugin.getConfigManager().loadConfig();
        plugin.getRegionManager().loadRegions();
        sendMessage(player, "<green>Plugin wurde neu geladen!");
    }

    private void sendHelpMessage(Player player) {
        sendMessage(player, "<green>=== JumpPad Commands ===");
        sendMessage(player, "<yellow>/jumppad pos1 <gray>- Setze Position 1");
        sendMessage(player, "<yellow>/jumppad pos2 <gray>- Setze Position 2");
        sendMessage(player, "<yellow>/jumppad create <name> <gray>- Erstelle Region");
        sendMessage(player, "<yellow>/jumppad delete <name> <gray>- Lösche Region");
        sendMessage(player, "<yellow>/jumppad list <gray>- Liste alle Regionen");
        sendMessage(player, "<yellow>/jumppad info <name> <gray>- Zeige Region Info");
        sendMessage(player, "<yellow>/jumppad setblock <region> <block> <gray>- Setze JumpPad Block");
        sendMessage(player, "<yellow>/jumppad setstrength <region> <stärke> <gray>- Setze Sprung Stärke");
        sendMessage(player, "<yellow>/jumppad setforward <region> <stärke> <gray>- Setze Vorwärts Stärke");
        sendMessage(player, "<yellow>/jumppad setsound <region> <sound> <gray>- Setze Sound");
        sendMessage(player, "<yellow>/jumppad reload <gray>- Plugin neu laden");
    }

    private void sendMessage(Player player, String message) {
        Component component = plugin.getMiniMessage().deserialize(message);
        player.sendMessage(component);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String[] subCommands = {"pos1", "pos2", "create", "delete", "list", "info", "setblock", "setstrength", "setforward", "setsound", "reload"};
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("delete") || subCommand.equals("info") ||
                    subCommand.equals("setblock") || subCommand.equals("setstrength") ||
                    subCommand.equals("setforward") || subCommand.equals("setsound")) {

                for (String regionName : plugin.getRegionManager().getAllRegions().keySet()) {
                    if (regionName.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(regionName);
                    }
                }
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("setblock")) {
                for (Material material : Material.values()) {
                    if (material.isBlock() && material.name().toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(material.name());
                    }
                }
            } else if (subCommand.equals("setsound")) {
                for (Sound sound : Sound.values()) {
                    if (sound.name().toLowerCase().startsWith(args[2].toLowerCase())) {
                        completions.add(sound.name());
                    }
                }
            } else if (subCommand.equals("setstrength") || subCommand.equals("setforward")) {
                // Provide some example values
                completions.add("0.5");
                completions.add("1.0");
                completions.add("1.5");
                completions.add("2.0");
                completions.add("2.5");
                completions.add("3.0");
            }
        }

        return completions;
    }
}