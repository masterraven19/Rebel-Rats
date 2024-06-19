package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class rebelrats_ratfield extends BaseHullMod {
    private float dmgBonus = 50;
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
        stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
        stats.getDamageToMissiles().modifyPercent(id, dmgBonus);
    }
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) dmgBonus + "%";
        return null;
    }
}
