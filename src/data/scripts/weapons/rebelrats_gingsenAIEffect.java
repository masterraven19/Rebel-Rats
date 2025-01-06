package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import data.scripts.combat.rebelrats_gingsenCloudEffect;

public class rebelrats_gingsenAIEffect implements OnFireEffectPlugin {

    @Override
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if(!(projectile instanceof MissileAPI)) return;

        MissileAPI missile = (MissileAPI) projectile;
        rebelrats_gingsenCloudEffect effect = new rebelrats_gingsenCloudEffect(missile);
        engine.addPlugin(effect);
    }
}
