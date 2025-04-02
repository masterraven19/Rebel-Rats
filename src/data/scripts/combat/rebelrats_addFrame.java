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

//Will create a single particle when used. Make sure to assign it as a CombatEntity as
//"engine.addLayeredPlugin(copy of this class)"
//Will follow the projectile when given one, will stay in place when given a Vector2f
// example
//rebelrats_addParticle p = new rebelrats_addParticle();
//p.addParticle(m,"graphics/fx/target_painter.png",50,50,null,new Vector2f(0,0),m.getFacing(),0,true,1,m.getMaxFlightTime(),false, null);
//CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
//e.getLocation().set(projectile.getLocation());
public class rebelrats_addFrame extends BaseCombatLayeredRenderingPlugin {
    private CombatEntityAPI proj;
    private SpriteAPI sprite;
    private Vector2f loc;
    private Vector2f offset;
    private Vector2f vel;
    private float angleRotation;
    private float spriteDur = 0;
    private float elapsed = 0;
    public rebelrats_addFrame(CombatEntityAPI proj,Vector2f loc,
                              String spriteNameOrCategory, String spriteKey,
                            Vector2f spriteSize, Vector2f offset,
                            Vector2f vel, float spriteDur,
                            float angle, float angleRotation,
                            boolean multipleSprites,
                            Color color){

        if (proj != null) {
            this.proj = proj;
            this.loc = proj.getLocation();
        } else {
            if (loc != null) {
                this.loc = Vector2f.add(loc,offset,new Vector2f(0,0));
            }
        }

        if (spriteNameOrCategory != null && spriteKey != null){
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory,spriteKey,true);
        }else{
            this.sprite = Global.getSettings().getSprite(spriteNameOrCategory);
        }

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

        this.angleRotation = angleRotation;
        this.spriteDur = spriteDur;
        if (color != null){
            sprite.setColor(color);
        }

        if (multipleSprites){
            float i = Misc.random.nextInt(4);
            float j = Misc.random.nextInt(4);
            sprite.setTexWidth(0.25f);
            sprite.setTexHeight(0.25f);
            sprite.setTexX(i * 0.25f);
            sprite.setTexY(j * 0.25f);
        }
        sprite.setSize(spriteSize.x,spriteSize.y);
        sprite.setAngle(angle);
        sprite.setNormalBlend();

    }
    public void advance(float amount){
        if (Global.getCombatEngine().isPaused()) return;

        elapsed += amount;

        entity.getLocation().set(loc.x += vel.x * amount, loc.y += vel.y * amount);

        sprite.setAngle(sprite.getAngle() + angleRotation);

    }
    public float getRenderRadius() {
        return 300;
    }

    public void render(CombatEngineLayers layer, ViewportAPI viewport) {

        sprite.renderAtCenter(loc.x + offset.x, loc.y + offset.y);

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
