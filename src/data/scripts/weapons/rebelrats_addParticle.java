package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.EnumSet;

public class rebelrats_addParticle extends BaseCombatLayeredRenderingPlugin {
    private CombatEntityAPI proj;
    private CombatEngineAPI e;
    private SpriteAPI sprite;
    private Vector2f loc;
    private boolean pulseInOut;
    private float spriteWidth;
    private float spriteHeight;
    private float angle;
    private float angleRotation;
    private float pulseDur;
    private float pulseMin;
    private float pulseMax;
    private float scale = 1;
    private float scaleincrease = 1;
    private float elapsed = 0;
    private float elapsed2 = 0;
    private float spriteDur;
    public void addParticle(CombatEntityAPI proj, CombatEngineAPI e, String spriteName,
                            float spriteWidth, float spriteHeight, Vector2f loc,
                            float angle, float angleRotation, boolean pulseInOut,
                            float pulseDur, float spriteDur){
        if (proj != null){this.proj = proj;}
        if (loc != null){this.loc = loc;}
        this.e = e;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.angle = angle;
        this.angleRotation = angleRotation;
        this.pulseInOut = pulseInOut;
        this.pulseDur = pulseDur;
        this.spriteDur = spriteDur;
        this.sprite = Global.getSettings().getSprite(spriteName);
        pulseMin = (spriteWidth * 0.7F);
        pulseMax = (spriteWidth * 1);
        scaleincrease = 0.01F;
        scale = 1F;

        sprite.setWidth(spriteWidth);
        sprite.setHeight(spriteHeight);
        sprite.setAngle(angle);
        sprite.setNormalBlend();

    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;
        elapsed2 += amount;


        if (proj != null){
            entity.getLocation().set(proj.getLocation());
        }else{
            entity.getLocation().set(loc);
        }
        sprite.setAngle(sprite.getAngle() + angleRotation);

        if (pulseInOut){
            elapsed += amount;
            if (elapsed < pulseDur){
                if (scale < 1){
                    scale += scaleincrease;
                }
            }

            if (elapsed > pulseDur){
                if (scale > 0.7){
                    scale -= scaleincrease;
                }else{
                    elapsed -= pulseDur;
                }
            }
        }
    }
    public float getRenderRadius() {
        return 300;
    }

    public void render(CombatEngineLayers layer, ViewportAPI viewport) {
        //sprite.setSize(spriteWidth,spriteHeight);
        sprite.setAlphaMult(1);
        sprite.setSize(spriteWidth * scale,spriteHeight * scale);

        if (proj != null){
            sprite.renderAtCenter(proj.getLocation().x,proj.getLocation().y);
        }else{
            sprite.renderAtCenter(loc.x,loc.y);
        }
    }
    protected EnumSet<CombatEngineLayers> layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);

    public EnumSet<CombatEngineLayers> getActiveLayers() {return layers;}

    public boolean isExpired() {
        if (proj != null) {
            return proj.isExpired() || !Global.getCombatEngine().isEntityInPlay(proj);
        }else{
            if (elapsed > spriteDur){
                return true;
            }
        }
        return false;
    }
    public void init(CombatEntityAPI entity) {
        this.entity = entity;
    }
}
