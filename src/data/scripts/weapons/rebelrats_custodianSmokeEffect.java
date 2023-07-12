package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class rebelrats_custodianSmokeEffect implements ProximityExplosionEffect {
    public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
        CombatEngineAPI engine = Global.getCombatEngine();
        engine.addSmokeParticle(originalProjectile.getLocation(),new Vector2f(),40F,1F,0.8F, Color.BLACK);
    }
}
