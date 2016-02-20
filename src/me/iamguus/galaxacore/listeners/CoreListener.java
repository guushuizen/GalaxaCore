package me.iamguus.galaxacore.listeners;

import me.iamguus.galaxacore.Core;
import me.iamguus.galaxacore.util.ConfigUtil;
import me.iamguus.galaxacore.util.EconomyUtil;
import me.iamguus.galaxacore.util.LocationUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Guus on 29-1-2016.
 */
public class CoreListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (player.hasPermission(ConfigUtil.get().getConfig().getString("joinleave.permission"))) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
            } else {
                event.setResult(PlayerLoginEvent.Result.KICK_FULL);
                event.setKickMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleave.kick-message")));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String prefix = ChatColor.translateAlternateColorCodes('&', Core.chat.getGroupPrefix(Bukkit.getWorlds().get(0), Core.chat.getPrimaryGroup(player)));
        player.setDisplayName(prefix + player.getName());
        if (!player.hasPermission(ConfigUtil.get().getConfig().getString("joinleavemessage.permission"))) {
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleavemessage.join-message").replaceAll("%player%", player.getDisplayName())));
        }

        if (ConfigUtil.get().getConfig().getBoolean("tp-to-spawn")) {
            Location teleportTo = LocationUtil.get().deserialize(ConfigUtil.get().getConfig().getString("spawn.location"));
            event.getPlayer().teleport(teleportTo);
        }

        EconomyUtil.get().createRowForPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(ConfigUtil.get().getConfig().getString("joinleavemessage.permission"))) {
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleavemessage.quit-message").replaceAll("%player%", player.getDisplayName())));
        }
    }

    private HashMap<UUID, Long> delay = new HashMap<UUID, Long>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String prefix = ChatColor.translateAlternateColorCodes('&', Core.chat.getGroupPrefix(Bukkit.getWorlds().get(0), Core.chat.getPrimaryGroup(player)));
        player.setDisplayName(prefix + player.getName());
        event.setFormat(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("chat-format")).replaceAll("%DISPLAYNAME%", "%s").replaceAll("%MESSAGE%", "%s"));

        if (!player.hasPermission(ConfigUtil.get().getConfig().getString("chat-delay.permission"))) {
            if (!delay.containsKey(player.getUniqueId())) {
                event.setCancelled(false);
                delay.put(player.getUniqueId(), System.currentTimeMillis());
            } else {
                long timestamp = delay.get(player.getUniqueId());
                long currentTime = System.currentTimeMillis();
                if (currentTime - timestamp  > ConfigUtil.get().getConfig().getInt("chat-delay.delay") * 1000) {
                    event.setCancelled(false);
                    delay.remove(player.getUniqueId());
                    delay.put(player.getUniqueId(), System.currentTimeMillis());
                } else {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("chat-delay.message")));
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (ConfigUtil.get().getConfig().getBoolean("tp-to-spawn")) {
            if (event.getTo().getY() <= 0) {
                Location teleportTo = LocationUtil.get().deserialize(ConfigUtil.get().getConfig().getString("spawn.location"));
                event.getPlayer().teleport(teleportTo);
            }
        }
    }
}
