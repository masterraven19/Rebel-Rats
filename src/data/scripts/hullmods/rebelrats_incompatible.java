package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class rebelrats_incompatible extends BaseHullMod {
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

    }
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {

    }
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "INCOMPATIBLE";
        return null;
    }
}
