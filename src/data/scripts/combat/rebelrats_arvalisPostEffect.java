package data.scripts.combat;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class rebelrats_arvalisPostEffect extends BaseEveryFrameCombatPlugin {
    private CombatEntityAPI proj;
    private CombatEngineAPI engine;
    private boolean right = (Math.random() < 0.5f);

    public rebelrats_arvalisPostEffect(CombatEntityAPI proj, CombatEngineAPI engine){
        this.proj = proj;
        this.engine = engine;
    }
    public void advance(float amount, List<InputEventAPI> events) {
        if(engine.isPaused()) return;
        if(proj.isExpired() || !engine.isEntityInPlay(proj)){
            engine.removePlugin(this);
            return;
        }

        float angle = 1;
        if(right){angle *= -1;}
        proj.setFacing(proj.getFacing() + angle);
    }
}
