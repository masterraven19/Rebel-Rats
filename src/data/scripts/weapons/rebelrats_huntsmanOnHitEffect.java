package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_huntsmanOnHitEffect implements OnHitEffectPlugin {
    private int numParticles = 2;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        for (int i = 0;i<numParticles;i++){
            rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
            p.addExplosion("misc","nebula_particles",new Vector2f(50,50),new Vector2f(200,200),point,new Vector2f(0,0),new Color(42,106,170),3.5F,4,1,(float) Math.random() * 1F,0,0.7F,true);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(point);
        }
    }
}
