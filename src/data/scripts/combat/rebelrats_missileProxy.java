package data.scripts.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.List;

public class rebelrats_missileProxy extends BaseEveryFrameCombatPlugin {
    protected MissileAPI missile;
    protected CombatEngineAPI engine;
    private float proxyRange = 30;
    private int numShrap = 40;
    private DamagingExplosionSpec createExplosionSpec() {
        float damage = 60f;
        DamagingExplosionSpec spec = new DamagingExplosionSpec(
                0.1f, // duration
                40f, // radius
                40f, // coreRadius
                damage, // maxDamage
                damage / 2f, // minDamage
                CollisionClass.PROJECTILE_FF, // collisionClass
                CollisionClass.PROJECTILE_FIGHTER, // collisionClassByFighter
                3f, // particleSizeMin
                3f, // particleSizeRange
                0.7f, // particleDuration
                0, // particleCount
                new Color(179,179,179,255), // particleColor
                new Color(163,124,200,0)  // explosionColor
        );

        spec.setDetailedExplosionFlashColorCore(new Color(217, 194, 158));
        spec.setDetailedExplosionFlashColorFringe(new Color(255, 255, 255));
        spec.setDetailedExplosionFlashDuration(0.3f);
        spec.setDetailedExplosionRadius(20f);
        spec.setDetailedExplosionFlashRadius(90f);
        spec.setDamageType(DamageType.FRAGMENTATION);
        return spec;
    }
    public rebelrats_missileProxy(MissileAPI m, CombatEngineAPI engine){
        missile = m;
        this.engine = engine;
    }
    public void advance(float amount, List<InputEventAPI> events) {
        if(engine.isPaused())return;
        if (missile.isFading() || missile.isExpired() || !engine.isEntityInPlay(missile)) {
            engine.removePlugin(this);
            return;
        }

        for(ShipAPI ship : engine.getShips()){
            if (!(ship.isFighter()))continue;
            if (ship.getOwner() == missile.getOwner())continue;
            if (MathUtils.getDistance(missile.getLocation(),ship.getLocation()) < proxyRange){
                engine.spawnDamagingExplosion(createExplosionSpec(), missile.getSource(),missile.getLocation());
                for (int i = 0; i < numShrap/2; i++) {
                    float angle = rebelrats_combatUtils.calcConeAngle(360,missile.getFacing());
                    engine.addHitParticle(missile.getLocation(),rebelrats_combatUtils.calcVelDir(angle,90f),rebelrats_combatUtils.randomNumber(8f,10f),1f,2f, new Color(200,200,200,255));
                }
                for (int i = 0; i < numShrap/2; i++) {
                    float angle = rebelrats_combatUtils.calcConeAngle(360,missile.getFacing());
                    engine.addHitParticle(missile.getLocation(),rebelrats_combatUtils.calcVelDir(angle,rebelrats_combatUtils.randomNumber(90f,91f)),rebelrats_combatUtils.randomNumber(5f,8f),1f,2f, new Color(200,200,200,255));
                }
                engine.removeEntity(missile);
                engine.removePlugin(this);
            }
        }
    }
}
