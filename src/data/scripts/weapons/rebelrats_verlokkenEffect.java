package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import data.scripts.combat.rebelrats_verlokkenScript;
import org.magiclib.ai.MagicMissileAI;

public class rebelrats_verlokkenEffect implements OnFireEffectPlugin {
    @Override
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if(!(projectile instanceof MissileAPI)) return;
        MissileAPI missile = (MissileAPI) projectile;
        MagicMissileAI ai = new MagicMissileAI(missile,missile.getSource());
        rebelrats_verlokkenScript rat = new rebelrats_verlokkenScript(engine,missile,ai,"track");
        engine.addPlugin(rat);
    }
}
