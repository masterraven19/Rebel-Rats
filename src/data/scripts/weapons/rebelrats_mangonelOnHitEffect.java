package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_mangonelOnHitEffect implements OnHitEffectPlugin {
    @Override //if armored or shielded shrapnel, if bare hull pyroclast explson
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if(!(target instanceof ShipAPI)) return;
        ShipAPI ship = (ShipAPI) target;
        float hullDmg = 300f;
        DamageType hullDmgType = DamageType.FRAGMENTATION;
        float shieldDmg = 100f;
        DamageType shieldDmgType = DamageType.KINETIC;

        if(shieldHit){
            engine.applyDamage(ship,point,shieldDmg,shieldDmgType,0,false,false,projectile.getSource());

            float scale = (ship.getCurrFlux()/ship.getMaxFlux()) * 1;
            int amt = (int)(scale * 15);

            for(int i = 0;i < amt;i++){
                float angle = rebelrats_combatUtils.calcConeAngle(rebelrats_combatUtils.randomNumber(120,170),projectile.getFacing() - 180);
                Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing() - 180, 50, point);

                DamagingProjectileAPI p = (DamagingProjectileAPI) engine.spawnProjectile(projectile.getSource(), null, "rebelrats_mangonel_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
//                Vector2f speed = rebelrats_combatUtils.calcVelDir(angle,200);
//                p.getVelocity().set(speed); try this tmrw
            }
        }else{
            engine.applyDamage(ship,point,hullDmg,hullDmgType,0,false,false,projectile.getSource());

            int[] cell = ship.getArmorGrid().getCellAtLocation(point);
            if(cell == null) return;

            float armot = ship.getArmorGrid().getArmorValue(cell[0],cell[1]);
            float max = ship.getArmorGrid().getMaxArmorInCell();
            float scale = (armot/max) * 1;
            int amt = (int)(scale * 15);

            if(armot > 0){
                for(int i = 0;i < amt;i++){
                    float angle = rebelrats_combatUtils.calcConeAngle(rebelrats_combatUtils.randomNumber(120,170),projectile.getFacing() - 180);
                    Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing() - 180, 50, point);

                    CombatEntityAPI p = engine.spawnProjectile(projectile.getSource(), null, "rebelrats_mangonel_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
                    p.setFacing(angle);
                    p.getVelocity().scale((float)Math.random());
                }
            }else{
                for(int i = 0;i < 3;i++){
                    rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
                    p.addExplosion("misc","nebula_particles",new Vector2f(50,50),new Vector2f(80,80),point,new Vector2f(0,0),new Color(244,0,0),3.5F,4,1,0, rebelrats_combatUtils.randomNumber(0,360),0.7F,true);
                    CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                    e.getLocation().set(point);
                }
            }
        }
    }
}
