package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_petardBeamEffect implements BeamEffectPlugin{
    private boolean done = false;
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam){
        beam.setCoreTexture("graphics/fx/petardlaser_beam.png");
        if (done) return;
        CombatEntityAPI target = beam.getDamageTarget();
        boolean first = beam.getWeapon().getBeams().indexOf(beam) == 0;
        if (target != null && beam.getBrightness() >= 1f && first) {
            Vector2f point = beam.getTo();
            float maxDist = 0f;
            for (BeamAPI curr : beam.getWeapon().getBeams()) {
                maxDist = Math.max(maxDist, Misc.getDistance(point, curr.getTo()));
            }
            if (maxDist < 15f) {
                DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(), beam.getSource(), point);
                done = true;
            }
        }
    }
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = 700f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.5f, // duration
                400f, // radius
                400f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.7f, // particleDuration
                150, // particleCount
                new Color(222,146,24,255), // particleColor
                new Color(226,162,58,200)  // explosionColor
        );

        spec.setDamageType(DamageType.ENERGY);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
}
