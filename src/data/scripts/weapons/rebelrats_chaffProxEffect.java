package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;

public class rebelrats_chaffProxEffect implements ProximityExplosionEffect {
    protected static float numflak = 10;
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        for (int i = 0; i < numflak; i++) {
            float angle = (float) Math.random() * 360;
            CombatEntityAPI p = engine.spawnProjectile(originalProjectile.getSource(), null, "rebelrats_chaff_munition", originalProjectile.getLocation(), angle, originalProjectile.getSource().getVelocity());
            p.getVelocity().scale((float)Math.random());
        }
    }
}
