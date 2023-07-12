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
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_doldrumsEffect implements OnHitEffectPlugin,EveryFrameWeaponEffectPlugin,OnFireEffectPlugin{
    private static float emparcs = 3;
    private static float numexplosions = 10;
    private static float shieldpiercechance = 0.3F;
    private float elapsed = 0;
    public DamagingExplosionSpec createExplosionSpec() {
        float damage = 300f;
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
        ShipAPI ship = (ShipAPI) target;
        //emp pierce chance
        float pierceChance = ((ShipAPI) target).getHardFluxLevel() - 0.1f;
        pierceChance *= ship.getMutableStats().getDynamic().getValue(Stats.SHIELD_PIERCED_MULT);
        boolean piercedShield = shieldHit && (float) Math.random() < pierceChance;
        //shield pierce chance

        if (!shieldHit || piercedShield){
            float emp = projectile.getEmpAmount();
            float dam = 100;
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
        }
        if (shieldHit){
            if (Math.random() < shieldpiercechance){
                Vector2f projloc = new Vector2f(projectile.getLocation());
                float shipLength = target.getCollisionRadius() * 1.2F;
                float angle = projectile.getFacing() * (float) Math.PI / 180;
                float dx = (float) Math.cos(angle);
                float dy = (float) Math.sin(angle);

                for (int i = 0; i < numexplosions; i++) {
                    Vector2f exploloc = new Vector2f((shipLength*i/numexplosions) * dx + projloc.x, (shipLength*i/numexplosions) * dy + projloc.y);
                    DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                    engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
                }
            }
        }
        if (!shieldHit){
            Vector2f projloc = new Vector2f(projectile.getLocation());
            float shipLength = target.getCollisionRadius() * 1.2F;
            float angle = projectile.getFacing() * (float) Math.PI / 180;
            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            for (int i = 0; i < numexplosions; i++) {
                Vector2f exploloc = new Vector2f((shipLength*i/numexplosions) * dx + projloc.x, (shipLength*i/numexplosions) * dy + projloc.y);
                DamagingProjectileAPI e = engine.spawnDamagingExplosion(createExplosionSpec(),projectile.getSource(),exploloc);
                engine.spawnExplosion(exploloc, new Vector2f(30,30),Color.getHSBColor(207,71,35),30F,1F);
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

        if (elapsed > f) {

                fired = !fired;
                elapsed -= f;
        }
    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        fired = true;
    }
}
