package me.iamguus.galaxacore.boosters;

import me.iamguus.galaxacore.util.MySQLHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Guus on 10-2-2016.
 */
public class BoosterUtil {

    private static BoosterUtil instance;

    public void createBooster(BoosterType type, Player player) {
        if (!getActive(type)) {
            try {
                PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE boosters SET active = ?, date = ? WHERE game = ?");
                ps.setBoolean(1, true);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                ps.setString(2, sdf.format(calendar.getTime()));

                ps.setInt(3, type.getId());

                ps.executeUpdate();

                player.sendMessage(ChatColor.GREEN + "You have successfully activated your " + type.getName() + " booster!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            player.sendMessage(ChatColor.RED + "Could not enable your booster since another booster is already running!");
        }
    }

    public void checkBoosters() {
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT * FROM boosters");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("active") == 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                    Date currentDate = Calendar.getInstance().getTime();
                    Date dbDate = sdf.parse(rs.getString("date"));

                    if (sdf.format(currentDate).equals(sdf.format(dbDate))) {
                        PreparedStatement ps2 = MySQLHandler.get().getConnection().prepareStatement("UPDATE boosters SET active = ?, date = ? WHERE game = ?");
                        ps2.setBoolean(1, false);

                        ps2.setString(2, null);

                        ps2.setInt(3, rs.getInt("game"));

                        ps2.executeUpdate();

                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public boolean getActive(BoosterType type) {
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT active FROM boosters WHERE game = ?");

            ps.setInt(1, type.getId());

            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getInt("active") != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Map<BoosterType, Integer> getBoosters(UUID uuid) {
        Map<BoosterType, Integer> boosters = new HashMap<BoosterType, Integer>();

        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT boosters FROM players WHERE uuid = ?");

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();
            rs.next();

            String boosterString = rs.getString("boosters");

            for (String loop : boosterString.split(";")) {
                if (loop.length() > 1) {
                    String gameS = loop.split(":")[0];
                    String amountS = loop.split(":")[1];

                    BoosterType boosterType = BoosterType.valueOf(gameS);
                    int amount = Integer.parseInt(amountS.trim());

                    boosters.put(boosterType, amount);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return boosters;
    }

    public void setBoosters(UUID uuid, Map<BoosterType, Integer> boosters) {
        String boosterString = "";

        for (Map.Entry<BoosterType, Integer> entry : boosters.entrySet()) {
            boosterString += entry.getKey().name() + ":" + entry.getValue() + ";";
        }

        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET boosters = ? WHERE uuid = ?");

            ps.setString(1, boosterString.substring(0, boosterString.length() - 1));
            ps.setString(2, uuid.toString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addBooster(UUID uuid, BoosterType type) {
        addBooster(uuid, type, 1);
    }

    public void addBooster(UUID uuid, BoosterType type, int amount) {
        Map<BoosterType, Integer> boosters = getBoosters(uuid);

        int currentAmount = (boosters.containsKey(type)) ? boosters.get(type) : 0;
        boosters.remove(type);

        boosters.put(type, currentAmount + amount);

        setBoosters(uuid, boosters);
    }

    public void removeBooster(UUID uuid, BoosterType type) {
        removeBooster(uuid, type, 1);
    }

    public void removeBooster(UUID uuid, BoosterType type, int amount) {
        Map<BoosterType, Integer> boosters = getBoosters(uuid);

        int currentAmount = (boosters.containsKey(type)) ? boosters.get(type) : 0;
        boosters.remove(type);

        boosters.put(type, currentAmount - amount);


        setBoosters(uuid, boosters);
    }

    public ItemStack getItem(BoosterType type) {
        if (type == BoosterType.SKYWARS) {
            ItemStack item = new ItemStack(type.getMat());
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(ChatColor.BLUE + "SkyWars Booster");
            im.setLore(Arrays.asList("", ChatColor.GRAY + "Triples the amount of coins you get", ChatColor.GRAY + "when the game has ended."));
            item.setItemMeta(im);
            return item;
        }

        return null;
    }

    public static BoosterUtil get() {
        if (instance == null) {
            instance = new BoosterUtil();
        }

        return instance;
    }
}