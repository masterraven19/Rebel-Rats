package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import data.scripts.combat.rebelrats_combatUtils;

public class rebelrats_hawkerProxEffect implements ProximityExplosionEffect {
    protected float numshrapnel = 20;
    protected float cone = 30;
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        for (int i = 0; i < numshrapnel; i++){
            CombatEntityAPI p = engine.spawnProjectile(originalProjectile.getSource(), null, "rebelrats_hawker_munition", originalProjectile.getLocation(), originalProjectile.getFacing(), originalProjectile.getSource().getVelocity());
            float angle = rebelrats_combatUtils.calcConeAngle(cone, originalProjectile.getFacing());
            p.setFacing(angle);
        }
    }
}
