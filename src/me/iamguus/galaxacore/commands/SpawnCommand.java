package me.iamguus.galaxacore.commands;

import me.iamguus.galaxacore.boosters.BoosterUtil;
import me.iamguus.galaxacore.util.ConfigUtil;
import me.iamguus.galaxacore.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Guus on 29-1-2016.
 */
public class SpawnCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't execute this command as a non-player!");
            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("spawn")) {
            Location teleportTo = LocationUtil.get().deserialize(ConfigUtil.get().getConfig().getString("spawn.location"));
            player.teleport(teleportTo);
            player.sendMessage(ChatColor.GREEN + "You have been teleported to the spawn!");
            return true;
        } else
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (player.isOp()) {
                Location newSpawn = player.getLocation();
                ConfigUtil.get().getConfig().set("spawn.location", LocationUtil.get().serialize(newSpawn));
                ConfigUtil.get().saveConfig();
                player.sendMessage(ChatColor.GREEN + "Successfully set the spawn location at your current location!");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have the permission to execute this command");
                return false;
            }
        }
        return false;
    }
}
