package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_sideMissileScript implements OnFireEffectPlugin {

    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        if (!(projectile instanceof MissileAPI)) return;

        MissileAPI m = (MissileAPI) projectile;
        m.getVelocity().set(0,0);
        float launchspeed = m.getSpec().getLaunchSpeed();
        Vector2f vel = calculateloc(m.getFacing(),90,launchspeed,weapon.getSlot().getLocation());
        m.getVelocity().set(vel);
    }
    public Vector2f calculateloc(float angle, float launchAngle, float launchSpeed, Vector2f slotloc){
        float dx;
        float dy;
        Vector2f targetloc;
        if (slotloc.y > 0){
            angle = (angle + launchAngle) * (float) Math.PI / 180;
            dx = (float) Math.cos(angle);
            dy = (float) Math.sin(angle);
            targetloc = new Vector2f(launchSpeed * dx, launchSpeed * dy);
        } else{
            angle = (angle - launchAngle) * (float) Math.PI / 180;
            dx = (float) Math.cos(angle);
            dy = (float) Math.sin(angle);
            targetloc = new Vector2f(launchSpeed * dx, launchSpeed * dy);
        }
        return targetloc;
    }
}
