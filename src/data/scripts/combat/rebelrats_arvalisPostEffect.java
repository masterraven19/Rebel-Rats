package data.scripts.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.combat.ids.rebelrats_Weapons;
import data.scripts.weapons.rebelrats_arvalisEffect;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.List;

public class rebelrats_arvalisPostEffect extends BaseEveryFrameCombatPlugin {
    private CombatEntityAPI bounceProj;
    private CombatEngineAPI engine;
    private DamagingProjectileAPI passThroughProj;
    private ShipAPI target;
    private float elapsed;
    private final float facingVel = 2 * ((Math.random() < 0.5f) ? 1f : -1f);
    private int currExplosion = 0;

    public rebelrats_arvalisPostEffect(
            CombatEntityAPI bounceProj, CombatEngineAPI engine, DamagingProjectileAPI passThroughProj,
            ShipAPI target
    ){
        this.engine = engine;
        this.passThroughProj = passThroughProj;
        this.bounceProj = bounceProj;
        this.target = target;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (engine.isPaused()) return;
        if (bounceProj == null && passThroughProj == null) {
            engine.removePlugin(this);
            return;
        }
        if (bounceProj != null) {
            if (bounceProj.isExpired() || !engine.isEntityInPlay(bounceProj)) {
                engine.removePlugin(this);
                return;
            }
        }

        if (passThroughProj != null) {

            Vector2f point = new Vector2f(passThroughProj.getLocation());
            float facing = passThroughProj.getFacing();

            if (!target.isPointInBounds(point)) {

                point = rebelrats_combatUtils.calcLocWAngle(facing, 25f, point);
                CombatEntityAPI newProj = engine.spawnProjectile(passThroughProj.getSource(), null, rebelrats_Weapons.arvalisMunition, point, facing, null);

                engine.addPlugin(new rebelrats_arvalisPostEffect(
                        newProj, engine, null, null
                ));
                engine.removePlugin(this);
                return;
            }
            if (currExplosion == rebelrats_arvalisEffect.numExplosions &&
                    target.isPointInBounds(point)
            ) {
                engine.removePlugin(this);
                return;
            }


            BreachOnHitEffect.dealArmorDamage(passThroughProj, target, point, rebelrats_arvalisEffect.armorDmg);

            rebelrats_addExplosionFx explosionFx = new rebelrats_addExplosionFx();
            explosionFx.addExplosion("graphics/fx/explosion_ring0.png",null,new Vector2f(6,6),new Vector2f(150,150),point,new Vector2f(0,0), new Color(22, 153, 196),1.5F,1f,2.2F,0,(float) Math.random() * 180,0.8F,false);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(explosionFx);
            e.getLocation().set(point);

            currExplosion++;
        }

        if (bounceProj != null) {
            bounceProj.setFacing(bounceProj.getFacing() + facingVel);
        }
    }
}
