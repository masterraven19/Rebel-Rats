package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class rebelrats_lockjaw extends BaseShipSystemScript {
    private static final float ballisticDamageBonus = 50;
    private static final float ballisticRangeBuff = 30;
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        float ballisticBonus = ballisticDamageBonus * effectLevel;
        float ballisticDebuff = ballisticRangeBuff * effectLevel;
        stats.getBallisticWeaponDamageMult().modifyPercent(id,ballisticBonus);
        stats.getBallisticWeaponRangeBonus().modifyPercent(id,ballisticDebuff);
    }
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getBallisticWeaponRangeBonus().unmodify(id);
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        float bonusPercent = ballisticDamageBonus * effectLevel;
        float rofDebuff = ballisticRangeBuff * effectLevel;
        if (index == 0) {
            return new StatusData("+" + (int) bonusPercent + "% ballistic weapon damage" , false);
        }
        if (index == 1){
            return new StatusData("+" + (int) rofDebuff + "% ballistic RoF" , false);
        }
        return null;
    }
}
