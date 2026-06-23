package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import data.scripts.combat.ids.rebelrats_Weapons;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.combat.rebelrats_arvalisPostEffect;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_arvalisEffect implements OnHitEffectPlugin{
    private final float shieldBounceChance = 0.5F;
    public final static int numExplosions = 10;
    public final static float armorDmg = 20;

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult,
                      CombatEngineAPI engine) {

        if (!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;

        float pFacing = projectile.getFacing();

        if (shieldHit){
            if (Math.random() > shieldBounceChance)return;
            float reflectAngle;

            float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(target.getLocation(),point);
            reflectAngle = rebelrats_combatUtils.flipAngle(pFacing);
            reflectAngle = rebelrats_combatUtils.findReflectedAngle(reflectAngle, normal);

            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(reflectAngle,40,point);
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(),
                    null,
                    rebelrats_Weapons.arvalisMunition,
                    loc,
                    reflectAngle,
                    projectile.getSource().getVelocity());
            engine.addPlugin(new rebelrats_arvalisPostEffect(
                    newproj, engine, null, ship
            ));
        }
        if (!shieldHit){
            BreachOnHitEffect.dealArmorDamage(projectile, ship, point, armorDmg);

            DamagingProjectileAPI newProj = (DamagingProjectileAPI) engine.spawnProjectile(
                    projectile.getSource(),
                    projectile.getWeapon(),
                    rebelrats_Weapons.passThroughMunition,
                    point,
                    pFacing,
                    projectile.getSource().getVelocity()
            );
            newProj.setCollisionClass(CollisionClass.NONE);

            engine.addPlugin(new rebelrats_arvalisPostEffect(
                    null, engine, newProj, ship
            ));
        }
    }
    private void arvalisEffect() {

    }
}
