package dev.ryujix.commandwatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class CommandWatcher extends JavaPlugin implements Listener {

    HashSet<String> watchersList = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        if (!watchersList.isEmpty())
            watchersList.clear();
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commandwatcher")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("commandwatcher.use")) {
                        if (!watchersList.remove(sender.getName()))
                            watchersList.add(sender.getName());
                        sender.sendMessage(color("&d&l[!] &dNow " + (watchersList.contains(sender.getName()) ? "activating" : "deactivating") + " your CommandWatcher."));
                    } else {
                        sender.sendMessage(color("&7(&c!&7) &cSorry, you do not have permission to use this command."));
                    }
                    return true;
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (sender.hasPermission("commandwatcher.help")) {
                        sender.sendMessage(new String[] {
                                color("&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                                color("&d&lCommandWatcher Help &7| (Version: " + getDescription().getVersion() + ")"),
                                " ",
                                color("&d/commandwatcher &f- Main command to enable CommandWatcher."),
                                color("&d/commandwatcher <username> &f- Enable CommandWatcher for other users."),
                                color("&d/commandwatcher help &f- Views a list of available commands."),
                                color("&d/commandwatcher list &f- List of users using CommandWatcher."),
                                " ",
                                color("&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                        });
                    } else {
                        sender.sendMessage(color("&7(&c!&7) &cSorry, you do not have permission to use this command."));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("commandwatcher.list")) {
                        sender.sendMessage(color("&d&m-&f&l*&d&m-------------------------------------------------&f&l*&d&m-&r"));
                        sender.sendMessage(color("  &7&o(( List of all users currently using CommandWatcher! ))"));
                        sender.sendMessage(" ");
                        sender.sendMessage(color("  &d" + (watchersList.isEmpty() ? "&cThere are no users using CommandWatcher!" : watchersList.toString().replace("[", "").replace("]", "").replace(",", "&f,&d"))));
                        sender.sendMessage(" ");
                        sender.sendMessage(color("  &7&o(( There are currently &f&o" + watchersList.size() + " &7&oPlayers using CommandWatcher! ))"));
                        sender.sendMessage(color("&d&m-&f&l*&d&m-------------------------------------------------&f&l*&d&m-&r"));
                    } else {
                        sender.sendMessage(color("&7(&c!&7) &cSorry, you do not have permission to use this command."));
                    }
                    return true;
                } else {
                    if (sender.hasPermission("commandwatcher.use.other")) {
                        Player target = getServer().getPlayer(args[0]);
                        if (target != null) {
                            if (!watchersList.remove(target.getName()))
                                watchersList.add(target.getName());
                            sender.sendMessage(color("&d&l[!] &dNow " + (watchersList.contains(target.getName()) ? "activating" : "deactivating") + " " + target.getName() + "'s CommandWatcher."));
                            target.sendMessage(color("&d&l[!] &d" + sender.getName() + " has " + (watchersList.contains(target.getName()) ? "activated" : "deactivated") + " your CommandWatcher."));
                        } else {
                            sender.sendMessage(color("&7(&c!&7) &cSorry, that player is offline or doesn't exist."));
                        }
                    } else {
                        sender.sendMessage(color("&7(&c!&7) &cSorry, you do not have permission to use this command."));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        for (Player users : Bukkit.getOnlinePlayers()) {
            if (watchersList.contains(users.getName())) {
                if (player.getName() != users.getName()) {
                    users.sendMessage(color("&d&l[&fCommandWatcher&d&l] &d" + player.getName() + " &f| " + event.getMessage()));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (watchersList.contains(event.getPlayer().getName()))
            watchersList.remove(event.getPlayer().getName());
    }

}
