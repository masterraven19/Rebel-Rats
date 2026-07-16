package data.scripts.combat.particles;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import data.scripts.utils.rebelrats_booleanUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.EnumSet;

/**
 * Will create a particle from the provided {@link SpriteAPI}.
 * Use {@link rebelrats_particleUtils#addParticlePlugin(CombatLayeredRenderingPlugin, Vector2f)}
 * to add to combat engine
 */
public class rebelrats_addParticle extends BaseCombatLayeredRenderingPlugin {
    // BASE
    protected SpriteAPI sprite;
    protected Vector2f location;
    protected Vector2f velocity;
    protected Vector2f offset;
    protected float spriteDur;
    protected float fadeDur;
    protected FaderUtil fader;

    // ADDONS

    // SPIN
    protected float flatSpinRate = 0;

    // EXPAND / SHRINK
    protected Vector2f targetSize = null;
    protected float flatExpandRate;
    protected boolean fadeAfterExpand;

    // ATTACH TO ENTITY
    protected CombatEntityAPI attachedEntity;

    // PULSE
    protected float minSizeScale;
    protected float maxSizeScale;
    protected float flatPulseExpandRate;
    protected float pulseDur;
    private Vector2f originalSize;
    private float pulseElapsed;

    private float elapsed;

    public rebelrats_addParticle(
            SpriteAPI sprite, Vector2f location,
            Vector2f offset, Vector2f velocity,
            float spriteDur, float alpha,
            float fadeDur
    ) {
        this.sprite = sprite;
        this.location = location;
        this.offset = (offset == null) ? new Vector2f() : offset;
        this.velocity = (velocity == null) ? new Vector2f() : velocity;
        this.spriteDur = spriteDur;

        sprite.setAlphaMult(alpha);

        fader = new FaderUtil(alpha, fadeDur);
        fader.fadeOut();
    }

    public void addSpin(float flatSpinRate){
        this.flatSpinRate = flatSpinRate;
    }
    public void addExpand(
            Vector2f targetSize, float flatExpandRate, boolean fadeAfterExpand
    ) {
        this.flatExpandRate = flatExpandRate;
        this.fadeAfterExpand = fadeAfterExpand;
        this.targetSize = targetSize;
    }
    public void addEntity(CombatEntityAPI attachedEntity) {
        this.attachedEntity = attachedEntity;
    }
    public void addPulse(
            float minSizeScale, float maxSizeScale, float flatPulseExpandRate,
            float pulseDur
    ){
        this.minSizeScale = minSizeScale;
        this.maxSizeScale = maxSizeScale;
        this.flatPulseExpandRate = flatPulseExpandRate;
        this.pulseDur = pulseDur;
        originalSize = new Vector2f(sprite.getWidth(), sprite.getHeight());
    }

    public void addParticle(CombatEntityAPI proj, String spriteNameOrCategory, String spriteKey,
                            float spriteWidth, float spriteHeight, Vector2f loc, Vector2f offset,
                            Vector2f vel,
                            float angle, float angleRotation, boolean pulseInOut,
                            float pulseDur, float spriteDur, float alphaMult, float fadeOutDur,
                            boolean multipleSprites,
                            Color color){

    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;


        elapsed += amount;
        if (elapsed >= spriteDur || elapsed >= spriteDur - fadeDur){
            fader.advance(amount);
        }

        entity.getLocation().set(
                location.x += velocity.x * amount,
                location.y += velocity.y * amount
        );

        // SPIN
        if (flatSpinRate != 0){
            sprite.setAngle(sprite.getAngle() + flatSpinRate);
        }

        // EXPAND
        if (targetSize != null){
            Vector2f currSize = new Vector2f(sprite.getWidth(), sprite.getHeight());

            if (currSize.x < targetSize.x){
                sprite.setSize(
                        sprite.getWidth() + flatExpandRate,
                        sprite.getHeight() + flatExpandRate
                );
            }
            if (currSize.x >= targetSize.x && fadeAfterExpand) {
                fader.advance(amount);
            }
        }

        // PULSE
        if (minSizeScale != 0){
            pulseElapsed += amount;
            Vector2f currSize = new Vector2f(sprite.getWidth(), sprite.getHeight());
            Vector2f minSize = new Vector2f(
                    originalSize.x * minSizeScale,
                    originalSize.y * minSizeScale
            );
            Vector2f maxSize = new Vector2f(
                    originalSize.x * maxSizeScale,
                    originalSize.y * maxSizeScale
            );

            if (pulseElapsed < pulseDur){
                if (currSize.x < maxSize.x){
                    sprite.setSize(
                            sprite.getWidth() + flatPulseExpandRate,
                            sprite.getHeight() + flatPulseExpandRate
                    );
                }
            }
            if (pulseElapsed > pulseDur){
                if (currSize.x > minSize.x){
                    sprite.setSize(
                            sprite.getWidth() - flatPulseExpandRate,
                            sprite.getHeight() - flatPulseExpandRate
                    );
                }else {
                    pulseElapsed -= pulseDur;
                }
            }
        }

    }
    public float getRenderRadius() {
        return 300;
    }

    public void render(CombatEngineLayers layer, ViewportAPI viewport) {
        sprite.setAlphaMult(viewport.getAlphaMult() * fader.getBrightness());

        sprite.renderAtCenter(location.x + offset.x, location.y + offset.y);
    }
    protected EnumSet<CombatEngineLayers> layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);

    public EnumSet<CombatEngineLayers> getActiveLayers() {return layers;}

    public boolean isExpired() {

        if (attachedEntity != null) {
            return rebelrats_booleanUtils.isEntityAlive(attachedEntity);
        }

        return
                sprite.getAlphaMult() == 0 ||
                elapsed >= spriteDur + fadeDur + 1f;
    }
}
