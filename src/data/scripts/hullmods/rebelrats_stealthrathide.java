package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class rebelrats_stealthrathide extends BaseHullMod {
    public static float profiledetection_bonus = -70;
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getSensorProfile().modifyPercent(id,profiledetection_bonus);
    }
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) profiledetection_bonus + "%";
        return null;
    }
}
