package data.scripts.unused;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;

import java.util.List;

public class rebelrats_custodianProxEffect extends BaseEveryFrameCombatPlugin {
    protected IntervalUtil interval = new IntervalUtil(0.1f,0.1f);
    protected DamagingProjectileAPI proj;
    protected WeaponAPI weapon;
    protected static float distance;
    protected static float proxRange = 90;
    protected static boolean hasfired = false;
    protected static float numflak = 3;
    protected static float n = 0;
    public rebelrats_custodianProxEffect(DamagingProjectileAPI proj, WeaponAPI weapon){
        this.proj = proj;
        this.weapon = weapon;
    }
    public void spawnflak(CombatEngineAPI engine){
        if (hasfired == false) {
            for (int i = 0; i < numflak; i++) {
                n = n + 1;
                float angle = (float) Math.random() * 360;
                engine.spawnProjectile(weapon.getShip(), weapon, "rebelrats_custodian_munition", proj.getLocation(), angle, weapon.getShip().getVelocity());
                if (i == numflak){engine.removeObject(proj);}
            }
        }
    }
    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {

    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (Global.getCombatEngine().isPaused()) return;
        CombatEngineAPI engine = Global.getCombatEngine();
        if (engine.getMissiles().isEmpty()) return;
        //fix tomorrow, doesn't proc when near multiple missiles, probably detection issue
        for (MissileAPI m : engine.getMissiles()){
            if (m.isFading() || m.isExpired()) return;
            float d = MathUtils.getDistance(m.getLocation(),proj.getLocation());
            if (d < proxRange){
                spawnflak(engine);
                hasfired = true;
            }
        }

        if (proj.isExpired() || proj.didDamage() || !Global.getCombatEngine().isEntityInPlay(proj)) {
            engine.removePlugin(this);
            hasfired = false;
        }
    }
}
