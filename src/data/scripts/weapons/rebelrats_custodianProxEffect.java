package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_custodianProxEffect implements ProximityExplosionEffect {
    protected static float numflak = 10;
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if (originalProjectile.getWeapon() == null){
            engine.addSmokeParticle(originalProjectile.getLocation(),new Vector2f(),40F,1F,1.52F, new Color(156,84,45,255));
            return;
        }
        if (originalProjectile.getWeapon().getId().matches("rebelrats_custodian_m")){
            for (int i = 0; i < numflak; i++) {
                float angle = (float) Math.random() * 360;
                CombatEntityAPI p = engine.spawnProjectile(originalProjectile.getSource(), null, "rebelrats_custodian_munition", originalProjectile.getLocation(), angle, originalProjectile.getSource().getVelocity());
                p.getVelocity().scale((float)Math.random());
            }
        }
    }
}
