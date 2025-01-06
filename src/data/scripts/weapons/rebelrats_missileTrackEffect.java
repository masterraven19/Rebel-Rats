package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import data.scripts.combat.rebelrats_missileProxy;
import data.scripts.combat.rebelrats_missileScript;
import org.magiclib.ai.MagicMissileAI;
//hwacha, rattenjager and snapper
public class rebelrats_missileTrackEffect implements OnFireEffectPlugin {
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;
        if (projectile.isExpired()) return;

        if (projectile.getWeapon().getId().contains("hwacha")){
            rebelrats_missileProxy p = new rebelrats_missileProxy((MissileAPI) projectile, engine);
            engine.addPlugin(p);
        }else{
            MissileAPI missile = (MissileAPI) projectile;
            MagicMissileAI ai = new MagicMissileAI(missile, weapon.getShip());
            rebelrats_missileScript rat = new rebelrats_missileScript(missile,engine,ai);

            missile.setMissileAI(ai);
            engine.addPlugin(rat);
        }
    }
}
