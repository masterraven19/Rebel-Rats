package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import org.lwjgl.util.vector.Vector2f;

import java.util.EnumSet;
//Will create a single particle when used. Make sure to assign it as a CombatEntity as
//"engine.addLayeredPlugin(copy of this class)"
//Will follow the projectile when given one, will stay in place when given a Vector2f
public class rebelrats_addParticle extends BaseCombatLayeredRenderingPlugin {
    private CombatEntityAPI proj;
    private SpriteAPI sprite;
    private Vector2f loc;
    private Vector2f fixedLoc;
    private FaderUtil fader;
    private boolean pulseInOut;
    private boolean trail;
    private boolean hasRendered = false;
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
    private float spriteDur = 0;
    public void addParticle(CombatEntityAPI proj, String spriteName,
                            float spriteWidth, float spriteHeight, Vector2f loc,
                            float angle, float angleRotation, boolean pulseInOut,
                            float pulseDur, float spriteDur, boolean trail){

        if (proj != null) {
            this.proj = proj;
            this.loc = proj.getLocation();
            fixedLoc = proj.getLocation();
        } else {
            if (loc != null) {
                this.loc = loc;
                fixedLoc = loc;
            }
        }

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.angle = angle;
        this.angleRotation = angleRotation;
        this.pulseInOut = pulseInOut;
        this.pulseDur = pulseDur;
        this.spriteDur = spriteDur;
        this.trail = trail;
        this.sprite = Global.getSettings().getSprite(spriteName);
        pulseMin = (spriteWidth * 0.7F);
        pulseMax = (spriteWidth * 1);
        scaleincrease = 0.01F;
        scale = 1F;
        fader = new FaderUtil(1F,0.5F);
        fader.fadeOut();

        sprite.setWidth(spriteWidth);
        sprite.setHeight(spriteHeight);
        sprite.setAngle(angle);
        sprite.setNormalBlend();

    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;
        elapsed2 += amount;

        if (elapsed2 >= spriteDur * 0.9){
            fader.advance(amount);
        }

        if (!trail){
            entity.getLocation().set(loc);
        }
        else{
            entity.getLocation().set(fixedLoc);
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
        sprite.setAlphaMult(fader.getBrightness());
        sprite.setSize(spriteWidth * scale,spriteHeight * scale);

        if (!trail) {
            sprite.renderAtCenter(loc.x, loc.y);
        }else{
            sprite.renderAtCenter(fixedLoc.x,fixedLoc.y);
        }
    }
    protected EnumSet<CombatEngineLayers> layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);

    public EnumSet<CombatEngineLayers> getActiveLayers() {return layers;}

    public boolean isExpired() {
        if (proj != null) {
            return proj.isExpired() || !Global.getCombatEngine().isEntityInPlay(proj);
        }else{
            if (elapsed2 > spriteDur){
                return true;
            }
        }
        return false;
    }
    public void init(CombatEntityAPI entity) {
        this.entity = entity;
    }
}
