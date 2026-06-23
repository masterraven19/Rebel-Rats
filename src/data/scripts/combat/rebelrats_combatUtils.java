package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import data.scripts.utils.rebelrats_booleanUtils;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class rebelrats_combatUtils {
    public static final float PI = (float) Math.PI;

    public static Vector2f calcSideMissileLaunch(
            float angle, float launchAngle, float launchSpeed, Vector2f slotloc
    ) {
        float dx;
        float dy;
        Vector2f targetloc;
        if (slotloc.y > 0){
            angle = (angle + launchAngle) * (float) Math.PI / 180;
            dx = (float) Math.cos(angle);
            dy = (float) Math.sin(angle);
            targetloc = new Vector2f(launchSpeed * dx, launchSpeed * dy);
        } else{
            angle = (angle - launchAngle) * (float) Math.PI / 180;
            dx = (float) Math.cos(angle);
            dy = (float) Math.sin(angle);
            targetloc = new Vector2f(launchSpeed * dx, launchSpeed * dy);
        }
        return targetloc;
    }

    public static Vector2f calcVelDir(float angle, float speed) {
        float dx;
        float dy;
        Vector2f targetloc;
        angle = (angle) * (float) Math.PI / 180;
        dx = (float) Math.cos(angle);
        dy = (float) Math.sin(angle);
        targetloc = new Vector2f(speed * dx, speed * dy);

        return targetloc;
    }

    public static Vector2f calcLocWAngle(float angle, float length, Vector2f loc) {
        float dx;
        float dy;
        Vector2f targetloc;
        angle = (angle) * (float) Math.PI / 180;
        dx = (float) Math.cos(angle);
        dy = (float) Math.sin(angle);
        targetloc = new Vector2f(length * dx + loc.x, length * dy + loc.y);

        return targetloc;
    }

    public static float calcConeAngle(float coneArc, float coneFacing) {
        float angle = coneArc * (float)Math.random();
        if (angle > coneArc/2){angle = (float)Math.random() * coneArc/2;}
        else{angle = (float)Math.random() * -coneArc/2;}
        angle = coneFacing - angle;
        return angle;
    }

    public static float flipAngle(float angle) {
        float flippedAngle = (angle + 180) % 360;
        return convertTo180Domain(flippedAngle);
    }

    public static float convertTo180Domain(float angle) {
        if (angle > 180)
            return angle - 360;
        else if (angle <= -180)
            return angle + 360;
        return angle;
    }

    public static float findReflectedAngle(float angleIncidence, float normal) {
        float difference = normal - angleIncidence;
        return convertTo180Domain(normal + difference);
    }

    public static float calcDirectionOfTwoPoints(Vector2f origin, Vector2f target) {
        float angle;
        float dx = origin.x - target.x;
        float dy = origin.y - target.y;
        angle = (float) Math.atan2(dx,dy) * -180/(float)Math.PI;
        if(angle < -90){angle += 270;}
        else{angle -= 90;}
        return angle;
    }

    public static float randomNumber(float min, float max) {
        float r = (float)Math.random() * ((max - min + 1) + min);
        return r;
    }

    public static MissileAPI getClosestMissile(
            CombatEngineAPI engine, ShipAPI ship, WeaponAPI weapon, float startRange
    ) {
        float range = startRange;
        for (MissileAPI m : engine.getMissiles()){
            if(rebelrats_booleanUtils.isNotEnemyMissile(m,ship)) continue;

            float d = MathUtils.getDistance(weapon.getLocation(),m.getLocation());
            if(d > weapon.getRange())continue;
            if(d < range){
                range = d;
            }
        }
        return null;
    }

    /**
     * Predicts entities future location according to its linear and angular velocity.
     * note: accuracy not fully tested
     */
    public static Vector2f predictLinearAndAngular(
            CombatEntityAPI target, float time
    ) {
        Vector2f currentLocation = target.getLocation();
        Vector2f velocity = target.getVelocity();
        float angularVelocity = target.getAngularVelocity();

        float speed = velocity.length();
        Vector2f combinedVelocity = new Vector2f(
                speed * (cos(angularVelocity) * time),
                speed * (sin(angularVelocity) * time)
        );

        return new Vector2f(
                currentLocation.x + combinedVelocity.x * time,
                currentLocation.y + combinedVelocity.y * time
        );
    }

    public static Vector2f applyRotationMatrix(float angle, Vector2f position) {
        return new Vector2f(
                cos(angle) * position.x - sin(angle) * position.y,
                cos(angle) * position.x + sin(angle) * position.y
        );
    }

    public static float sin(float angle) {
        return (float) Math.sin(angle);
    }

    public static float cos(float angle) {
        return (float) Math.cos(angle);
    }

    public static float toRadians(float angle) {
        return (float) Math.toRadians(angle);
    }

    public static void logMessage(Class<?> currentClass, String message) {
        Logger log = Global.getLogger(currentClass);
        log.info(message);
    }

    public static void logMessageCombat(CombatEngineAPI engine,
                                        ShipAPI ship,
                                        String message) {
        engine.addFloatingText(
                engine.getViewport().getCenter(),
                message,
                20f,
                Color.RED,
                ship,
                0f,
                0f
                );
    }
}
