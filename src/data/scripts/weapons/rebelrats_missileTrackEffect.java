package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import data.scripts.weapons.rebelrats_missileScript;
import org.magiclib.ai.MagicMissileAI;

public class rebelrats_missileTrackEffect implements OnFireEffectPlugin {

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;
        if (engine.getMissiles().isEmpty()) return;
        if (projectile.isExpired()) return;

        MissileAPI missile = (MissileAPI) projectile;
        MagicMissileAI ai = new MagicMissileAI(missile, weapon.getShip());
        rebelrats_missileScript rat = new rebelrats_missileScript(missile,engine,ai);

        missile.setMissileAI(ai);
        engine.addPlugin(rat);
    }
}
