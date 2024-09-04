package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.combat.rebelrats_arvalisPostEffect;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_arvalisEffect implements OnHitEffectPlugin{
    private float shieldBounceChance = 0.5F;

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;

        if (shieldHit){
            if ((float)Math.random() > shieldBounceChance)return;

            float pFacing = projectile.getFacing();
            float reflectAngle;

            float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
            if(pFacing > 0){reflectAngle = pFacing -180;}
            else{reflectAngle = pFacing + 180;}
            float diff = normal - reflectAngle;
            reflectAngle = normal + diff;

            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(reflectAngle,40,point);
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_arvalis_munition",loc, reflectAngle,projectile.getSource().getVelocity());
            engine.addPlugin(new rebelrats_arvalisPostEffect(newproj,true,target,engine));
        }
        if (!shieldHit){
            engine.addPlugin(new rebelrats_arvalisPostEffect(projectile,false,target,engine));
        }
    }
}
