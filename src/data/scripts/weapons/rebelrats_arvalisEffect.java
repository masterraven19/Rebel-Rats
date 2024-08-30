package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_arvalisEffect implements OnHitEffectPlugin, EveryFrameWeaponEffectPlugin, OnFireEffectPlugin {
    private float elapsed = 0;
    private float elapsed2 = 0;
    private float particleDuration = 1F;
    private float numexplosions = 20;
    private float explosionDmg = 5;
    private float armorDmg = 5;
    private float shieldpiercechance = 0.3F;
    private float numEmp = 2;
    private float baseCone = 40;
    private float penDist = 600;
    private int numShrap = 15;
    private boolean fired = false;
    private boolean fired2 = false;
    private DamagingProjectileAPI proj;
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = explosionDmg;
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

        spec.setDamageType(DamageType.FRAGMENTATION);
        spec.setUseDetailedExplosion(false);
        spec.setShowGraphic(false);
        spec.setSoundSetId("explosion_guardian");
        return spec;
    }
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;

        for (int i = 0; i < numEmp; i++) {
            float angle = rebelrats_combatUtils.calcConeAngle(360,0);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(angle,100f,point);
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
            float pFacing = projectile.getFacing();
            float reflectAngle;
            float dist = MathUtils.getDistance(projectile.getSource(),target);

            float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
            if(pFacing > 0){reflectAngle = pFacing -180;}
            else{reflectAngle = pFacing + 180;}
            float diff = normal - reflectAngle;
            reflectAngle = normal + diff;

            float reflectDiff = diff;
            if(diff < -180){reflectDiff += 360;}
            if(diff > 180){reflectDiff -= 360;}

            float dmg = projectile.getWeapon().getDamage().getDamage();

            if ((reflectDiff > baseCone || reflectDiff < -baseCone) && dist > penDist){
                dmg *= 0.5F;
                engine.applyDamage(target,point,dmg,projectile.getDamageType(), projectile.getEmpAmount(),false,false,projectile.getSource());

                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(reflectAngle,40,point);
                CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_muridae_m",loc, reflectAngle,projectile.getSource().getVelocity());
                return;
            }
            engine.applyDamage(target,point,dmg,projectile.getDamageType(), projectile.getEmpAmount(),false,false,projectile.getSource());


            if (Math.random() < shieldpiercechance){
                Vector2f projloc = new Vector2f(projectile.getLocation());
                float shipLength = target.getCollisionRadius() * 1.2F;

                for (int i = 0; i < numexplosions; i++) {
                    Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * i/numexplosions, projloc);
                    DamagingProjectileAPI ex = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                    engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
                    BreachOnHitEffect.dealArmorDamage(projectile,ship,exploloc,armorDmg);

                    rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
                    explosionFx.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(50,50),new Vector2f(100,100),exploloc,new Vector2f(0,0), new Color(235,163,54),1.5F,2,2.2F,0,(float) Math.random() * 180,0.8F,false);
                    CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
                    e.getLocation().set(point);
                }
            }
        }
        if (!shieldHit){
            Vector2f projloc = new Vector2f(projectile.getLocation());
            float shipLength = target.getCollisionRadius() * 1.2F;
            float dmg = projectile.getWeapon().getDamage().getDamage();
            engine.applyDamage(target,point,dmg,projectile.getDamageType(), projectile.getEmpAmount(),false,false,projectile.getSource());

            for (int i = 0; i < numShrap; i++) {
                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing() - 180, 60, point);
                CombatEntityAPI p = engine.spawnProjectile(projectile.getSource(), null, "rebelrats_railgun_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
                float angle = rebelrats_combatUtils.calcConeAngle(180,projectile.getFacing() - 180);
                p.setFacing(angle);
            }
            for (int i = 0; i < numexplosions; i++) {
                Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * i/numexplosions, projloc);
                DamagingProjectileAPI ex = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
                BreachOnHitEffect.dealArmorDamage(projectile,ship,exploloc,armorDmg);

                rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
                explosionFx.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(50,50),new Vector2f(100,100),exploloc,new Vector2f(0,0), new Color(235,163,54),1.5F,2,2.2F,0,(float) Math.random() * 180,0.8F,false);
                CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
                e.getLocation().set(point);
            }
            for (int i = 0; i < numShrap; i++) {
                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(), shipLength * 1.3F, point);
                CombatEntityAPI p = engine.spawnProjectile(projectile.getSource(), null, "rebelrats_railgun_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
                float angle = rebelrats_combatUtils.calcConeAngle(30,projectile.getFacing());
                p.setFacing(angle);
            }
            Vector2f point2 = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing(),shipLength * 1.3F,point);
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_muridae_m",point2, projectile.getFacing(),projectile.getSource().getVelocity());

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
            p.addParticle(null,"misc", "nebula_particles",80,80,loc,new Vector2f(0,0),vel,currAngle,1,false,0,1F,alphaMult,0.5F,true,new Color(235,54,54,160));
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
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() - 180, 35, weapon.getLocation());
            float range = (1 - 0.5F) + 1;
            float alphaMult = (float)(Math.random() * range) + 0.5F;

            rebelrats_addParticle p = new rebelrats_addParticle();
            p.addParticle(null,"misc", "nebula_particles",30,30,loc,new Vector2f(0,0),vel,angle,1,false,0,0.2F,alphaMult,0.5F,true,new Color(141,95,240,255));
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
        projectile.getDamage().setDamage(1);

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(projectile,"graphics/fx/doldrums_warning.png",null,240,80,null,new Vector2f(80,10),new Vector2f(0,0),0,0,false,0,15,1,0.5F,false, new Color(233,91,14,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
