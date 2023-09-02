package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class rebelrats_JerboaSystemStats extends BaseShipSystemScript {
    //mostly works, however still not bug tested thoroughly
    //also no description yet (StatusData)

    private static float weaponTurnRateDebuff = -200;
    private static float range = 1500;
    private static boolean hasActivated = false;
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = null;
        if (stats.getEntity() instanceof ShipAPI){
            ship = (ShipAPI) stats.getEntity();
        }else{
            return;
        }
        ShipAPI target = findTarget(ship);

        if (target != null) {
            if (state == State.IN){
                target.getMutableStats().getWeaponTurnRateBonus().modifyFlat(id,weaponTurnRateDebuff);
                target.getMutableStats().getAcceleration().modifyPercent(id,-200 * effectLevel);
                target.getMutableStats().getDeceleration().modifyPercent(id,-200 * effectLevel);
                target.getMutableStats().getTurnAcceleration().modifyPercent(id,-200 * effectLevel);
                target.getMutableStats().getMaxTurnRate().modifyPercent(id, 0 * effectLevel);
                target.getMutableStats().getMaxSpeed().modifyFlat(id, 0 * effectLevel);

                if (!hasActivated){
                    target.getFluxTracker().showOverloadFloatyIfNeeded("EWS Blasted!",new Color(255,55,55),4f,true);
                    hasActivated = !hasActivated;
                }
            }else
                if(state == State.ACTIVE){
                    for (ShipEngineControllerAPI.ShipEngineAPI e : target.getEngineController().getShipEngines()){
                        target.getEngineController().setFlameLevel(e.getEngineSlot(),-0f);
                    }
                }
            if (state == State.OUT){
                target.getMutableStats().getWeaponTurnRateBonus().unmodify(id);
                target.getMutableStats().getAcceleration().unmodify(id);
                target.getMutableStats().getDeceleration().unmodify(id);
                target.getMutableStats().getTurnAcceleration().unmodify(id);
                target.getMutableStats().getMaxTurnRate().unmodify(id);
                target.getMutableStats().getMaxSpeed().unmodify(id);

                hasActivated = !hasActivated;
            }
        }
    }
    public void unapply(MutableShipStatsAPI stats, String id) {

    }
    protected ShipAPI findTarget(ShipAPI ship){
        float range = getMaxRange(ship);
        boolean player = ship == Global.getCombatEngine().getPlayerShip();
        ShipAPI target = ship.getShipTarget();

        if (ship.getShipAI() != null && ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.TARGET_FOR_SHIP_SYSTEM)){
            target = (ShipAPI) ship.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.TARGET_FOR_SHIP_SYSTEM);
        }

        if (target != null){
            float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
            float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
            if (dist > range + radSum) target = null;
        }else{
            if (target == null || target.getOwner() == ship.getOwner()) {
                if (player){
                    target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), ShipAPI.HullSize.FRIGATE,range,true);
                }else {
                    Object test = ship.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.MANEUVER_TARGET);
                    if (test instanceof ShipAPI) {
                        target = (ShipAPI) test;
                        float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
                        float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
                        if (dist > range + radSum) target = null;
                    }
                }
            }
            if (target == null){
                target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), ShipAPI.HullSize.FIGHTER, range, true);
            }
        }

        return target;
    }
    private static float getMaxRange(ShipAPI ship){
        return ship.getMutableStats().getSystemRangeBonus().computeEffective(range);
    }
    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (effectLevel > 0){
            if (index == 0){
                return new StatusData("Disabling enemy ship weapon rotation and engines.",false);
            }
        }
        return null;
    }
    public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
        if (system.isOutOfAmmo()) return null;

        ShipAPI target = findTarget(ship);
        if (target != null && target != ship) {
            return "SQUEEK!!!";
        }
        if ((target == null) && ship.getShipTarget() != null) {
            return "squeek...";
        }
        return "squeek?";
    }
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        ShipAPI target = findTarget(ship);
        return target != null && target != ship;
    }
}
