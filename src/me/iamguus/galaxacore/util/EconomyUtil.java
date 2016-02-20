package me.iamguus.galaxacore.util;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Guus on 3-2-2016.
 */
public class EconomyUtil {

    private static EconomyUtil instance;

    public void createRowForPlayer(Player player) {
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("INSERT IGNORE INTO players (uuid, orbs, coins, boosters) VALUES(?, ?, ?, ?);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setInt(2, 0);
            ps.setString(3, "");
            ps.setString(4, "");

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<GameType, Integer> getCoins(UUID uuid) {
        HashMap<GameType, Integer> coinsMap = new HashMap<GameType, Integer>();
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT coins FROM players WHERE uuid = ?;");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            rs.next();

            String coinString = rs.getString("coins");

            if (coinString.trim().length() > 1) {
                for (String sTemp : coinString.trim().split(";")) {
                    String[] loop = sTemp.split(":");

                    String gameS = loop[0];
                    String intS = loop[1];

                    GameType gt = GameType.valueOf(gameS.toUpperCase());
                    int amount = Integer.parseInt(intS);

                    coinsMap.put(gt, amount);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return coinsMap;
    }

    public int getCoins(UUID uuid, GameType type) {
        return (getCoins(uuid).get(type) != null) ? getCoins(uuid).get(type) : 0;
    }

    public void setCoins(UUID uuid, GameType type, int coins) {
        Map<GameType, Integer> coinsMap = getCoins(uuid);

        coinsMap.remove(type);
        coinsMap.put(type, coins);

        updateCoins(uuid, coinsMap);
    }

    public void updateCoins(UUID uuid, Map<GameType, Integer> coinsMap) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<GameType, Integer> entrySet : coinsMap.entrySet()) {
            sb.append(entrySet.getKey().name().toUpperCase() + ":" + entrySet.getValue());
        }

        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET coins = ? WHERE uuid = ?");

            ps.setString(1, sb.toString());
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getOrbs(UUID uuid) {
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT orbs FROM players WHERE uuid = ?;");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            rs.next();

            int coins = rs.getInt("orbs");

            ps.close();

            return coins;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setOrbs(UUID uuid, int coins) {
        try {
            PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET orbs = ? WHERE uuid = ?;");

            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static EconomyUtil get() {
        if (instance == null) {
            instance = new EconomyUtil();
        }

        return instance;
    }
}
