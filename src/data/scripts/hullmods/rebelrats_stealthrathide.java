package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

import java.awt.*;

public class rebelrats_stealthrathide extends BaseHullMod {
    public static float profiledetection_bonus = -70;
    public static String design;
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSensorProfile().modifyPercent(id,profiledetection_bonus);
    }
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) profiledetection_bonus + "%";
        return null;
    }
    public Color getBorderColor() {
        return Global.getSettings().getDesignTypeColor("Arms & Armor");
    }
    public Color getNameColor() {
        return Global.getSettings().getDesignTypeColor("Arms & Armor");
    }
}
