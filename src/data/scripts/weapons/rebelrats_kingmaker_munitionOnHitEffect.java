package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_kingmaker_munitionOnHitEffect implements OnHitEffectPlugin {
    private int numShrap = 15;
    private int numEmp = 10;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (!(target instanceof ShipAPI)) return;
        for (int i = 0; i < numShrap; i++) {
            Vector2f loc = rebelrats_combatUtils.calcLocWAngle(projectile.getFacing() - 180, 60, point);
            CombatEntityAPI p = engine.spawnProjectile(projectile.getSource(), null, "rebelrats_railgun_shrapnel", loc, projectile.getFacing(), projectile.getSource().getVelocity());
            float angle = rebelrats_combatUtils.calcConeAngle(180,projectile.getFacing() - 180);
            p.setFacing(angle);
        }
        for (int i = 0; i < numEmp; i++) {
            engine.spawnEmpArcPierceShields(
                    projectile.getSource(), point, target, target,
                    DamageType.ENERGY,
                    0, // damage
                    0, // emp
                    100000f, // max range
                    "tachyon_lance_emp_impact",
                    30f, // thickness
                    //new Color(25,100,155,255),
                    //new Color(255,255,255,255)
                    new Color(73, 171, 255, 255),
                    new Color(255, 255, 255, 255)
            );
        }
    }
}
