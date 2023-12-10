package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.HullDamageAboutToBeTakenListener;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_cc extends BaseHullMod {
    //repair hull when below 50% and no dmg for 10 seconds
    //acceleration(no top speed) by 20% and turn accel by 30% when below 30% hull
    private static String modId;
    private static final float accelBuff = 20f;
    private static final float turnBuff = 50f;
    private static final float maxTurnBuff = 30f;
    private static final float frigateRate = 0.05f;
    private static final float destroyerRate = 0.05f;
    private static final float cruiserRate = 0.05f;
    private static final float activationPoint = 50f;//all percentages, except caps, rate and cooldown
    private static final float turnPoint = 30f;
    private static final int frigateCap = 500;
    private static final int destroyerCap = 1000;
    private static final int cruiserCap = 2000;
    private static final int capitalCap = 3000;
    private static final float rate = 0.1f;
    private static final float cooldown = 10f;
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        modId = id;
    }
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {ship.addListener(new HullDamageListener(ship));}
    public static class HullDamageListener implements AdvanceableListener, HullDamageAboutToBeTakenListener{
        public ShipAPI ship;
        public boolean hasHit = false;
        public boolean repairing = false;
        public float repairRate;
        public int repairCap;
        public float elapsed;
        public float elapsed2;
        public float hullRepaired = 0;
        public HullDamageListener(ShipAPI ship){
            this.ship = ship;
            switch(ship.getHullSize()){
                case DESTROYER:
                    repairCap = destroyerCap;
                    repairRate = destroyerRate;
                    break;
                case CRUISER:
                    repairCap = cruiserCap;
                    repairRate = cruiserRate;
                    break;
                case CAPITAL_SHIP:
                    repairCap = capitalCap;
                    repairRate = cruiserRate;
                    break;
                default:
                    repairCap = frigateCap;
                    repairRate = frigateRate;
                    break;
            }
        }

        public void advance(float amount){
            if (Global.getCombatEngine().isPaused()) return;
            if (!ship.isAlive()) return;
            if (ship.getHitpoints() < ship.getHullSpec().getHitpoints() * activationPoint / 100f){repairing = true;}
            if (!repairing) return;

            if (!hasHit){
                elapsed += amount;
                elapsed2 += amount;
            }else{
                elapsed2 = 0;
            }
            if (!hasHit && elapsed2 > cooldown && elapsed > rate && hullRepaired < repairCap){
                elapsed -= rate;
                ship.setHitpoints(ship.getHitpoints() + (ship.getHullSpec().getHitpoints() * repairRate / 100f));
                hullRepaired += (ship.getHullSpec().getHitpoints() * repairRate / 100f);
            }
            if (ship.getHitpoints() < ship.getHullSpec().getHitpoints() * turnPoint / 100f){
                ship.getMutableStats().getAcceleration().modifyPercent(modId,accelBuff);
                ship.getMutableStats().getDeceleration().modifyPercent(modId,accelBuff / 2f);
                ship.getMutableStats().getTurnAcceleration().modifyPercent(modId,turnBuff);
                ship.getMutableStats().getMaxTurnRate().modifyPercent(modId,maxTurnBuff);
            }
        }

        public boolean notifyAboutToTakeHullDamage(Object param, ShipAPI ship, Vector2f point, float damageAmount) {
            if (damageAmount > 0){
                hasHit = false;
            }else{
                hasHit = true;
            }
            return false;
        }
    }
    public void advanceInCombat(ShipAPI ship, float amount) {
    }
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + activationPoint + "%";
        if (index == 1) return "" + frigateCap;
        if (index == 2) return "" + destroyerCap;
        if (index == 3) return "" + cruiserCap;
        if (index == 4) return "" + capitalCap;
        if (index == 5) return "" + turnPoint + "%";
        if (index == 6) return "" + accelBuff + "%";
        if (index == 7) return "" + turnBuff + "%";
        return null;
    }
    public Color getBorderColor() {
        return Global.getSettings().getDesignTypeColor("Rebel Rats");
    }
    public Color getNameColor() {
        return Global.getSettings().getDesignTypeColor("Rebel Rats");
    }
}
