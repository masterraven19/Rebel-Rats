package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class rebelrats_BastionShieldSystem extends BaseHullMod {
    private final float shieldRadiusOffset = -0.1F;
    private final float shieldArcMax = 150;
    private final float shieldDebuff = 0.2F;
    private final float sModShieldBuff = 0F;
    private final float hardenedShieldBuff = 0.1F;
    private String modId = null;
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        modId = id;
    }
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {}

    public void advanceInCombat(ShipAPI ship, float amount) {
        if (Global.getCombatEngine().isPaused()) return;

        float frontAngle = ship.getFacing();
        float targetArc = 0;
        float shieldRatio = 0;
        float efficiencyRatio = 0;

        float shieldAngle = ship.getShield().getFacing();
        float shieldRadius = ship.getHullSpec().getShieldSpec().getRadius();
        float shieldArc = ship.getHullSpec().getShieldSpec().getArc();
        float arcDifference = shieldArcMax - shieldArc;

        //this basically makes the shield larger and closer to the ship when its facing the sides of the ship.
        float anglediff = (frontAngle - shieldAngle + 180 + 360) % 360 - 180;

        if (anglediff >= 0 && anglediff <= 90){
            float diffRatio = anglediff / 90;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (anglediff <= 0 && anglediff >= -90){
            float diffRatio = (anglediff / -90);

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (anglediff >= 90 && anglediff <= 180){
            float diffRatio = ((90 - anglediff) / 90) + 1;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (anglediff <= -90 && anglediff >= -180){
            float diffRatio = ((90 + anglediff) / 90) + 1;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }

        float shieldRadiusTarget = (1 + shieldRatio) * shieldRadius;
        float efficiency = efficiencyRatio;

        boolean sMod = isSMod(ship);
        if (sMod) {
            efficiency = sModShieldBuff;
        }else{
            for(String mod: ship.getVariant().getHullMods()){
                if (mod.equals(modId)) continue;
                if (mod.equals("hardenedshieldemitter")){
                    if (efficiency > 1){
                        efficiency = efficiency - hardenedShieldBuff;
                    }
                }
            }
        }

        ship.getMutableStats().getShieldDamageTakenMult().modifyMult(modId, 1 + efficiency);
        ship.getShield().setRadius(shieldRadiusTarget, "graphics/fx/ratshieldouter.png", "graphics/fx/ratshieldouterring.png");
        ship.getShield().setArc(targetArc);
    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) shieldArcMax;
        if (index == 1) return "" + shieldDebuff;
        return null;
    }
    public String getSModDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + 0;
        return null;
    }
}
