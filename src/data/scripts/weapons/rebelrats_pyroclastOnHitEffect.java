package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_pyroclastOnHitEffect implements OnHitEffectPlugin {
    private final int numMissileDebris = 30;
    private final int numChargeParticles = 30;
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
        p.addExplosion("graphics/fx/explosion0.png",null,new Vector2f(50,50), new Vector2f(200,200),point,new Vector2f(0,0),new Color(198,71,49),3F,4,2,0,angle,1,false);
        CombatEntityAPI explosion = engine.addLayeredRenderingPlugin(p);
        explosion.getLocation().set(point);

        rebelrats_addExplosionFx p2 = new rebelrats_addExplosionFx();
        p2.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(100,100), new Vector2f(250,250),point,new Vector2f(0,0),new Color(217,115,97),3F,4,0.8F,0,angle,0.8F,false);
        CombatEntityAPI explosion2 = engine.addLayeredRenderingPlugin(p2);
        explosion2.getLocation().set(point);

        for(int i = 0;i < numMissileDebris;i++){
            float coneAngle = rebelrats_combatUtils.calcConeAngle(170,projectile.getFacing() - 180f);
            engine.addHitParticle(point,rebelrats_combatUtils.calcVelDir(coneAngle,rebelrats_combatUtils.randomNumber(400f,500f)),12f,1f,1.5f,new Color(255,71,49,255));
        }
        for(int i = 0;i < numChargeParticles;i++){
            float coneAngle = rebelrats_combatUtils.calcConeAngle(8,projectile.getFacing());
            engine.addSmoothParticle(point,rebelrats_combatUtils.calcVelDir(coneAngle,rebelrats_combatUtils.randomNumber(400f,500f)),rebelrats_combatUtils.randomNumber(35f,50f),1f,1f,new Color(255,60,40,255));
        }
    }
}
