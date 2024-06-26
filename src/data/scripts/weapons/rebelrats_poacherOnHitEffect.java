package data.scripts.weapons;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lwjgl.util.vector.Vector2f;

public class rebelrats_poacherOnHitEffect implements OnHitEffectPlugin {
    private float extraDmg = 30;
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
                      Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (target instanceof ShipAPI){
            ShipAPI ship = (ShipAPI)target;
            if (ship.isFighter()){
                engine.applyDamage(target,point,extraDmg,DamageType.FRAGMENTATION,0,false,false,projectile.getSource());
            }
        }
        if (target instanceof MissileAPI){
            engine.applyDamage(target,point,extraDmg,DamageType.FRAGMENTATION,0,false,false,projectile.getSource());
        }
    }
}
