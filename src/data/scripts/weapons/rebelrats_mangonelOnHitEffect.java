package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_mangonelOnHitEffect implements OnHitEffectPlugin {
    @Override //if armored or shielded shrapnel, if bare hull pyroclast explson
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
                float reflectAngle;

                float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
                if(pFacing > 0){reflectAngle = pFacing -180;}
                else{reflectAngle = pFacing + 180;}
                float diff = normal - reflectAngle;
                float reflectDiff = diff;
                if(diff < -180){reflectDiff += 360;}
                if(diff > 180){reflectDiff -= 360;}
                if(reflectDiff < 0){reflectDiff *= -1;}
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
            int cloudamt = 3;

            if(armot > 0){
                for(int i = 0;i < shrapamt;i++){
                    float pFacing = projectile.getFacing();
                    float reflectAngle;

                    float normal = rebelrats_combatUtils.calcDirectionOfTwoPoints(point,target.getLocation());
                    if(pFacing > 0){reflectAngle = pFacing -180;}
                    else{reflectAngle = pFacing + 180;}
                    float diff = normal - reflectAngle;
                    float reflectDiff = diff;
                    if(diff < -180){reflectDiff += 360;}
                    if(diff > 180){reflectDiff -= 360;}
                    if(reflectDiff < 0){reflectDiff *= -1;}
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
                for(int i = 0;i < cloudamt;i++){
                    rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
                    p.addExplosion("misc","nebula_particles",new Vector2f(50,50),new Vector2f(80,80),point,new Vector2f(0,0),new Color(244,0,0),1.5F,2,1,0, rebelrats_combatUtils.randomNumber(0,360),0.7F,true);
                    CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                    e.getLocation().set(point);
                }
            }
        }
    }
}
