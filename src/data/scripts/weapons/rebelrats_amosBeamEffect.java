package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.utils.rebelrats_booleanUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_amosBeamEffect implements BeamEffectPlugin {
    private IntervalUtil interval = new IntervalUtil(.1F,.1F);

    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        if (engine.isPaused()) return;
        if (engine.getMissiles().isEmpty()) return;

        if (beam.getDamageTarget() == null || !(beam.getDamageTarget() instanceof MissileAPI)) return;
        MissileAPI m = (MissileAPI) beam.getDamageTarget();

        interval.advance(amount);
        if (interval.intervalElapsed()){
            m.flameOut();
        }
    }
}
