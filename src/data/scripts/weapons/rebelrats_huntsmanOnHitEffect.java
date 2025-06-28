package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_huntsmanOnHitEffect implements OnHitEffectPlugin {
    private int numParticles = 4;
    private int numMissileDebris = 10;
    private int numExplosionDebris = 30;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        for (int i = 0;i<numParticles;i++){
            rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();;
            p.addExplosion("misc","nebula_particles",new Vector2f(50,50),new Vector2f(200,200),point,new Vector2f(0,0),new Color(42,106,170),2f,3f,1.3f,0,0,0.7F,true);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(point);
        }
        for(int i = 0;i < numMissileDebris;i++){
            float angle = rebelrats_combatUtils.calcConeAngle(170,projectile.getFacing() - 180f);
            engine.addHitParticle(point,rebelrats_combatUtils.calcVelDir(angle,rebelrats_combatUtils.randomNumber(300f,400f)),10f,1f,2.5f,new Color(255, 255, 255,255));
        }
        if(shieldHit) return;
        for(int i = 0;i < numExplosionDebris;i++){
            float angle = rebelrats_combatUtils.calcConeAngle(170,projectile.getFacing() - 180f);
            engine.addHitParticle(point,rebelrats_combatUtils.calcVelDir(angle,rebelrats_combatUtils.randomNumber(300f,400f)),10f,1f,2.5f,new Color(255, 175, 56,255));
        }
    }
}
