package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.utils.rebelrats_booleanUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_amosBeamEffect implements BeamEffectPlugin {
    private IntervalUtil interval = new IntervalUtil(.4F,.4F);
    private float hackChance = 1F;
    private float range = 1500;
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        if (engine.isPaused()) return;
        if (engine.getMissiles().isEmpty()) return;
        WeaponAPI w = beam.getWeapon();
        ShipAPI ship = w.getShip();

        for (MissileAPI m : engine.getMissiles()){
            if (rebelrats_booleanUtils.notAvailableEnemyMissile(m,ship)) continue;
            if (rebelrats_booleanUtils.missileFlamedOut(m)) continue;

            float d = MathUtils.getDistance(w.getLocation(),m.getLocation());
            if (d > w.getRange())continue;
            if (d < range){

                Vector2f futureLocation = rebelrats_combatUtils.getFuturePosition(m,0.05f);
                float facing = rebelrats_combatUtils.calcDirectionOfTwoPoints(futureLocation,w.getLocation());
                w.setFacing(facing);
                w.setForceFireOneFrame(true);
                range = d;
            }
        }

        if (beam.getDamageTarget() == null || !(beam.getDamageTarget() instanceof MissileAPI)) return;
        MissileAPI m = (MissileAPI) beam.getDamageTarget();

        interval.advance(amount);
        if (interval.intervalElapsed()){
            if (Math.random() < hackChance){
                m.flameOut();
                range = w.getRange();
            }
        }
    }
}
