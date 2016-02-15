package me.iamguus.galaxacore.boosters;

import org.bukkit.Material;

/**
 * Created by Guus on 12-2-2016.
 */
public enum BoosterType {

    SKYWARS(1, "SkyWars", Material.BOW);

    private int id;
    private String name;
    private Material mat;

    private BoosterType(int id, String name, Material mat) {
        this.id = id;
        this.name = name;
        this.mat = mat;
    }

    public int getId() { return id; }

    public String getName() { return this.name; }

    public Material getMat() { return mat; }

    public static BoosterType getByItem(Material mat) {
        for (BoosterType type : values()) {
            if (type.getMat() == mat) {
                return type;
            }
        }

        return null;
    }
}
