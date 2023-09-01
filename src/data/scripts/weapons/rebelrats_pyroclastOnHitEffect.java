package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addExplosionFx;
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
                0, // particleCount //150, turned off because replaced by custom FX
                new Color(198,71,49,255), // particleColor
                new Color(0,212,255,0)  // explosionColor
        );

        spec.setDamageType(DamageType.FRAGMENTATION);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(), projectile.getSource(), point);

        float angle = (float) Math.random() * 180;
        rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
        p.addExplosion("graphics/fx/explosion0.png",null,new Vector2f(50,50), new Vector2f(200,200),point,new Color(198,71,49),0.5F,4,2,0,angle,1,false);
        CombatEntityAPI explosion = engine.addLayeredRenderingPlugin(p);
        explosion.getLocation().set(point);

        rebelrats_addExplosionFx p2 = new rebelrats_addExplosionFx();
        p2.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(100,100), new Vector2f(250,250),point,new Color(217,115,97),3F,4,0.8F,0,angle,0.8F,false);
        CombatEntityAPI explosion2 = engine.addLayeredRenderingPlugin(p2);
        explosion2.getLocation().set(point);
    }
}
