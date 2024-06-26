package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import data.scripts.combat.rebelrats_combatUtils;
import org.lazywizard.lazylib.MathUtils;

public class rebelrats_amosBeamEffect implements BeamEffectPlugin {
    private IntervalUtil interval = new IntervalUtil(.4F,.4F);
    private float hackChance = .7F;
    private float range = 1500;
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        if (engine.isPaused())return;
        if (engine.getMissiles().isEmpty())return;
        WeaponAPI w = beam.getWeapon();

        for (MissileAPI m : engine.getMissiles()){
            if(m.isFading() || m.isExpired())continue;
            if(m.isFizzling())continue;

            float d = MathUtils.getDistance(w.getLocation(),m.getLocation());
            if(d > w.getRange())continue;
            if(d < range){
                range = d;
                w.setFacing(rebelrats_combatUtils.calcDirectionOfTwoPoints(m.getLocation(),w.getLocation()));
                w.setForceFireOneFrame(true);
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
