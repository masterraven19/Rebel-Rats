package data.scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class rebelrats_lockjaw extends BaseShipSystemScript {
    public static final float ballisticDamageBonus = 60;
    public static final float ballisticROFDebuff = -30;
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        float ballisticBonus = ballisticDamageBonus * effectLevel;
        float ballisticDebuff = ballisticROFDebuff * effectLevel;
        stats.getBallisticWeaponDamageMult().modifyPercent(id,ballisticBonus);
        stats.getBallisticRoFMult().modifyPercent(id,ballisticDebuff);
    }
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getBallisticWeaponDamageMult().unmodify(id);
        stats.getBallisticRoFMult().unmodify(id);
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        float bonusPercent = ballisticDamageBonus * effectLevel;
        float rofDebuff = ballisticROFDebuff * effectLevel;
        if (index == 0) {
            return new StatusData("+" + (int) bonusPercent + "% ballistic weapon damage" , false);
        }
        if (index == 1){
            return new StatusData("-" + (int) rofDebuff + "% ballistic RoF" , true);
        }
        return null;
    }
}