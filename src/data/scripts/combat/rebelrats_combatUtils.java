package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import data.scripts.utils.rebelrats_booleanUtils;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.Iterator;
import java.util.List;


public class rebelrats_combatUtils {
    public static Vector2f calcSideMissileLaunch(float angle, float launchAngle, float launchSpeed, Vector2f slotloc){
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
    public static Vector2f calcVelDir(float angle, float speed){
        float dx;
        float dy;
        Vector2f targetloc;
        angle = (angle) * (float) Math.PI / 180;
        dx = (float) Math.cos(angle);
        dy = (float) Math.sin(angle);
        targetloc = new Vector2f(speed * dx, speed * dy);

        return targetloc;
    }
    public static Vector2f calcLocWAngle(float angle, float length, Vector2f loc){
        float dx;
        float dy;
        Vector2f targetloc;
        angle = (angle) * (float) Math.PI / 180;
        dx = (float) Math.cos(angle);
        dy = (float) Math.sin(angle);
        targetloc = new Vector2f(length * dx + loc.x, length * dy + loc.y);

        return targetloc;
    }
    public static float calcConeAngle(float coneAngle, float faceAngle){
        float angle = coneAngle * (float)Math.random();
        if (angle > coneAngle/2){angle = (float)Math.random() * coneAngle/2;}
        else{angle = (float)Math.random() * -coneAngle/2;}
        angle = faceAngle - angle;
        return angle;
    }
    public static float calcDirectionOfTwoPoints(Vector2f origin, Vector2f target){
        float angle;
        float dx = target.x - origin.x;
        float dy = target.y - origin.y;
        angle = (float) Math.atan2(dx,dy) * -180/(float)Math.PI;
        if(angle < -90){angle += 270;}
        else{angle -= 90;}
        return angle;
    }
    public static float randomNumber(float min, float max){
        float r = (float)Math.random() * ((max - min + 1) + min);
        return r;
    }
    public static MissileAPI getClosestMissile(
            CombatEngineAPI engine, ShipAPI ship, WeaponAPI weapon, float startRange
    ){
        float range = startRange;
        for (MissileAPI m : engine.getMissiles()){
            if(rebelrats_booleanUtils.notAvailableEnemyMissile(m,ship)) continue;

            float d = MathUtils.getDistance(weapon.getLocation(),m.getLocation());
            if(d > weapon.getRange())continue;
            if(d < range){
                range = d;
            }
        }
        return null;
    }
    public static Vector2f getFuturePosition(
            CombatEntityAPI target, float time
    ){
        Vector2f currentLocation = target.getLocation();
        Vector2f velocity = target.getVelocity();
        return new Vector2f(
                currentLocation.x + (velocity.x * time),
                currentLocation.y + (velocity.y * time)
        );
    }
    public static void logMessage(Class<?> currentClass, String message){
        Logger log = Global.getLogger(currentClass);
        log.warn(message);
    }
    public static boolean isPointInPoints(List<Vector2f> points, Vector2f point){
        Iterator<Vector2f> iterator = points.iterator();
        float minX = 0;
        float maxX = 0;
        float minY = 0;
        float maxY = 0;
        while(iterator.hasNext()){
            Vector2f vector = iterator.next();
            minX = Math.min(minX,vector.getX());
            maxX = Math.max(maxX,vector.getX());
            minY = Math.min(minY,vector.getY());
            maxY = Math.max(maxY,vector.getY());
        }
        if (point.x < minX || point.x > maxX || point.y < minY || point.y > maxY){
            return false;
        }
        return true;
    }
}
