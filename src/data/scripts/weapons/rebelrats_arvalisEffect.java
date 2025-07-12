package data.scripts.weapons;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_combatUtils;
import data.scripts.combat.rebelrats_arvalisPostEffect;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class rebelrats_arvalisEffect implements OnHitEffectPlugin{
    private float shieldBounceChance = 0.5F;
    private float numExplosions = 10;
    private float armorDmg = 20;

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;

        float pFacing = projectile.getFacing();
        if (shieldHit){
            if ((float)Math.random() > shieldBounceChance)return;


            float reflectAngle;

            float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
            if(pFacing > 0){reflectAngle = pFacing -180;}
            else{reflectAngle = pFacing + 180;}
            float diff = normal - reflectAngle;
            reflectAngle = normal + diff;

            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(reflectAngle,40,point);
            CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_arvalis_munition",loc, reflectAngle,projectile.getSource().getVelocity());
            engine.addPlugin(new rebelrats_arvalisPostEffect(newproj,engine));
        }
        if (!shieldHit){
            float shipLength = ship.getCollisionRadius() * 1.5f;

            List<Vector2f> shipSegments = new ArrayList<>();
            for(BoundsAPI.SegmentAPI s : ship.getExactBounds().getSegments()){
                shipSegments.add(s.getP1());
            }

            for(int i = 0;i < numExplosions;i++){
                float ratio = i / numExplosions;
                Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(pFacing,shipLength * ratio, point);

                BreachOnHitEffect.dealArmorDamage(projectile,ship,exploloc,armorDmg);

                rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
                explosionFx.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(6,6),new Vector2f(50,50),exploloc,new Vector2f(0,0), new Color(22, 153, 196),1.5F,0.1f,2.2F,0,(float) Math.random() * 180,0.8F,false);
                CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
                e.getLocation().set(exploloc);

                boolean isInShip = rebelrats_combatUtils.isPointInPoints(shipSegments,exploloc);
                if(!isInShip){
                    CombatEntityAPI newproj = engine.spawnProjectile(projectile.getSource(), null,"rebelrats_arvalis_munition",exploloc, pFacing,null);
                    engine.addPlugin(new rebelrats_arvalisPostEffect(newproj,engine));
                    break;
                }
            }
        }
    }
}
