package me.iamguus.galaxacore.boosters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Guus on 12-2-2016.
 */
public class BoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("booster")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You can't execute this command as a non-player!");
                    return false;
                }

                Player player = (Player) sender;
                player.openInventory(BoosterInventory.get().getBoosterInventory(player));
                return true;
            } else
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (sender.isOp()) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (offlinePlayer != null) {
                            BoosterType type = BoosterType.valueOf(args[2]);
                            if (type != null) {
                                BoosterUtil.get().addBooster(offlinePlayer.getUniqueId(), type, 1);
                            } else {
                                sender.sendMessage("The booster " + args[2] + " was not found!");
                                return false;
                            }
                        } else {
                            sender.sendMessage("Player " + args[1] + " was not found!");
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
