package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_kingmakerEffect implements OnHitEffectPlugin, EveryFrameWeaponEffectPlugin, OnFireEffectPlugin {
    private float elapsed = 0;
    private boolean fired = false;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

    }
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        if (!fired) {
            weapon.getAnimation().setFrame(0);
            return;
        }else{
            elapsed += amount;
        }

        float f = weapon.getAnimation().getNumFrames() / weapon.getAnimation().getFrameRate();
        weapon.getAnimation().play();

        if (weapon.getAnimation().getFrame() >= 6 && weapon.getAnimation().getFrame() < 37){
            float angle = rebelrats_combatUtils.calcConeAngle(30,0);
            Vector2f vel = rebelrats_combatUtils.calcVelDir((weapon.getCurrAngle() - 180) + angle, 200);
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle() - 180, 32, weapon.getLocation());
            float range = (1 - 0.5F) + 1;
            float alphaMult = (float)(Math.random() * range) + 0.5F;

            rebelrats_addParticle p = new rebelrats_addParticle();
            p.addParticle(null,"misc", "nebula_particles",20,20,loc,new Vector2f(0,0),vel,angle,1,false,0,0.2F,alphaMult,0.5F,true,new Color(141,95,240,255));
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(weapon.getLocation());
        }

        if (elapsed > f) {
            fired = !fired;
            elapsed -= f;
        }
    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        fired = true;

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(projectile,"graphics/fx/doldrums_warning.png",null,240,80,null,new Vector2f(80,10),new Vector2f(0,0),0,0,false,0,15,1,0.5F,false, new Color(233,91,14,255));
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
