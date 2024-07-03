package data.scripts.weapons;

import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import data.scripts.combat.rebelrats_combatUtils;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicFakeBeam;

import java.awt.Color;

public class rebelrats_mischiefBeamEffect implements EveryFrameWeaponEffectPlugin {
    private Color fringe = new Color(83,187,25);
    private Color core = new Color(83,187,25);
    private IntervalUtil interval = new IntervalUtil(.2f,.2f);
    private float width = 5;
    private float timeFullOpacity = 0F;
    private float fade = 0F;
    private float impactGlow = 30f;
    private float fluxPerShot = 10;
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if(engine.isPaused())return;
        if(engine.getMissiles().isEmpty())return;
        if(weapon.getShip() == null)return;
        if(!weapon.getShip().isAlive())return;
        if(weapon.getShip().getFluxTracker().isOverloadedOrVenting())return;
        if(weapon.getShip().getCurrFlux() >= weapon.getShip().getFluxTracker().getMaxFlux() - fluxPerShot * 10)return;
        if(!weapon.getShip().getWeaponGroupFor(weapon).isAutofiring())return;

        Vector2f point = weapon.getFirePoint(0);
        float range = weapon.getRange();
        float dmg = weapon.getDamage().getDamage();
        DamageType dmgType = weapon.getDamageType();
        interval.advance(amount);

        float shipFacing = weapon.getShip().getFacing();
        if(shipFacing > 180){shipFacing -= 360;}
        float arcFacing = weapon.getArcFacing();
        float arcRange = weapon.getArc();
        float arcx,arcy;
        if(arcFacing < 0){
            arcx = shipFacing + (arcFacing + 90);
            arcy = shipFacing + (arcFacing - arcRange/2);
            if(arcx > 180){arcx -= 360;}
            if(arcy < -180){arcy += 360;}
            if(arcx == -180){arcx = 180;}
            if(arcy == 180){arcy = -180;}
        }else{
            arcx = shipFacing - (90 - arcFacing);
            arcy = shipFacing + (arcFacing + arcRange/2);
            if(arcy > 180){arcy -= 360;}
            if(arcx < -180){arcx += 360;}
            if(arcy == -180){arcy = 180;}
            if(arcx == 180){arcx = -180;}
        }

        if(interval.intervalElapsed()){
            for(MissileAPI m : engine.getMissiles()){
                if(m.isFizzling() || m.isExpired())continue;
                if(weapon.getShip().getOwner() == m.getOwner())continue;
                float dir = rebelrats_combatUtils.calcDirectionOfTwoPoints(m.getLocation(),weapon.getLocation());
                if(MathUtils.getDistance(weapon.getLocation(),m.getLocation()) > range)continue;

                if(arcFacing < 0){
                    if((arcy > 0 && dir < 0)||dir > arcy){
                        if((arcx < 0 && dir > 0)|| dir < arcx){
                            MagicFakeBeam.spawnFakeBeam(engine, point, range, dir, width, timeFullOpacity, fade, impactGlow, core, fringe, dmg, dmgType, 0, weapon.getShip());
                            weapon.getShip().getFluxTracker().setCurrFlux(weapon.getShip().getCurrFlux()+fluxPerShot);
                        }
                    }
                }
                if(arcFacing > 0){
                    if((arcx > 0 && dir < 0)||dir > arcx){
                        if((arcy < 0 && dir > 0) || dir < arcy){
                            MagicFakeBeam.spawnFakeBeam(engine, point, range, dir, width, timeFullOpacity, fade, impactGlow, core, fringe, dmg, dmgType, 0, weapon.getShip());
                            weapon.getShip().getFluxTracker().setCurrFlux(weapon.getShip().getCurrFlux()+fluxPerShot);
                        }
                    }
                }
            }
        }
    }
}
