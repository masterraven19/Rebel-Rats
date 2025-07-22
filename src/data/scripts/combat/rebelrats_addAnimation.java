package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.EnumSet;

//Will create a single particle when used. Make sure to assign it as a CombatEntity as
//"engine.addLayeredPlugin(copy of this class)"
//Will follow the projectile when given one, will stay in place when given a Vector2f
// example
//rebelrats_addParticle p = new rebelrats_addParticle();
//p.addParticle(m,"graphics/fx/target_painter.png",50,50,null,new Vector2f(0,0),m.getFacing(),0,true,1,m.getMaxFlightTime(),false, null);
//CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
//e.getLocation().set(projectile.getLocation());
public class rebelrats_addAnimation extends BaseCombatLayeredRenderingPlugin {
    private CombatEntityAPI proj;
    private SpriteAPI sprite;
    private Vector2f loc;
    private Vector2f offset;
    private Vector2f vel;
    private FaderUtil fader;
    private int currFrame = 0;
    private int totalFrames = 0;
    private float fpsInterval = 0;
    private float elapsed = 0;
    private float elapsedDur = 0;
    private float spriteDur = 0;
    private float angle = 0;
    private String spriteCategory;
    public rebelrats_addAnimation(CombatEntityAPI proj, Vector2f loc,
                                  String spriteCategory, Vector2f spriteSize,
                                  Vector2f offset, Vector2f vel,
                                  float angle, float fadeoutDur,
                                  float spriteDur, int fps, Color color){

        this.totalFrames = Global.getSettings().getSpriteKeys(spriteCategory).size();
        this.fpsInterval = 1f / fps;
        this.spriteCategory = spriteCategory;
        this.spriteDur = spriteDur;
        this.angle = angle;

        if (loc != null) {
            this.loc = Vector2f.add(loc,offset,new Vector2f(0,0));
        }
        if (proj != null) {
            this.proj = proj;
            this.loc = proj.getLocation();
        }

        this.sprite = Global.getSettings().getSprite(spriteCategory,""+currFrame,true);

        if(offset == null){
            this.offset = new Vector2f(0,0);
        }else{
            this.offset = offset;
        }
        if(vel == null){
            this.vel = new Vector2f(0,0);
        }else{
            this.vel = vel;
        }
        if (color != null){
            sprite.setColor(color);
        }

        sprite.setSize(spriteSize.x,spriteSize.y);
        sprite.setAngle(angle);
        sprite.setNormalBlend();

        fader = new FaderUtil(1f,fadeoutDur);
        fader.fadeOut();
    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;

        elapsed += amount;
        elapsedDur += amount;

        entity.getLocation().set(loc.x += vel.x * amount, loc.y += vel.y * amount);

        if(proj == null){
            if(elapsedDur > spriteDur) fader.advance(amount);
        }else{
            if(proj.isExpired() || !Global.getCombatEngine().isEntityInPlay(proj)) fader.advance(amount);

            sprite.setAngle(angle);
        }

        if(elapsed > fpsInterval && currFrame < totalFrames - 1){
            elapsed -= fpsInterval;

            currFrame++;
            sprite = Global.getSettings().getSprite(spriteCategory,""+currFrame,true);
        }
    }
    public float getRenderRadius() {
        return 300;
    }

    public void render(CombatEngineLayers layer, ViewportAPI viewport) {

        sprite.setAlphaMult(viewport.getAlphaMult() * fader.getBrightness());

        sprite.renderAtCenter(loc.x + offset.x, loc.y + offset.y);

    }
    protected EnumSet<CombatEngineLayers> layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);

    public EnumSet<CombatEngineLayers> getActiveLayers() {return layers;}

    public boolean isExpired() {
        if(sprite.getAlphaMult() == 0){
            return true;
        }
        return false;
    }
    public void init(CombatEntityAPI entity) {
        this.entity = entity;
    }
}
