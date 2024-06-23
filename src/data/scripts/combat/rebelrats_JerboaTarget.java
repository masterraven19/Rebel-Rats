package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;

import java.awt.Color;
import java.util.List;

public class rebelrats_JerboaTarget extends BaseEveryFrameCombatPlugin {
    private float damMult;
    private String id;
    private ShipAPI target;
    private Color jitterColor = new Color(60,213,33,255);
    public void update(ShipAPI target, float damMult, String id){
        this.target = target;
        this.damMult = damMult;
        this.id = id;
    }

    public void advance(float amount, List<InputEventAPI> events) {
        if (Global.getCombatEngine().isPaused()) return;
        if (target != null){
            if (damMult < 1F) {
                target.getMutableStats().getWeaponTurnRateBonus().unmodify(id);
                target.getMutableStats().getAcceleration().unmodify(id);
                target.getMutableStats().getDeceleration().unmodify(id);
                target.getMutableStats().getTurnAcceleration().unmodify(id);
                target.getMutableStats().getMaxTurnRate().unmodify(id);
                target.getMutableStats().getMaxSpeed().unmodify(id);
                //Global.getCombatEngine().addFloatingText(target.getLocation(),"removed",10, Color.WHITE,target,1,1);
                Global.getCombatEngine().removePlugin(this);
            }else{
                target.getMutableStats().getWeaponTurnRateBonus().modifyMult(id,-1);
                target.getMutableStats().getAcceleration().modifyPercent(id,-200 * damMult);
                target.getMutableStats().getDeceleration().modifyPercent(id,-200 * damMult);
                target.getMutableStats().getTurnAcceleration().modifyPercent(id,-200 * damMult);
                target.getMutableStats().getMaxTurnRate().modifyPercent(id, 0 * damMult);
                target.getMutableStats().getMaxSpeed().modifyFlat(id, 0 * damMult);
                if (damMult > 0) {
                    float shipjitter = damMult * 0.7F;
                    if (shipjitter > 0){
                        target.setJitter(target,jitterColor,shipjitter,3,5f);
                    }
                }
                //Global.getCombatEngine().addFloatingText(target.getLocation(),""+damMult,10, Color.WHITE,target,1,1);
            }
        }
    }
}
