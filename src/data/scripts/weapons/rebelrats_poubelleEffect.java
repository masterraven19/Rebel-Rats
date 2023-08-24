package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import data.scripts.weapons.rebelrats_combatUtils;

import java.awt.*;

public class rebelrats_poubelleEffect implements OnHitEffectPlugin, OnFireEffectPlugin,EveryFrameWeaponEffectPlugin {

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
        explosionFx.addExplosion("graphics/fx/explosion0.png",new Vector2f(50,50),new Vector2f(200,200),point, new Color(61,160,244),0.7F,3,5,0.1F,0.6F);
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
        e.getLocation().set(point);
    }

    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;

        MissileAPI m = (MissileAPI) projectile;
        m.getVelocity().set(0,0);

        float launchspeed = m.getSpec().getLaunchSpeed();
        Vector2f vel = rebelrats_combatUtils.calcSideMissileLaunch(m.getFacing(),90,launchspeed,weapon.getSlot().getLocation());
        m.getVelocity().set(vel);

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(m,"graphics/fx/target_painter.png",null,50,50,null,new Vector2f(0,0),m.getFacing(),0,true,1,m.getMaxFlightTime(),0.5F,false, null);
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
