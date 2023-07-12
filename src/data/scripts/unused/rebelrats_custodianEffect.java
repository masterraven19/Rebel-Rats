package data.scripts.unused;

import com.fs.starfarer.api.combat.*;


public class rebelrats_custodianEffect implements EveryFrameWeaponEffectPlugin, OnFireEffectPlugin{

    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {

    }

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        rebelrats_custodianProxEffect prox = new rebelrats_custodianProxEffect(projectile, weapon);
        engine.addPlugin(prox);
    }
}
