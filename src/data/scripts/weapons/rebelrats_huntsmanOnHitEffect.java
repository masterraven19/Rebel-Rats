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
    private int numShrapRing = 30;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        for (int i = 0;i<numParticles;i++){
            rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();;
            p.addExplosion("misc","nebula_particles",new Vector2f(50,50),new Vector2f(200,200),point,new Vector2f(0,0),new Color(42,106,170),2f,3f,1.3f,0,0,0.7F,true);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(point);
        }

        float faceAngle = projectile.getFacing() - 180f;
        Color missileColor = new Color(255, 255, 255,255);
        rebelrats_effectsFactory.particleExplosion(numMissileDebris,170f,faceAngle,
                engine,point,300f,400f,10f,1f,2.5f,missileColor);

        Color shrapColor = new Color(200,200,200,255);
        float shrapSize = rebelrats_combatUtils.randomNumber(10f,12f);
        rebelrats_effectsFactory.particleExplosion(numShrapRing,170f,faceAngle,
                engine,point,160f,160f,shrapSize,1f,1f,shrapColor);

        if(shieldHit) return;

        Color explosionColor = new Color(255, 175, 56,255);
        rebelrats_effectsFactory.particleExplosion(numExplosionDebris,170f,faceAngle,
                engine,point,300f,400f,10f,1f,2.5f,explosionColor);

        rebelrats_effectsFactory.particleExplosion(numShrapRing,170f,faceAngle,
                engine,point,160f,160f,shrapSize,1f,1f,shrapColor);
    }
}
