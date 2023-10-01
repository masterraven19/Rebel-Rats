package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class rebelrats_BastionShieldSystem extends BaseHullMod {
    private static float shieldRadiusOffset = -0.15F;
    private static float shieldArcMax = 150;
    private static float shieldDebuff = 0.2F;
    private static String modId = null;
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

    }
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        modId = id;
    }
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
        float angleDifference = frontAngle - shieldAngle;
        if (angleDifference >= 0 && angleDifference <= 90){
            float diffRatio = angleDifference * 0.01F;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (angleDifference >= 90 && angleDifference <= 180){
            float diffRatio = 1 - ((angleDifference - 90) * 0.01F);

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (angleDifference >= 180 && angleDifference <= 270){
            float diffRatio = (angleDifference - 180) * 0.01F;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (angleDifference >= 270 && angleDifference <= 360){
            float diffRatio = 1 - ((angleDifference - 270) * 0.01F);

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (angleDifference <= 0 && angleDifference >= -90){
            float diffRatio = angleDifference * -0.01F;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if (angleDifference <= -90 && angleDifference >= -180){
            float diffRatio = 1 - ((angleDifference + 90) * -0.01F);

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if(angleDifference <= -180 && angleDifference >= -270){
            float diffRatio = (angleDifference + 180) * -0.01F;

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }
        if(angleDifference <= -270 && angleDifference >= -360){
            float diffRatio = 1 - ((angleDifference + 270) * -0.01F);

            targetArc = (diffRatio * arcDifference) + shieldArc;

            shieldRatio = diffRatio * shieldRadiusOffset;

            efficiencyRatio = diffRatio * shieldDebuff;
        }


        float shieldRadiusTarget = (1 + shieldRatio) * shieldRadius;

        ship.getMutableStats().getShieldDamageTakenMult().modifyMult(modId, 1 + efficiencyRatio);
        ship.getShield().setRadius(shieldRadiusTarget, "graphics/fx/ratshieldouter.png", "graphics/fx/shields256ring.png");
        ship.getShield().setArc(targetArc);

    }

    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) shieldArcMax;
        if (index == 1) return "" + (int) shieldDebuff;
        return null;
    }
}
