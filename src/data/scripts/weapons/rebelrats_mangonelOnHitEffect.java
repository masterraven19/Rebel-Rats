package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.List;

public class rebelrats_mangonelOnHitEffect implements OnHitEffectPlugin {
    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if(!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;
        float ratioCone = 60;
        float hullDmg = 300f;
        float shieldDmg = 100f;
        DamageType hullDmgType = DamageType.FRAGMENTATION;
        DamageType shieldDmgType = DamageType.KINETIC;

        if(shieldHit){
            engine.applyDamage(ship,point,shieldDmg,shieldDmgType,0,false,false,projectile.getSource());

            float scale = (ship.getCurrFlux()/ship.getMaxFlux()) * 1;
            int shrapamt = (int)(scale * 15);

            for(int i = 0;i < shrapamt;i++){
                float pFacing = projectile.getFacing();
                float reflectAngle = pFacing - 180;

                float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
                float diff = normal - reflectAngle;
                float reflectDiff = diff;
                reflectDiff = Math.abs(reflectDiff);
                if(reflectDiff > ratioCone){reflectDiff = ratioCone;}
                float ratio = reflectDiff / ratioCone;
                reflectAngle = normal + (ratio * diff);

                float coneAngle = 170 - (155 * ratio);
                float angle = rebelrats_combatUtils.calcConeAngle(coneAngle,reflectAngle);
                float speed = rebelrats_combatUtils.randomNumber(1000,1500);
                float fadout = (((speed - 999)/500) * 0.3f) + 0.08f;
                Vector2f velocity = rebelrats_combatUtils.calcVelDir(angle,speed);

                rebelrats_addParticle p = new rebelrats_addParticle();
                p.addParticle(null,"fx","rebelrats_mangonel_plasma",80,20,point,new Vector2f(0,0),velocity,angle,0,false,0,0.3f,1f,fadout,false,null);
                CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                e.getLocation().set(point);
            }
        }else{
            engine.applyDamage(ship,point,hullDmg,hullDmgType,0,false,false,projectile.getSource());

            int[] cell = ship.getArmorGrid().getCellAtLocation(point);
            if(cell == null) return;

            float armot = ship.getArmorGrid().getArmorValue(cell[0],cell[1]);
            float max = ship.getArmorGrid().getMaxArmorInCell();
            float scale = (armot/max) * 1;
            int shrapamt = (int)(scale * 15);
            int cloudamt = (int)((1 - scale) * 6);

            if(armot > 0){
                float pFacing = projectile.getFacing();
                float reflectAngle = pFacing - 180;

                float nearestMidpoint = 1000000f;
                List<BoundsAPI.SegmentAPI> segments = ship.getExactBounds().getSegments();
                BoundsAPI.SegmentAPI s0 = null;
                BoundsAPI.SegmentAPI s1 = null;

                for(int i = 0;i < segments.size();i++){
                    BoundsAPI.SegmentAPI currentSegment;
                    BoundsAPI.SegmentAPI previousSegment;

                    currentSegment = segments.get(i);
                    if(i == 0){
                        previousSegment = segments.get(segments.size() - 1);
                    }else{
                        previousSegment = segments.get(i - 1);
                    }
                    Vector2f midpoint = MathUtils.getMidpoint(currentSegment.getP1(),previousSegment.getP1());
                    float dist = MathUtils.getDistance(midpoint,point);
                    if(dist < nearestMidpoint){
                        nearestMidpoint = dist;
                        s0 = currentSegment;
                        s1 = previousSegment;
                    }
                }
                float surface = rebelrats_combatUtils.calcDirectionOfTwoPoints(s1.getP1(),s0.getP1());

                float turn = 90;
                float normal = surface - turn;

                for(int i = 0;i < shrapamt;i++){

                    float diff = normal - reflectAngle;
                    float reflectDiff = diff;
                    reflectDiff = Math.abs(reflectDiff);
                    if(reflectDiff > ratioCone){reflectDiff = ratioCone;}
                    float ratio = reflectDiff / ratioCone;
                    reflectAngle = normal + (ratio * diff);

                    float coneAngle = 170 - (155 * ratio);
                    float angle = rebelrats_combatUtils.calcConeAngle(coneAngle,reflectAngle);
                    float speed = rebelrats_combatUtils.randomNumber(1000,1500);
                    float fadout = (((speed - 999)/500) * 0.3f) + 0.08f;
                    Vector2f velocity = rebelrats_combatUtils.calcVelDir(angle,speed);

                    rebelrats_addParticle p = new rebelrats_addParticle();
                    p.addParticle(null,"fx","rebelrats_mangonel_plasma",80,20,point,new Vector2f(0,0),velocity,angle,0,false,0,0.3f,1f,fadout,false,null);
                    CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                    e.getLocation().set(point);
                }
                for(int i = 0;i < cloudamt;i++){
                    rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
                    p.addExplosion("misc","nebula_particles",new Vector2f(25,25),new Vector2f(140,140),point,new Vector2f(0,0),new Color(244,0,0),2F,2,1,0, rebelrats_combatUtils.randomNumber(0,360),0.7F,true);
                    CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                    e.getLocation().set(point);
                }
            }
        }
    }
}
