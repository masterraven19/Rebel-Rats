package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import data.scripts.combat.rebelrats_JerboaTarget;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class rebelrats_JerboaSystemStats extends BaseShipSystemScript {
    //mostly works, however still not bug tested thoroughly
    //AHHH the rats curse
    private rebelrats_JerboaTarget effect = null;
    private Color jitterColor = new Color(60,213,33,255);
    private static float range = 1500;
    private boolean hasActivated = false;
    private boolean addedeffect = false;
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        ShipAPI ship = null;

        if (stats.getEntity() instanceof ShipAPI){
            ship = (ShipAPI) stats.getEntity();
        }else{
            return;
        }

        ShipAPI target = findTarget(ship);

        if(effect == null && ship.isAlive()) {
            effect = new rebelrats_JerboaTarget();
        }

        if (target != null && effect != null) {

            if (state == State.IN){
                if (!hasActivated){
                    target.getFluxTracker().showOverloadFloatyIfNeeded("EWS Blasted!",new Color(255,55,55),4f,true);
                    hasActivated = true;
                }
            }

            if(state == State.ACTIVE){
                if (!addedeffect){
                    Global.getCombatEngine().addPlugin(effect);
                    //Global.getCombatEngine().addFloatingText(stats.getEntity().getLocation(),"EFFECT READY",10,Color.WHITE,stats.getEntity(),1,1);
                    addedeffect = true;
                }
                effect.update(target,effectLevel,id);
                //Global.getCombatEngine().addFloatingText(stats.getEntity().getLocation(),""+effectLevel,10,Color.WHITE,stats.getEntity(),1,1);
                for (ShipEngineControllerAPI.ShipEngineAPI e : target.getEngineController().getShipEngines()){
                    target.getEngineController().setFlameLevel(e.getEngineSlot(),-0f);
                }
            }

            if (state == State.OUT){
                hasActivated = false;
                addedeffect = false;
                //Global.getCombatEngine().addFloatingText(stats.getEntity().getLocation(),""+addedeffect,10,Color.WHITE,stats.getEntity(),1,1);
                effect.update(target,effectLevel,id);
            }

            if (state == State.IDLE){
                if (!ship.isAlive()){
                    effect.update(target,effectLevel,id);
                }
                //yeah
            }
        }

        if (effectLevel > 0){
            float shipjitter = effectLevel * 0.7F;
            ship.setJitter(ship,jitterColor,shipjitter,2,5f);

            if (shipjitter > 0){
                ship.setJitter(ship,jitterColor,shipjitter,2,5f);
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
            return "BURST READY";
        }
        if ((target == null) && ship.getShipTarget() != null) {
            return "OUT OF RANGE";
        }
        return "NO TARGET";
    }
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        ShipAPI target = findTarget(ship);
        return target != null && target != ship;
    }
}
