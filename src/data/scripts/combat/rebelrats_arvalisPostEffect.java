package data.scripts.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.List;

public class rebelrats_arvalisPostEffect extends BaseEveryFrameCombatPlugin {
    private CombatEntityAPI proj;
    private CombatEntityAPI target;
    private CombatEngineAPI engine;
    private Vector2f projloc;
    private boolean PorB;
    private boolean right = (Math.random() < 0.5f);
    private float armorDmg = 5;
    private float numExplosions = 0;
    private float maxExplosions = 10;
    private float facing;
    private float shipLength;

    public rebelrats_arvalisPostEffect(CombatEntityAPI proj, boolean PenOrBounce,
                                       CombatEntityAPI target, CombatEngineAPI engine){
        this.proj = proj;
        this.projloc = proj.getLocation();
        this.facing = proj.getFacing();
        this.target = target;
        this.shipLength = target.getCollisionRadius();
        this.PorB = PenOrBounce;
        this.engine = engine;
    }
    public void advance(float amount, List<InputEventAPI> events) {
        if(engine.isPaused()) return;
        if(proj.isExpired() || !engine.isEntityInPlay(proj)){
            engine.removePlugin(this);
            return;
        }
        if(PorB){
            float angle = 1;
            if(right){angle *= -1;}
            proj.setFacing(proj.getFacing() + angle);
        }else{
            if(numExplosions == maxExplosions) return;
            numExplosions += 1;

            float ratio = numExplosions / maxExplosions;
            Vector2f exploloc = rebelrats_combatUtils.calcLocWAngle(facing,shipLength * ratio, projloc);

            ShipAPI ship = (ShipAPI) target;
            ArmorGridAPI grid = ship.getArmorGrid();
            int[] cell = grid.getCellAtLocation(exploloc);
            if(cell == null){
                CombatEntityAPI newproj = engine.spawnProjectile(null, null,"rebelrats_arvalis_munition",exploloc, facing,null);
                engine.addPlugin(new rebelrats_arvalisPostEffect(newproj,true,target,engine));
                numExplosions = maxExplosions;
                return;
            }

            BreachOnHitEffect.dealArmorDamage((DamagingProjectileAPI) proj,(ShipAPI) target,exploloc,armorDmg);

            rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
            explosionFx.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(50,50),new Vector2f(100,100),exploloc,new Vector2f(0,0), new Color(235,163,54),1.5F,2,2.2F,0,(float) Math.random() * 180,0.8F,false);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
            e.getLocation().set(exploloc);
        }
    }
}
