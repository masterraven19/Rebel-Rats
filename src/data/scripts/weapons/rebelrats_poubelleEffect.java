package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;
import data.scripts.weapons.rebelrats_combatUtils;

public class rebelrats_poubelleEffect implements OnHitEffectPlugin, OnFireEffectPlugin,EveryFrameWeaponEffectPlugin {

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

    }

    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;

        MissileAPI m = (MissileAPI) projectile;
        m.getVelocity().set(0,0);

        float launchspeed = m.getSpec().getLaunchSpeed();
        Vector2f vel = rebelrats_combatUtils.calculateloc(m.getFacing(),90,launchspeed,weapon.getSlot().getLocation());
        m.getVelocity().set(vel);

        rebelrats_addParticle p = new rebelrats_addParticle();
        p.addParticle(m,engine,"graphics/fx/target_painter.png",50,50,null,m.getFacing(),0,true,1,m.getMaxFlightTime());
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(projectile.getLocation());
    }
}
