package data.scripts.weapons;

import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;


public class rebelrats_huntsmanLaunchEffect implements OnFireEffectPlugin,EveryFrameWeaponEffectPlugin {
    private WeaponAPI decor;
    private float elapsed = 0;
    private float elapsed2 = 0;
    private float chargedown = 0;
    private float firedur = 0;
    private int frame = 0;
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        MissileAPI m = (MissileAPI) projectile;
        m.getVelocity().set(0,0);

        float launchspeed = m.getSpec().getLaunchSpeed();
        Vector2f vel = rebelrats_combatUtils.calcSideMissileLaunch(m.getFacing(),90,launchspeed,weapon.getSlot().getLocation());
        m.getVelocity().set(vel);
    }

    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        if (decor == null){
            List<WeaponAPI> weapons = weapon.getShip().getAllWeapons();
            for (WeaponAPI w: weapons){
                if (w.getId().matches("rebelrats_jerboahatch")){
                    decor = w;
                }
            }
            chargedown = weapon.getSpec().getChargeTime();
            firedur = weapon.getDerivedStats().getRoF() * weapon.getSpec().getBurstSize();
        }
        if (!weapon.isFiring()) return;
        elapsed += amount;
        elapsed2 += amount;

        float rate = 1 / decor.getAnimation().getFrameRate();
        int max = decor.getAnimation().getNumFrames();

        if (elapsed > rate){
            if (elapsed2 < (chargedown + firedur)){
                if (frame < max - 1){
                    frame = frame + 1;
                }
            }
            if (elapsed2 > (chargedown + firedur)){
                if (weapon.getCooldownRemaining() == 0 && weapon.getCooldownRemaining() < 0.1f){
                    elapsed2 -= (chargedown + firedur);
                }
                if (frame > 0){
                    frame = frame - 1;
                }
            }
            if (weapon.getChargeLevel() == 1){
                frame = max - 1;
            }
            elapsed -= rate;
        }
        
        decor.getAnimation().setFrame(frame);
    }
}
