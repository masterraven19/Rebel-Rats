package data.scripts.combat;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.ai.MagicMissileAI;

import java.awt.Color;
import java.util.List;

public class rebelrats_verlokkenScript extends BaseEveryFrameCombatPlugin {
    private CombatEngineAPI engine;
    private MissileAPI missile;
    private MagicMissileAI ai;
    private float range = 1200;
    private float proxyrange = 500;
    private float deadzoneRange = 0;
    private float numDecoys = 4;
    private float cone = 180;
    private float launchSpeedMin = 150;
    private float launchSpeedMax = 180;
    private float d = 0;
    private int frame = 1;
    private int maxFrames = 6;
    private int FPS = 6;
    private float frameRate = 1f/FPS;
    private IntervalUtil interval = new IntervalUtil(frameRate,frameRate);
    private boolean finished;
    private String munitionId = "rebelrats_verlokken_munition";
    private String mode = "main";
    public rebelrats_verlokkenScript(CombatEngineAPI engine, MissileAPI missile, MagicMissileAI ai, String mode){
        this.engine = engine;
        this.missile = missile;
        this.ai = ai;
        this.mode = mode;
        this.deadzoneRange = missile.getSource().getCollisionRadius();
    }
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine.isPaused()) return;
        if (engine.getMissiles().isEmpty()) return;
        if (missile.isFading() || missile.isExpired() || !engine.isEntityInPlay(missile)) {
            engine.removePlugin(this);
            return;
        }

        interval.advance(amount);
        if(mode.equals("track")){
            mainBody();
        }
        if(mode.equals("animate")){
            munition();
        }
    }
    public void mainBody(){
        for (MissileAPI m : engine.getMissiles()) {
            if (m.isFading() || m.isExpired()) continue;
            if (m.getOwner() != missile.getOwner() && m != missile) {
                d = MathUtils.getDistance(missile.getLocation(),m.getLocation());
                if (d < range){
                    range = d;
                    ai.setTarget(m);
                }
                if(d < proxyrange){
                    float distFromShip = MathUtils.getDistance(missile.getLocation(),missile.getSource().getLocation());
                    if(distFromShip < deadzoneRange) return;

                    for(int i = 1;i < numDecoys;i++){
                        MissileAPI p = (MissileAPI)engine.spawnProjectile(missile.getSource(), null, munitionId, missile.getLocation(), missile.getFacing(), missile.getSource().getVelocity());
                        float angle = rebelrats_combatUtils.calcConeAngle(cone,missile.getFacing());
                        float launchSpeed = rebelrats_combatUtils.randomNumber(launchSpeedMin,launchSpeedMax);
                        Vector2f velocity = rebelrats_combatUtils.calcVelDir(angle,launchSpeed);
                        p.getVelocity().set(velocity);

                        rebelrats_verlokkenScript rat = new rebelrats_verlokkenScript(engine,p,null,"animate");
                        engine.addPlugin(rat);
                    }
                    engine.addSmokeParticle(missile.getLocation(), new Vector2f(0, 0), 50F, 1F, 1F, Color.WHITE);
                    engine.removeEntity(missile);
                    engine.removePlugin(this);
                    return;
                }
            }
        }
    }
    public void munition(){
        //if(interval.intervalElapsed()){
            if(finished)return;
            float angle = missile.getFacing() - 90;
            float duration = missile.getMaxFlightTime() - missile.getFlightTime() + missile.getSpec().getFadeTime();

            rebelrats_addAnimation p = new rebelrats_addAnimation(missile,null,
                    "rebelrats_verlokkenAnimation",new Vector2f(30,26),
                    null,null,angle,0.4f,0,3,null);
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(missile.getLocation());
            engine.removePlugin(this);


            frame += 1;
        //}
    }
}
