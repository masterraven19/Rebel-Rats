package data.scripts.combat;

import org.lwjgl.util.vector.Vector2f;


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
}
