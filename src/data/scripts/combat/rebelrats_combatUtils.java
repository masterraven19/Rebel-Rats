package data.scripts.combat;

import org.lwjgl.util.vector.Vector2f;

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
    }/* too laggy
    public static boolean isPointInPoints(List<Vector2f> points, Vector2f point){
        float minX = 0;
        float maxX = 0;
        float minY = 0;
        float maxY = 0;
        for(Vector2f v: points){
            minX = Math.min(minX,v.getX());
            maxX = Math.max(maxX,v.getX());
            minY = Math.min(minY,v.getY());
            maxY = Math.max(maxY,v.getY());
        }
        if (point.x < minX || point.x > maxX || point.y < minY || point.y > maxY){
            return false;
        }
        return true;
    }*/
}
