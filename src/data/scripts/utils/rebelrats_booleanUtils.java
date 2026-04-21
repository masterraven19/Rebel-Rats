package data.scripts.utils;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.ProjectileSpawnType;

public class rebelrats_booleanUtils {
    public static boolean notAvailableEnemyMissile(MissileAPI missile, ShipAPI ship){
        return (missile.isFading() || missile.isExpired() || missile.isFizzling() ||
                ship.getOwner() == missile.getOwner());
    }
    public static boolean notAvailableMissile(MissileAPI missile){
        return (missile.isFading() || missile.isExpired() || missile.isFizzling());
    }
    public static boolean missileFlamedOut(MissileAPI missile){
        if (missile.getEngineController() != null){
            missile.getEngineController().isFlamedOut();
        }
        return false;
    }
}

