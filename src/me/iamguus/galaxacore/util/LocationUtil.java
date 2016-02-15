package me.iamguus.galaxacore.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Guus on 29-1-2016.
 */
public class LocationUtil {

    private static LocationUtil instance;

    public String serialize(Location loc) {
        if (loc == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("w:" + loc.getWorld().getName() + " ");
        sb.append("x:" + loc.getX() + " ");
        sb.append("y:" + loc.getY() + " ");
        sb.append("z:" + loc.getZ() + " ");
        sb.append("yaw:" + loc.getYaw() + " ");
        sb.append("pitch:" + loc.getPitch());

        return sb.toString();
    }

    public Location deserialize(String s) {
        if (s == null) {
            return null;
        }
        String[] parts = s.split(" ");

        String wS = parts[0].split(":")[1];
        String xS = parts[1].split(":")[1];
        String yS = parts[2].split(":")[1];
        String zS = parts[3].split(":")[1];
        String yawS = parts[4].split(":")[1];
        String pitchS = parts[5].split(":")[1];

        World w = Bukkit.getWorld(wS);
        double x = Double.parseDouble(xS);
        double y = Double.parseDouble(yS);
        double z = Double.parseDouble(zS);
        float yaw = Float.parseFloat(yawS);
        float pitch = Float.parseFloat(pitchS);

        Location loc = new Location(w, x, y, z, yaw, pitch);
        return loc;
    }

    public static LocationUtil get() {
        if (instance == null) {
            instance = new LocationUtil();
        }

        return instance;
    }
}
