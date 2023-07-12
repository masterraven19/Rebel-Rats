package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_pyroclastOnHitEffect implements OnHitEffectPlugin {
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = 1200f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                2f, // duration
                60f, // radius
                60f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                5f, // particleSizeMin
                5f, // particleSizeRange
                0.7f, // particleDuration
                150, // particleCount
                new Color(0,212,255,255), // particleColor
                new Color(0,212,255,0)  // explosionColor
        );

        spec.setDamageType(DamageType.FRAGMENTATION);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        damageResult.setTotalDamageToArmor(300F);
        DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(), projectile.getSource(), point);
    }
}
