package data.scripts.weapons;
import java.awt.*;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_doldrumsEffect implements OnHitEffectPlugin,EveryFrameWeaponEffectPlugin,OnFireEffectPlugin{
    private static float emparcs = 3;
    private static float numexplosions = 20;
    private static float shieldpiercechance = 0.3F;
    private int numShrap = 15;
    private float elapsed = 0;
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
        //check if target is a ship and assign variable
        if (!(target instanceof ShipAPI)) return;

        float emp = 0;
        float dam = 0;
        for (int i = 0; i < emparcs; i++){
            engine.spawnEmpArcPierceShields(
                    projectile.getSource(), point, target, target,
                    DamageType.ENERGY,
                    dam, // damage
                    emp, // emp
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
            for (int i = 0; i < numShrap; i++) {
                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing() - 180, 60, point);
                CombatEntityAPI p = engine.spawnProjectile(projectile.getSource(), null, "rebelrats_railgun_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
                float angle = rebelrats_combatUtils.calcConeAngle(180,projectile.getFacing() - 180);
                p.setFacing(angle);
            }
        }
    }
    //protected DamagingProjectileAPI proj;
    boolean fired = false;
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        if (!fired) {
            weapon.getAnimation().setFrame(0);
            return;
        }else{
            elapsed += amount;
        }

        float f = weapon.getAnimation().getNumFrames() / weapon.getAnimation().getFrameRate();
        weapon.getAnimation().play();

        if (weapon.getAnimation().getFrame() >= 13 && weapon.getAnimation().getFrame() < 37){
            //right vent
            float angle = rebelrats_combatUtils.calcConeAngle(40,0);
            Vector2f vel = rebelrats_combatUtils.calcVelDir((weapon.getCurrAngle() - 135) + angle, 400);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() - 135, 16, weapon.getLocation());

            rebelrats_addParticle p = new rebelrats_addParticle();
            p.addParticle(null,"misc", "nebula_particles",20,20,loc,new Vector2f(0,0),vel,angle,1,false,0,0.2F,1,0.5F,true,Color.WHITE);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(weapon.getLocation());

            //left
            float angle2 = rebelrats_combatUtils.calcConeAngle(40,0);
            Vector2f vel2 = rebelrats_combatUtils.calcVelDir((weapon.getCurrAngle() + 135) + angle2, 400);
            Vector2f loc2 = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() + 135, 16, weapon.getLocation());

            rebelrats_addParticle p2 = new rebelrats_addParticle();
            p2.addParticle(null,"misc", "nebula_particles",20,20,loc2,new Vector2f(0,0),vel2,angle2,1,false,0,0.2F,1,0.5F,true,Color.WHITE);
            CombatEntityAPI e2 = engine.addLayeredRenderingPlugin(p2);
            e2.getLocation().set(weapon.getLocation());
        }


        if (elapsed > f) {
                fired = !fired;
                elapsed -= f;
        }
    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        fired = true;

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(projectile,"graphics/fx/doldrums_warning.png",null,240,80,null,new Vector2f(80,10),new Vector2f(0,0),0,0,false,0,15,1,0.5F,false, new Color(233,91,14,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
