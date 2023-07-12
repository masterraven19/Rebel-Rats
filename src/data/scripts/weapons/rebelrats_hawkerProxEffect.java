package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;

public class rebelrats_hawkerProxEffect implements ProximityExplosionEffect {
    protected float numshrapnel = 20;
    protected float cone = 30;
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        for (int i = 0; i < numshrapnel; i++){
            CombatEntityAPI p = engine.spawnProjectile(originalProjectile.getSource(), null, "rebelrats_hawker_munition", originalProjectile.getLocation(), originalProjectile.getFacing(), originalProjectile.getSource().getVelocity());
            float angle = cone * (float)Math.random();
            if (angle > cone/2){angle = (float)Math.random() * cone/2;}
            else{angle = (float)Math.random() * -cone/2;}
            angle = originalProjectile.getFacing() - angle;
            p.setFacing(angle);
        }
    }
}
