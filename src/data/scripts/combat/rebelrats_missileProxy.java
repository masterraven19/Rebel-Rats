package data.scripts.combat;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lazywizard.lazylib.MathUtils;

import java.util.List;

public class rebelrats_missileProxy extends BaseEveryFrameCombatPlugin {
    protected MissileAPI missile;
    protected CombatEngineAPI engine;
    private float proxyRange = 30;
    private int numShrap = 40;
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
                for (int i = 0; i < numShrap; i++) {
                    CombatEntityAPI p = engine.spawnProjectile(missile.getSource(), null, "rebelrats_hwacha_munition", missile.getLocation(), missile.getFacing(), missile.getSource().getVelocity());
                    float angle = rebelrats_combatUtils.calcConeAngle(360,missile.getFacing());
                    p.setFacing(angle);
                }
                engine.removeEntity(missile);
                engine.removePlugin(this);
            }
        }
    }
}
