package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_falconOnHitEffect implements OnHitEffectPlugin{
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = 400f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.1f, // duration
                60f, // radius
                60f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.7f, // particleDuration
                150, // particleCount
                new Color(179,179,179,255), // particleColor
                new Color(163,124,200,0)  // explosionColor
        );

        spec.setDamageType(DamageType.KINETIC);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),point,false);
    }
}
