package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

import java.util.ArrayList;

public class rebelrats_targetinglaserEffect implements EveryFrameWeaponEffectPlugin {
    WeaponAPI currentWeapon = null;
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        if(currentWeapon == null){
            for(WeaponAPI w : weapon.getShip().getAllWeapons()){
                if(w.getId().equals("rebelrats_doldrums_l")){
                    currentWeapon = w;
                }
            }
        }

        if(currentWeapon.getId().equals("rebelrats_doldrums_l")){
            weapon.setCurrAngle(currentWeapon.getCurrAngle());
            if (currentWeapon.getChargeLevel() > 0 && !(currentWeapon.getCooldownRemaining() > 0)){
                weapon.setForceFireOneFrame(true);
            }else{
                weapon.setForceFireOneFrame(false);
            }
        }

    }
}
