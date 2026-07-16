package data.scripts.utils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;

public class rebelrats_booleanUtils {

    public static boolean isEntityAlive(CombatEntityAPI proj){
        CombatEngineAPI engine = Global.getCombatEngine();
        return proj.isExpired() || !engine.isEntityInPlay(proj);
    }

    public static boolean isNotEnemyMissile(MissileAPI missile, ShipAPI ship) {
        return isNotAliveMissile(missile) || ship.getOwner() == missile.getOwner() ;
    }

    public static boolean isNotAliveMissile(MissileAPI missile) {
        return (missile.isFading() || missile.isExpired() || missile.isFizzling());
    }

    public static boolean missileFlamedOut(MissileAPI missile) {
        if (missile.getEngineController() != null){
            return missile.getEngineController().isFlamedOut();
        }
        return false;
    }

    public static boolean isNotAliveShip(ShipAPI ship) {
        return !ship.isAlive() || ship.isHulk();
    }

    public static boolean isAllyShip(ShipAPI ship1, ShipAPI ship2) {
        return ship1.getOwner() == ship2.getOwner();
    }

    public static boolean isAllyShip(WeaponAPI weapon, ShipAPI ship2) {
        return weapon.getShip().getOwner() == ship2.getOwner();
    }
}

