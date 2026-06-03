package data.scripts.combat.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.utils.rebelrats_booleanUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.Iterator;

public class rebelrats_amosAutofireAI implements AutofireAIPlugin {

    protected WeaponAPI weapon;
    protected ShipAPI targetShip;
    protected MissileAPI targetMissile;
    protected Vector2f targetLocation;

    private float range;
    private final float maxRange;
    private boolean shouldFire;

    public rebelrats_amosAutofireAI(WeaponAPI weapon) {
        this.weapon = weapon;
        maxRange = weapon.getRange();
        range = maxRange;
    }

    private void resetRange(){
        range = maxRange;
    }

    // Searches for the nearest missile first, then if theres a nearer ship
    private void searchTarget(CombatEngineAPI engine) {
        resetRange();

        Iterator<Object> missileIterator = engine.getMissileGrid().getCheckIterator(
                weapon.getLocation(),
                weapon.getRange(),
                weapon.getRange()
        );
        while (missileIterator.hasNext()) {
            MissileAPI missile = (MissileAPI) missileIterator.next();

            if (rebelrats_booleanUtils.isNotEnemyMissile(
                    missile,
                    weapon.getShip())
            ) continue;
            if (rebelrats_booleanUtils.missileFlamedOut(missile)) continue;

            float distance = MathUtils.getDistance(missile, weapon.getLocation());

            if (distance < range){
                targetMissile = missile;
                range = distance;
            }
        }

//        Iterator<Object> shipIterator = engine.getShipGrid().getCheckIterator(
//                weapon.getLocation(),
//                weapon.getRange(),
//                weapon.getRange()
//        );
//        while (shipIterator.hasNext()) {
//            ShipAPI ship = (ShipAPI) shipIterator.next();
//
//            if (!ship.isAlive() || ship.isHulk()) continue;
//            if (rebelrats_booleanUtils.isAllyShip(weapon, ship)) continue;
//
//            float distance = MathUtils.getDistance(ship, weapon.getLocation());
//
//            if (distance < range){
//                targetShip = ship;
//                range = distance;
//            }
//        }
    }

    private void checkIfMissileAndShipAlive(CombatEngineAPI engine) {
        if (targetMissile != null){
            if (rebelrats_booleanUtils.isNotAliveMissile(targetMissile) ||
                    !engine.isEntityInPlay(targetMissile) ||
                    rebelrats_booleanUtils.missileFlamedOut(targetMissile) ||
                    !Misc.isInArc(
                            weapon.getArcFacing(),
                            weapon.getArc(),
                            weapon.getLocation(),
                            targetMissile.getLocation()
                    ) ||
                    MathUtils.getDistance(targetMissile, weapon.getLocation()) < maxRange
            ) {
                targetMissile = null;
            }
        }

//        if (targetShip != null){
//            if (rebelrats_booleanUtils.isNotAliveShip(targetShip) ||
//                    !engine.isEntityInPlay(targetShip) ||
//                    !Misc.isInArc(
//                            weapon.getArcFacing(),
//                            weapon.getArc(),
//                            weapon.getLocation(),
//                            targetShip.getLocation()
//                    ) ||
//                    MathUtils.getDistance(targetShip, weapon.getLocation()) < maxRange
//            ) {
//                targetShip = null;
//            }
//        }
    }

    private boolean checkIfFire(float amount, CombatEntityAPI target) {
        targetLocation = rebelrats_combatUtils.predictLinearAndAngular(
                target,
                amount
        );
        if (Misc.isInArc(
                weapon.getCurrAngle(),
                5,
                weapon.getLocation(),
                targetLocation
        )) {
            return true;
        }
        return false;
    }

    private void resetIfNoTarget() {
        if (targetMissile == null && targetShip == null) {
            targetLocation = null;
            shouldFire = false;
        }
    }

    @Override
    public void advance(float amount) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine == null || engine.isPaused()) return;

        checkIfMissileAndShipAlive(engine);

        resetIfNoTarget();

        if (targetMissile == null || targetShip == null) {
            searchTarget(engine);
        }

        if (targetMissile != null) {
            if (checkIfFire(amount, targetMissile)) {
                shouldFire = true;
            }
            return;
        }

//        if (targetShip != null) {
//            if (checkIfFire(amount, targetShip)) {
//                shouldFire = true;
//            }
//        }
    }

    @Override
    public boolean shouldFire() {
        return shouldFire;
    }

    @Override
    public void forceOff() {
        weapon.stopFiring();
    }

    @Override
    public Vector2f getTarget() {
        return targetLocation;
    }

    @Override
    public ShipAPI getTargetShip() {
        return targetShip;
    }

    @Override
    public WeaponAPI getWeapon() {
        return weapon;
    }

    @Override
    public MissileAPI getTargetMissile() {
        return targetMissile;
    }
}
