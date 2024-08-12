package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_addParticle;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_poubelleEffect implements OnHitEffectPlugin, OnFireEffectPlugin,EveryFrameWeaponEffectPlugin {

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
        explosionFx.addExplosion("graphics/fx/explosion0.png",null,new Vector2f(50,50),new Vector2f(200,200),point,new Vector2f(0,0), new Color(61,160,244),0.7F,3,5,0.1F,(float) Math.random() * 180,0.6F,false);
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
        p.addParticle(m,"graphics/rebelrats/fx/target_painter.png",null,50,50,null,new Vector2f(0,0),new Vector2f(0,0),m.getFacing(),0,true,1,m.getMaxFlightTime(),1,0.5F,false, null);
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
