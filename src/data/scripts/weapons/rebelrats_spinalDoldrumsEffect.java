package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.combat.rebelrats_addExplosionFx;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_spinalDoldrumsEffect implements EveryFrameWeaponEffectPlugin {
    final Vector2f endSize = new Vector2f(30,30);
    final Vector2f endSize2 = new Vector2f(50,50);
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
        Vector2f size2 = new Vector2f(endSize2.x * scale, endSize2.y * scale);

        rebelrats_addExplosionFx p = new rebelrats_addExplosionFx();
        p.addExplosion("graphics/fx/fog_circle.png",null,size,size,loc,weapon.getShip().getVelocity(),new Color(67,67,222,255),0.05f,0.1f,0,0,angle,1,false);
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
        e.getLocation().set(weapon.getLocation());

        rebelrats_addExplosionFx p2 = new rebelrats_addExplosionFx();
        p2.addExplosion("graphics/fx/explosion3.png",null,size2,size2,loc,weapon.getShip().getVelocity(),new Color(10,10,208,255),0.05f,0.1f,0,0,angle,1f,false);
        CombatEntityAPI e2 = engine.addLayeredRenderingPlugin(p2);
        e2.getLocation().set(weapon.getLocation());
    }
}
