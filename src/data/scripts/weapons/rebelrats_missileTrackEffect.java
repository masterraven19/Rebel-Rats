package data.scripts.weapons;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.combat.rebelrats_missileProxy;
import data.scripts.combat.rebelrats_missileScript;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.ai.MagicMissileAI;

import java.awt.Color;

//hwacha, rattenjager and snapper
public class rebelrats_missileTrackEffect implements OnFireEffectPlugin, OnHitEffectPlugin {
    private int numShrapInner = 20;
    private int numShrapRing = 30;
    private DamagingExplosionSpec createExplosionSpec() {
        float damage = 80f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.1f, // duration
                40f, // radius
                40f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.7f, // particleDuration
                0, // particleCount
                new Color(179,179,179,255), // particleColor
                new Color(163,124,200,0)  // explosionColor
        );

        spec.setDetailedExplosionFlashColorCore(new Color(217, 194, 158));
        spec.setDetailedExplosionFlashColorFringe(new Color(255, 255, 255));
        spec.setDetailedExplosionFlashDuration(0.3f);
        spec.setDetailedExplosionRadius(20f);
        spec.setDetailedExplosionFlashRadius(90f);
        spec.setDamageType(DamageType.FRAGMENTATION);
        return spec;
    }
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;
        if (projectile.isExpired()) return;

        if (projectile.getWeapon().getId().contains("hwacha")){
            rebelrats_missileProxy p = new rebelrats_missileProxy((MissileAPI) projectile, engine);
            engine.addPlugin(p);
        }else{
            MissileAPI missile = (MissileAPI) projectile;
            MagicMissileAI ai = new MagicMissileAI(missile, weapon.getShip());
            rebelrats_missileScript rat = new rebelrats_missileScript(missile,engine,ai);

            missile.setMissileAI(ai);
            engine.addPlugin(rat);
        }
    }

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if(projectile.getWeapon().getId().contains("hwacha")){
            engine.spawnDamagingExplosion(createExplosionSpec(), projectile.getSource(), point);
            float projAngle = projectile.getFacing() - 180f;
            float coneAngle = 170f;
            for (int i = 0; i < numShrapRing; i++) {
                float angle = rebelrats_combatUtils.calcConeAngle(coneAngle,projAngle);
                engine.addHitParticle(projectile.getLocation(),rebelrats_combatUtils.calcVelDir(angle,160f),rebelrats_combatUtils.randomNumber(10f,12f),2f,1f, new Color(200,200,200,255));
            }
            for (int i = 0; i < numShrapInner; i++) {
                float angle = rebelrats_combatUtils.calcConeAngle(coneAngle,projAngle);
                engine.addHitParticle(projectile.getLocation(),rebelrats_combatUtils.calcVelDir(angle,rebelrats_combatUtils.randomNumber(100f,101f)),rebelrats_combatUtils.randomNumber(5f,8f),1f,2f, new Color(200,200,200,255));
            }
        }
    }
}
