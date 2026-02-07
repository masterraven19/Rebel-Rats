package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_kingmakerEffect implements OnHitEffectPlugin, EveryFrameWeaponEffectPlugin, OnFireEffectPlugin {
    private float elapsed = 0;
    private float elapsed2 = 0;
    private float elapsed3 = 0;
    private float numexplosions = 20;
    private float explosionDmg = 50;
    private float armorDmg = 100;
    private float shieldpiercechance = 0.3F;
    private float numEmp = 10;
    private float baseCone = 40;
    private float penDist = 600;
    private int numShrap = 15;
    private float fullyOpenedSmokeRate = 1/4; //1/rate
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
            float reflectAngle = pFacing - 180;
            float dist = MathUtils.getDistance(projectile.getSource(),target);

            float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
            float diff = normal - reflectAngle;
            reflectAngle = normal + diff;

            float reflectDiff = Math.abs(diff);

            float dmg = projectile.getWeapon().getDamage().getDamage();

            if (reflectDiff > baseCone && dist > penDist){
                dmg *= 0.5F;
                engine.applyDamage(target,point,dmg,projectile.getDamageType(), projectile.getEmpAmount(),false,false,projectile.getSource());

                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(reflectAngle,40,point);
                CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_kingmaker_munition",loc, reflectAngle,projectile.getSource().getVelocity());
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
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_kingmaker_munition",point2, projectile.getFacing(),projectile.getSource().getVelocity());

        }
    }

//    private static Logger logger = Global.getLogger(rebelrats_kingmakerEffect.class);
    private boolean fullyOpened = false;
    private int currFrame = 0;
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;

        if (weapon.getCooldownRemaining() == 0) {
            weapon.getAnimation().setFrame(0);
            fullyOpened = false;
            elapsed3 = 0;
            return;
        }else{
            elapsed += amount;
            elapsed2 += amount;
            elapsed3 += amount;
        }

        float framerate = 1 / weapon.getAnimation().getFrameRate();
        int totalFrames = weapon.getAnimation().getNumFrames();

        float cooldown = weapon.getCooldown();
        boolean isCooling = (elapsed3 < cooldown * 0.5f);

        if (elapsed > framerate){
            if (currFrame < totalFrames - 1 && !fullyOpened){
                currFrame++;
            }else{
                fullyOpened = true;
            }
            if (isCooling && fullyOpened){
                currFrame = totalFrames - 1;
            }
            if (currFrame > 0 && !isCooling){
                currFrame--;
            }
            elapsed -= framerate;
        }
        weapon.getAnimation().setFrame(currFrame);

        if (!isCooling) return;
        if (elapsed2 >= fullyOpenedSmokeRate){
            if (currFrame >= 7){
                //top vents left
                addSmoke(engine,weapon,14,90,90,15);
                addSmoke(engine,weapon,8,97,90,15);
                //top vents right
                addSmoke(engine,weapon,-14,90,-90,15);
                addSmoke(engine,weapon,-8,97,-90,15);
            }
            if (currFrame >= 9){
                //bottom vents
                addSmoke(engine,weapon,153,33,140,20);
                addSmoke(engine,weapon,-153,33,-140,20);
            }
            elapsed2 -= fullyOpenedSmokeRate;
        }
    }
    private void addSmoke(CombatEngineAPI engine, WeaponAPI weapon, float angleOffsetFromWeaponAngle,
                          float lengthFromWeaponMiddle, float angleOffsetVelocity, float smokeSize){
        float angle = rebelrats_combatUtils.calcConeAngle(9,0);
        Vector2f vel = rebelrats_combatUtils.calcVelDir((weapon.getCurrAngle() + angleOffsetVelocity) + angle, rebelrats_combatUtils.randomNumber(160,200));
        Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() + angleOffsetFromWeaponAngle, lengthFromWeaponMiddle, weapon.getLocation());
        float alphaMult = rebelrats_combatUtils.randomNumber(0.5f,0.9f);

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(null,"misc", "nebula_particles",smokeSize,smokeSize,loc,new Vector2f(0,0),vel,angle,1,false,0,0.25F,alphaMult,0.2F,true,new Color(229, 229, 229,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(weapon.getLocation());
    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        projectile.getDamage().setDamage(1);

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(projectile,"fx","rebelrats_doldrums_warning",240,80,null,new Vector2f(80,10),new Vector2f(0,0),0,0,false,0,15,1,0.5F,false, new Color(233,91,14,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
