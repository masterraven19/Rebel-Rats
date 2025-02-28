package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.impl.combat.DisintegratorEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.ai.MagicMissileAI;

import java.awt.*;
import java.util.List;

public class rebelrats_gingsenCloudEffect extends BaseEveryFrameCombatPlugin {
    private float TOTAL_HEAL = 300;
    private float MAX_DUR = 14;
    private int TOTAL_TICKS = 5;
    private MissileAPI proj;
    private MagicMissileAI ai;
    private ShipAPI target;
    private Vector2f loc;
    private int ticks = 0;
    private float elapsed;
    private float closestDistance = 0;
    private IntervalUtil interval;
    public rebelrats_gingsenCloudEffect(MissileAPI missile){
        this.proj = missile;
        interval = new IntervalUtil(0.2f, 0.3f);
        interval.forceIntervalElapsed();

        MagicMissileAI ai = new MagicMissileAI(missile, missile.getSource());
        missile.setMissileAI(ai);
        this.ai = ai;
    }
    public void advance(float amount, List<InputEventAPI> events) {
        CombatEngineAPI engine = Global.getCombatEngine();
        if(engine.isPaused()) return;
        if(!engine.isEntityInPlay(proj) || proj.isFading()){
            elapsed += amount;
        }
        if(ticks >= TOTAL_TICKS || elapsed > MAX_DUR){
            engine.removePlugin(this);
        }
        if(loc != null){
            interval.advance(amount);
            if (interval.intervalElapsed() && ticks < TOTAL_TICKS) {
                dealDamage();

                ticks++;
            }
            return;
        }

        float range = proj.getWeapon().getRange();
        for(ShipAPI target : engine.getShips()){
            if(target.isFighter()) continue;
            if(target == proj.getSource()) continue;
            if(!(target.getOwner() == proj.getSource().getOwner())) continue;
            float d = Misc.getDistance(target.getLocation(),proj.getSource().getLocation());
            if(d < range){
                if(d > closestDistance){
                    closestDistance = d;
                    ai.setTarget(target);
                    this.target = target;
                }
            }
        }

        if(target == null) return;
        ArmorGridAPI grid = target.getArmorGrid();
        if(grid.getCellAtLocation(proj.getLocation()) != null){
            this.loc = proj.getLocation();
            //ejection clouds
            for(int i = 0;i < 4;i++){
                float angle = rebelrats_combatUtils.calcConeAngle(40f,proj.getFacing());
                Vector2f velocity = rebelrats_combatUtils.calcVelDir(angle,600f);
                rebelrats_addParticle p = new rebelrats_addParticle();
                float size = rebelrats_combatUtils.randomNumber(50,60);
                p.addParticle(null, "misc","nebula_particles",size,size,loc,new Vector2f(0,0),velocity, rebelrats_combatUtils.randomNumber(0,360),rebelrats_combatUtils.randomNumber(-2,2),false,0f,1f,0.7f,0.5f,true,new Color(240, 240, 240,255));
                CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
                e.getLocation().set(loc);
            }
            engine.removeEntity(proj);
        }
    }
    protected void dealDamage() {
        CombatEngineAPI engine = Global.getCombatEngine();
        //fart cloud
        Vector2f offset = rebelrats_combatUtils.calcLocWAngle(proj.getFacing(),rebelrats_combatUtils.randomNumber(70,140),loc);
        for(int i = 0;i < 4;i++){
            rebelrats_addParticle p = new rebelrats_addParticle();
            float size = rebelrats_combatUtils.randomNumber(150,200);
            p.addParticle(null, "misc","nebula_particles",size,size,offset,new Vector2f(0,0),new Vector2f(0,0), rebelrats_combatUtils.randomNumber(0,360),rebelrats_combatUtils.randomNumber(-2,2),false,0f,1f,0.7f,0.5f,true,new Color(93, 232, 125,255));
            CombatEntityAPI e = engine.addLayeredRenderingPlugin(p);
            e.getLocation().set(loc);
        }

        ArmorGridAPI grid = target.getArmorGrid();
        int[] cell = grid.getCellAtLocation(loc);
        if (cell == null) return;

        int gridWidth = grid.getGrid().length;
        int gridHeight = grid.getGrid()[0].length;

        float damageTypeMult = DisintegratorEffect.getDamageTypeMult(proj.getSource(), target);

        float damagePerTick = (float) TOTAL_HEAL / (float) TOTAL_TICKS;
        float damageDealt = 0f;

        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                if ((i == 4 || i == -4) && (j == 4 || j == -4)) continue; // skip corners

                int cx = cell[0] + i;
                int cy = cell[1] + j;

                if (cx < 0 || cx >= gridWidth || cy < 0 || cy >= gridHeight) continue;

                float damMult = 1/30f;
                if (i == 0 && j == 0) {
                    damMult = 1/15f;
                } else if (i <= 1 && i >= -1 && j <= 1 && j >= -1) { // S hits
                    damMult = 1/15f;
                } else { // T hits
                    damMult = 1/30f;
                }

                float maxArmorInCell = grid.getMaxArmorInCell();
                float armorInCell = grid.getArmorValue(cx, cy);
                float damage = damagePerTick * damMult * damageTypeMult;

                target.getArmorGrid().setArmorValue(cx, cy, Math.min(maxArmorInCell,armorInCell + damage));
                damageDealt += damage;
            }
        }

        if (damageDealt > 0) {
            if (Misc.shouldShowDamageFloaty(proj.getSource(), target)) {
                engine.addFloatingDamageText(loc, damageDealt, new Color(93, 232, 125,255), target, proj.getSource());
            }
            target.syncWithArmorGridState();
        }
    }
}
