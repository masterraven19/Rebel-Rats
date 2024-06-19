package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class rebelrats_lockjaw extends BaseShipSystemScript {
    private static final float ballisticDamageBonus = 50;
    private static final float ballisticRangeBuff = 30;
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        float ballisticDamage = ballisticDamageBonus * effectLevel;
        float ballisticRange = ballisticRangeBuff * effectLevel;
        stats.getBallisticWeaponDamageMult().modifyPercent(id,ballisticDamage);
        stats.getBallisticWeaponRangeBonus().modifyPercent(id,ballisticRange);
    }
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getBallisticWeaponRangeBonus().unmodify(id);
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        float bonusPercent = ballisticDamageBonus * effectLevel;
        float rangeBuff = ballisticRangeBuff * effectLevel;
        if (index == 0) {
            return new StatusData("+" + (int) bonusPercent + "% ballistic weapon damage" , false);
        }
        if (index == 1){
            return new StatusData("+" + (int) rangeBuff + "% ballistic weapon range" , false);
        }
        return null;
    }
}
