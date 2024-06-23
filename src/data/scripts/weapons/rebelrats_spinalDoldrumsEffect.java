package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_spinalDoldrumsEffect implements EveryFrameWeaponEffectPlugin {
    final Vector2f endSize = new Vector2f(30,30);
    private float elapsed = 0;
    private float scale = 0;
    private float angle = 0;
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) return;
        if (weapon.getCooldownRemaining() > 0) return;
        if (!(weapon.getChargeLevel() > 0)) return;

        elapsed += amount;
        float chargetime = weapon.getOriginalSpec().getChargeTime();
        Vector2f loc = rebelrats_combatUtils.calcLocWAngle(weapon.getCurrAngle(), 50, weapon.getLocation());

        angle += 2;
        if (angle > 360) {angle = 0;}
        
        if (elapsed < chargetime * 0.7 && scale < 1){
            scale += amount;
        }
        if (elapsed > chargetime * 0.7 && scale > 0){
            scale -= 0.1f;
        }
        if (elapsed > chargetime){
            elapsed -= chargetime;
            scale = 0;
        }

        Vector2f size = new Vector2f(endSize.x * scale, endSize.y * scale);

        rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
        p.addExplosion("graphics/fx/ratshieldouter7.png",null,size,size,loc,weapon.getShip().getVelocity(),new Color(67,67,222,255),0.05f,0.1f,0,0,angle,1,false);
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(weapon.getLocation());
    }
}
