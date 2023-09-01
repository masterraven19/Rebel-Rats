package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.EnumSet;
//Will create a single particle when used. Make sure to assign it as a CombatEntity as
//"engine.addLayeredPlugin(copy of this class)"
//Will follow the projectile when given one, will stay in place when given a Vector2f
// example
//rebelrats_addParticle p = new rebelrats_addParticle();
//p.addParticle(m,"graphics/fx/target_painter.png",50,50,null,new Vector2f(0,0),m.getFacing(),0,true,1,m.getMaxFlightTime(),false, null);
//CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
//e.getLocation().set(projectile.getLocation());
public class rebelrats_addParticle extends BaseCombatLayeredRenderingPlugin {
    private CombatEntityAPI proj;
    private SpriteAPI sprite;
    private Vector2f loc;
    private Vector2f vel;
    private FaderUtil fader;
    private Color color;
    private boolean pulseInOut;
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
    private float fadeOutDur = 0;
    public void addParticle(CombatEntityAPI proj, String spriteNameOrCategory, String spriteKey,
                            float spriteWidth, float spriteHeight, Vector2f loc,
                            Vector2f vel,
                            float angle, float angleRotation, boolean pulseInOut,
                            float pulseDur, float spriteDur, float fadeOutDur,
                            boolean multipleSprites,
                            Color color){

        if (proj != null) {
            this.proj = proj;
            this.loc = proj.getLocation();
        } else {
            if (loc != null) {
                this.loc = loc;
            }
        }

        if (spriteNameOrCategory != null && spriteKey != null){
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory,spriteKey,true);
        }else{
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory);
        }

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.angle = angle;
        this.vel = vel;
        this.angleRotation = angleRotation;
        this.pulseInOut = pulseInOut;
        this.pulseDur = pulseDur;
        this.spriteDur = spriteDur;
        this.fadeOutDur = fadeOutDur;
        if (color != null){
            this.color = color;
            sprite.setColor(color);
        }
        pulseMin = (spriteWidth * 0.7F);
        pulseMax = (spriteWidth * 1);
        scaleincrease = 0.01F;
        scale = 1F;
        fader = new FaderUtil(1F,fadeOutDur);
        fader.fadeOut();

        if (multipleSprites){
            float i = Misc.random.nextInt(4);
            float j = Misc.random.nextInt(4);
            sprite.setTexWidth(0.25f);
            sprite.setTexHeight(0.25f);
            sprite.setTexX(i * 0.25f);
            sprite.setTexY(j * 0.25f);
        }
        sprite.setWidth(spriteWidth);
        sprite.setHeight(spriteHeight);
        sprite.setAngle(angle);
        sprite.setNormalBlend();

    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;
        elapsed2 += amount;

        if (elapsed2 >= (spriteDur * 0.9) - fadeOutDur){
            fader.advance(amount);
        }

        entity.getLocation().set(loc.x += vel.x * amount, loc.y += vel.y * amount);

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

        sprite.renderAtCenter(loc.x, loc.y);
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
