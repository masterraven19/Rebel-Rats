package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_kingmakerEffect implements OnHitEffectPlugin, EveryFrameWeaponEffectPlugin, OnFireEffectPlugin {
    private float elapsed = 0;
    private float elapsed2 = 0;
    private float particleDuration = 1F;
    private float numexplosions = 20;
    private float shieldpiercechance = 0.3F;
    private float numEmp = 10;
    private boolean fired = false;
    private boolean fired2 = false;
    private DamagingProjectileAPI proj;
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = 150f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.1f, // duration
                80f, // radius
                80f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.7f, // particleDuration
                0, // particleCount
                new Color(163,124,200,0), // particleColor
                new Color(163,124,200,0)  // explosionColor
        );

        spec.setDamageType(DamageType.HIGH_EXPLOSIVE);
        spec.setUseDetailedExplosion(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;

        for (int i = 0; i < numEmp; i++) {
            float angle = rebelrats_combatUtils.calcConeAngle(360,0);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(angle,target.getCollisionRadius() * 0.8F,point);
            engine.spawnEmpArcPierceShields(
                    projectile.getSource(), loc, target, target,
                    DamageType.ENERGY,
                    0, // damage
                    0, // emp
                    100000f, // max range
                    "tachyon_lance_emp_impact",
                    30f, // thickness
                    //new Color(25,100,155,255),
                    //new Color(255,255,255,255)
                    new Color(73, 171, 255, 255),
                    new Color(255, 255, 255, 255)
            );
        }

        if (shieldHit){
            if (Math.random() < shieldpiercechance){
                Vector2f projloc = new Vector2f(projectile.getLocation());
                float shipLength = target.getCollisionRadius() * 1.2F;

                for (int i = 0; i < numexplosions; i++) {
                    Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * i/numexplosions, projloc);
                    DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                    engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
                }
                Vector2f point2 = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * 1.3F,point);
                CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_kingmaker_munition",point2, projectile.getFacing(),projectile.getSource().getVelocity());
            }
        }
        if (!shieldHit){
            Vector2f projloc = new Vector2f(projectile.getLocation());
            float shipLength = target.getCollisionRadius() * 1.2F;

            for (int i = 0; i < numexplosions; i++) {
                Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * i/numexplosions, projloc);
                DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
            }
            Vector2f point2 = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * 1.3F,point);
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_kingmaker_munition",point2, projectile.getFacing(),projectile.getSource().getVelocity());
        }
    }
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;

        if (fired2 && proj.didDamage()){
            elapsed2 += amount;
        }
        if (elapsed2 > 0.1){
            float currAngle = proj.getFacing();
            Vector2f projloc = proj.getLocation();

            Vector2f vel = rebelrats_combatUtils.calcVelDir(currAngle, 50);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(currAngle, 0, projloc);
            float range = (1 - 0.5F) + 1;
            float alphaMult = (float)(Math.random() * range) + 0.5F;

            rebelrats_addParticle p = new rebelrats_addParticle();
            p.addParticle(null,"misc", "nebula_particles",100,100,loc,new Vector2f(0,0),vel,currAngle,1,false,0,1F,alphaMult,0.5F,true,new Color(235,54,54,255));
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(projloc);
        }
        if (elapsed2 > particleDuration){
            fired2 = false;
            elapsed2 -= particleDuration;
        }

        if (!fired) {
            weapon.getAnimation().setFrame(0);
            return;
        }else{
            elapsed += amount;
        }

        float f = weapon.getAnimation().getNumFrames() / weapon.getAnimation().getFrameRate();
        weapon.getAnimation().play();

        if (weapon.getAnimation().getFrame() >= 6 && weapon.getAnimation().getFrame() < 37){
            float angle = rebelrats_combatUtils.calcConeAngle(30,0);
            Vector2f vel = rebelrats_combatUtils.calcVelDir((weapon.getCurrAngle() - 180) + angle, 200);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() - 180, 32, weapon.getLocation());
            float range = (1 - 0.5F) + 1;
            float alphaMult = (float)(Math.random() * range) + 0.5F;

            rebelrats_addParticle p = new rebelrats_addParticle();
            p.addParticle(null,"misc", "nebula_particles",20,20,loc,new Vector2f(0,0),vel,angle,1,false,0,0.2F,alphaMult,0.5F,true,new Color(141,95,240,255));
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(weapon.getLocation());
        }

        if (elapsed > f) {
            fired = !fired;
            elapsed -= f;
        }
    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        fired = true;
        fired2 = true;
        proj = projectile;

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(projectile,"graphics/fx/doldrums_warning.png",null,240,80,null,new Vector2f(80,10),new Vector2f(0,0),0,0,false,0,15,1,0.5F,false, new Color(233,91,14,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}