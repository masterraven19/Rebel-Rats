package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.ai.MagicMissileAI;

import java.awt.*;
import java.util.List;

public class rebelrats_missileScript extends BaseEveryFrameCombatPlugin {
    protected MissileAPI missile;
    protected MagicMissileAI ai;
    protected CombatEngineAPI engine;
    private float range = 1200;
    private float proxyrange = 100;
    private float numshrapnel = 35;
    private float cone = 90;
    private float d = 0;
    public rebelrats_missileScript(MissileAPI m, CombatEngineAPI e, MagicMissileAI ai){
        this.missile = m;
        this.ai = ai;
        this.engine = e;
    }
    private void resetrange(){
        d = 0;
        range = 1200;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (engine.isPaused()) return;
        if (engine.getMissiles().isEmpty()) return;
        if (missile.isFading() || missile.isExpired() || !engine.isEntityInPlay(missile)) {
            engine.removePlugin(this);
            resetrange();
            return;
        }

        for (MissileAPI m : engine.getMissiles()){
            if (m.isFading() || m.isExpired()) continue;
            if (m.getOwner() != missile.getOwner() && m != missile) {
                d = MathUtils.getDistance(missile.getLocation(),m.getLocation());
                if (d < range){
                    range = d;
                    ai.setTarget(m);
                }
                if (d < proxyrange){
                    for (int i = 0; i < numshrapnel; i++){
                        CombatEntityAPI p = engine.spawnProjectile(missile.getSource(), null, "rebelrats_rattenjager_munition", missile.getLocation(), missile.getFacing(), missile.getSource().getVelocity());
                        float angle = cone * (float)Math.random();
                        if (angle > cone/2){angle = (float)Math.random() * cone/2;}
                        else{angle = (float)Math.random() * -cone/2;}
                        angle = missile.getFacing() - angle;
                        p.setFacing(angle);
                    }
                    engine.addSmokeParticle(missile.getLocation(),new Vector2f(0,0),50F,1F,1F,Color.WHITE);
                    resetrange();
                    engine.removePlugin(this);
                    engine.removeEntity(missile);
                }
            }
        }
    }
}
