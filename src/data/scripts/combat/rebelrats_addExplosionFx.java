package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.EnumSet;
//adds big boom
public class rebelrats_addExplosionFx extends BaseCombatLayeredRenderingPlugin {
    private SpriteAPI sprite;
    private FaderUtil fader;
    private Vector2f sizeStart;
    private Vector2f sizeEnd;
    private Vector2f currSize;
    private Vector2f loc;
    private Color color;
    private float fadeOutDur;
    private float spriteDur;
    private float flatIncreaseRate;
    private float angleRotation;
    private float alphaMult;
    private float elapsed;
    //private float elapsed2;
    public void addExplosion(String spriteNameOrCategory, String spriteKey, Vector2f sizeStart, Vector2f sizeEnd,
                             Vector2f loc, Color color, float fadeOutDur, float spriteDur,
                             float flatIncreaseRate, float angeRotation,float angle, float alphaMult, boolean multipleSprites){

        if (spriteNameOrCategory != null && spriteKey != null){
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory,spriteKey,true);
        }else{
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory);
        }
        this.sizeStart = sizeStart;
        this.sizeEnd = sizeEnd;
        this.loc = loc;
        this.spriteDur = spriteDur;
        this.fadeOutDur = fadeOutDur;
        this.flatIncreaseRate = flatIncreaseRate;
        this.alphaMult = alphaMult;
        this.angleRotation = angeRotation;
        this.color = color;

        if (multipleSprites){
            float i = Misc.random.nextInt(4);
            float j = Misc.random.nextInt(4);
            sprite.setTexWidth(0.25f);
            sprite.setTexHeight(0.25f);
            sprite.setTexX(i * 0.25f);
            sprite.setTexY(j * 0.25f);
        }
        sprite.setSize(sizeStart.x,sizeStart.y);
        sprite.setAlphaMult(1);
        sprite.setAngle(angle);
        sprite.setColor(color);
        sprite.setAdditiveBlend();

        fader = new FaderUtil(alphaMult,fadeOutDur);
        fader.fadeOut();
    }

    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;


        elapsed += amount;
        sprite.setAngle(sprite.getAngle() + angleRotation);
        currSize = new Vector2f(sprite.getWidth(), sprite.getHeight());
        //entity.getLocation().set(loc);

        if (currSize.x < sizeEnd.x){
            sprite.setSize(sprite.getWidth() + flatIncreaseRate, sprite.getHeight() + flatIncreaseRate);
        }

        if (elapsed > (spriteDur * 0.9) - fadeOutDur || currSize.x >= sizeEnd.x){
            fader.advance(amount);
        }
    }

    public void render(CombatEngineLayers layer, ViewportAPI viewport) {
        //sprite.setSize(sizeStart.x * scale,sizeStart.y * scale);
        sprite.setAlphaMult(viewport.getAlphaMult() * fader.getBrightness());
        sprite.renderAtCenter(loc.x,loc.y);
    }

    public float getRenderRadius() {
        return 500;
    }

    public boolean isExpired() {
        if (elapsed > spriteDur || sprite.getAlphaMult() == 0){
            return true;
        }
        return false;
    }
    public void init(CombatEntityAPI entity) {
        this.entity = entity;
    }
    protected EnumSet<CombatEngineLayers> layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
    public EnumSet<CombatEngineLayers> getActiveLayers() {return layers;}
}
