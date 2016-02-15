package me.iamguus.galaxacore;

import me.iamguus.galaxacore.boosters.BoosterCommand;
import me.iamguus.galaxacore.boosters.BoosterListener;
import me.iamguus.galaxacore.boosters.BoosterUtil;
import me.iamguus.galaxacore.commands.SpawnCommand;
import me.iamguus.galaxacore.listeners.CoreListener;
import me.iamguus.galaxacore.util.ConfigUtil;
import me.iamguus.galaxacore.util.MySQLHandler;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Guus on 29-1-2016.
 */
public class Core extends JavaPlugin {

    private static Plugin p;
    public static Permission perms = null;
    public static Chat chat = null;

    public void onEnable() {
        this.p = this;

        ConfigUtil.get().setup(this.p);

        MySQLHandler.get().setup();

        Bukkit.getPluginManager().registerEvents(new CoreListener(), this);
        Bukkit.getPluginManager().registerEvents(new BoosterListener(), this);

        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SpawnCommand());
        getCommand("booster").setExecutor(new BoosterCommand());

        setupChat();
        setupPermissions();

        new BukkitRunnable() {
            public void run() {
                BoosterUtil.get().checkBoosters();
            }
        }.runTaskTimer(this, 0L, 1200L);
    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
