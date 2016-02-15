package me.iamguus.galaxacore.boosters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Guus on 12-2-2016.
 */
public class BoosterInventory {

    private static BoosterInventory instance;

    public Inventory getBoosterInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, player.getName() + "'s boosters");

        ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta blackMeta = blackGlass.getItemMeta();
        blackMeta.setDisplayName(" ");
        blackGlass.setDurability((short) 7);
        blackGlass.setItemMeta(blackMeta);

        ItemStack whiteGlass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta whiteMeta = whiteGlass.getItemMeta();
        whiteMeta.setDisplayName(" ");
        whiteGlass.setItemMeta(whiteMeta);

        inv.setItem(0, blackGlass);
        inv.setItem(2, blackGlass);
        inv.setItem(4, blackGlass);
        inv.setItem(6, blackGlass);
        inv.setItem(8, blackGlass);

        inv.setItem(1, whiteGlass);
        inv.setItem(3, whiteGlass);
        inv.setItem(5, whiteGlass);
        inv.setItem(7, whiteGlass);

        Map<BoosterType, Integer> boosters = BoosterUtil.get().getBoosters(player.getUniqueId());

        if (!boosters.isEmpty()) {
            for (Map.Entry<BoosterType, Integer> entry : boosters.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    inv.addItem(BoosterUtil.get().getItem(entry.getKey()));
                }
            }
        }

        boolean addItem = false;

        for (int i = 9; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null) {
                addItem = true;
            }
        }

        if (!addItem) {
            ItemStack commandBlock = new ItemStack(Material.COMMAND);
            ItemMeta im = commandBlock.getItemMeta();
            im.setDisplayName(ChatColor.RED + "No boosters found!");
            im.setLore(Arrays.asList("", ChatColor.GRAY + "It looks like you haven't bought", ChatColor.GRAY + "any boosters yet!", "", ChatColor.GRAY + "Click here to go to the store!"));
            commandBlock.setItemMeta(im);
            inv.setItem(31, commandBlock);
        }

        return inv;
    }

    public Inventory getAgree(BoosterType type) {
        Inventory inv = Bukkit.createInventory(null, 45, "Are you sure?");

        ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta blackMeta = blackGlass.getItemMeta();
        blackMeta.setDisplayName(" ");
        blackGlass.setDurability((short) 7);
        blackGlass.setItemMeta(blackMeta);

        ItemStack whiteGlass = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta whiteMeta = whiteGlass.getItemMeta();
        whiteMeta.setDisplayName(" ");
        whiteGlass.setItemMeta(whiteMeta);

        inv.setItem(0, blackGlass);
        inv.setItem(2, blackGlass);
        inv.setItem(4, BoosterUtil.get().getItem(type));
        inv.setItem(6, blackGlass);
        inv.setItem(8, blackGlass);

        inv.setItem(1, whiteGlass);
        inv.setItem(3, whiteGlass);
        inv.setItem(5, whiteGlass);
        inv.setItem(7, whiteGlass);

        ItemStack green = new ItemStack(Material.WOOL);
        green.setDurability((short) 13);
        ItemMeta greenMeta = green.getItemMeta();
        greenMeta.setDisplayName(ChatColor.GREEN + "Yes!");
        greenMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This will activate your " + type.getName() + " booster.", "", ChatColor.GRAY + "Are you sure?"));
        green.setItemMeta(greenMeta);

        ItemStack red = new ItemStack(Material.WOOL);
        red.setDurability((short) 14);
        ItemMeta redMeta = red.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "No!");
        redMeta.setLore(Arrays.asList("", ChatColor.GRAY + "This won't activate your " + type.getName() + "booster.", "", ChatColor.GRAY + "Are you sure?"));
        red.setItemMeta(redMeta);

        inv.setItem(20, green);
        inv.setItem(24, red);

        return inv;
    }

    private String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }

    public static BoosterInventory get() {
        if (instance == null) {
            instance = new BoosterInventory();
        }

        return instance;
    }
}
