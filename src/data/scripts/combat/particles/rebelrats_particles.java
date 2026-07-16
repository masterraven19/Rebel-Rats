package data.scripts.combat.particles;

import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_particles {

    public static SpriteAPI smokeParticle(){
        return rebelrats_particleUtils.createSprite(
                "misc",
                "dust_particles",
                new Vector2f(20,20),
                0f,
                Color.WHITE,
                4
        );
    }
    public static SpriteAPI mangonelPlasma(){
        return rebelrats_particleUtils.createSprite(
                "fx",
                "rebelrats_mangonel_plasma",
                new Vector2f(80,20),
                0f,
                null
        );
    }

    public static SpriteAPI doldrumsWarning(){
        return rebelrats_particleUtils.createSprite(
                "fx",
                "rebelrats_doldrums_warning",
                new Vector2f(240,80),
                0f,
                new Color(233,91,14,255)
        );
    }
    public static SpriteAPI targetPainter(){
        return rebelrats_particleUtils.createSprite(
                "fx",
                "rebelrats_target_painter",
                new Vector2f(50,50),
                0f,
                null
        );
    }

}
