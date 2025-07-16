package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_weaponUtils {
    public static void particleExplosion(int numParticles, float coneAngle, float faceAngle,
                                         CombatEngineAPI engine, Vector2f point, float minSpeed,
                                         float maxSpeed, float size, float brightness,
                                         float duration, Color color){
        for(int i = 0;i < numParticles;i++){
            float angle = rebelrats_combatUtils.calcConeAngle(coneAngle,faceAngle);
            float speed = rebelrats_combatUtils.randomNumber(minSpeed,maxSpeed);

            if(minSpeed == maxSpeed) speed = minSpeed;
            Vector2f velocity = rebelrats_combatUtils.calcVelDir(angle,speed);

            engine.addHitParticle(point,velocity,size,brightness,duration,color);
        }
    }
}
