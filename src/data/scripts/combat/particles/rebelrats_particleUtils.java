package data.scripts.combat.particles;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatLayeredRenderingPlugin;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class rebelrats_particleUtils {
    public static void addParticlePlugin(
            CombatLayeredRenderingPlugin particle, Vector2f location
    ){
        CombatEngineAPI engine = Global.getCombatEngine();
        CombatEntityAPI e = engine.addLayeredRenderingPlugin(particle);
        e.getLocation().set(location);
    }
    public static SpriteAPI createSprite(
            String category, String key, Vector2f size, float angle, Color color
    ){
        SpriteAPI sprite = Global.getSettings().getSprite(category, key, true);

        if (color != null){
            sprite.setColor(color);
        }
        sprite.setSize(size.x, size.y);
        sprite.setAngle(angle);

        sprite.setNormalBlend();
        return sprite;
    }
    public static SpriteAPI createSprite(
            String category, String key, Vector2f size, float angle, Color color,
            int multipleSpriteGridSize
    ){
        SpriteAPI sprite = Global.getSettings().getSprite(category, key, true);

        if (color != null){
            sprite.setColor(color);
        }
        sprite.setSize(size.x, size.y);
        sprite.setAngle(angle);

        float ratio = 1f/multipleSpriteGridSize;
        float i = Misc.random.nextInt(multipleSpriteGridSize);
        float j = Misc.random.nextInt(multipleSpriteGridSize);
        sprite.setTexWidth(ratio);
        sprite.setTexHeight(ratio);
        sprite.setTexX(i * ratio );
        sprite.setTexY(j * ratio );

        sprite.setNormalBlend();
        return sprite;
    }
}
