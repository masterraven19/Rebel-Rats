package data.scripts.campaign.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.AfterShipCreationSkillEffect;
import com.fs.starfarer.api.characters.CustomSkillDescription;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.awt.Color;

public class rebelrats_FuriousBrawler {
    public static class Level1 implements AfterShipCreationSkillEffect, CustomSkillDescription {

        @Override
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
        }

        @Override
        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
        }

        @Override
        public String getEffectDescription(float level) {

            return "Increases ship weapon damage based on closest distance.";
        }

        @Override
        public boolean hasCustomDescription() {
            return true;
        }

        @Override
        public void createCustomDescription(MutableCharacterStatsAPI stats, SkillSpecAPI skill,
                                            TooltipMakerAPI info, float width) {
            int frigate = (int) (rebelrats_FuriousBrawlerListener.DAMAGE_MULTIPLIER_FRIGATE * 100f - 100f);
            int destroyer = (int) (rebelrats_FuriousBrawlerListener.DAMAGE_MULTIPLIER_DESTROYER * 100f - 100f);
            int capital = (int) (rebelrats_FuriousBrawlerListener.DAMAGE_MULTIPLIER_CAPITAL * 100f - 100f);
            int lower = (int) rebelrats_FuriousBrawlerListener.LOWER_CAP;
            int higher = (int) rebelrats_FuriousBrawlerListener.HIGHER_CAP ;
            float pad = 20f;
            Color highlight = Misc.getHighlightColor();

            info.addPara("*Piloted ship weapon damage is increased depending on piloted ship's distance"
                    + "\nfrom the piloted ship's target. Starts from %s to %s."
                    + "\nMax damage bonus based on ship size is %s/%s/%s/%s",pad,highlight,
                    lower + " su",
                    higher + " su",
                    frigate + "%",
                    destroyer + "%",
                    destroyer + "%",
                    capital + "%");
        }

        @Override
        public String getEffectPerLevelDescription() {
            return null;
        }

        @Override
        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }

        @Override
        public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
            ship.addListener(new rebelrats_FuriousBrawlerListener(Global.getCombatEngine(),ship));
        }

        @Override
        public void unapplyEffectsAfterShipCreation(ShipAPI ship, String id) {
            ship.removeListenerOfClass(rebelrats_FuriousBrawlerListener.class);
        }
    }
    public static class rebelrats_FuriousBrawlerListener implements AdvanceableListener{
        private CombatEngineAPI engine;
        private ShipAPI ship;
        public static float HIGHER_CAP = 1200f;
        public static float LOWER_CAP = 600f;
        public static float DAMAGE_MULTIPLIER_FRIGATE = 1.8f;
        public static float DAMAGE_MULTIPLIER_DESTROYER = 1.5F;
        public static float DAMAGE_MULTIPLIER_CAPITAL = 1.3F;
        private float DAMAGE_MULTIPLIER_CAP = 1.8f;
        private boolean gotMultiplier = false;
        private Object KEY_WEAPON_BUFF = new Object();
        private String source = "rebelrats_FuriousBrawlerListener";
        private String SKILL_ID = "rebelrats_furious_brawler";
        private String ICON_ID = "";
        public rebelrats_FuriousBrawlerListener(CombatEngineAPI engine,ShipAPI ship){
            this.engine = engine;
            this.ship = ship;
        }
        @Override
        public void advance(float amount) {
            if(engine == null) return;
            if(ship == null) return;
            if(engine.isPaused()) return;
            if(!gotMultiplier){
                gotMultiplier = true;
                switch(ship.getHullSize()){
                    case FRIGATE:
                        DAMAGE_MULTIPLIER_CAP = DAMAGE_MULTIPLIER_FRIGATE;
                        break;
                    case DESTROYER:
                        DAMAGE_MULTIPLIER_CAP = DAMAGE_MULTIPLIER_DESTROYER;
                        break;
                    case CRUISER:
                        DAMAGE_MULTIPLIER_CAP = DAMAGE_MULTIPLIER_DESTROYER;
                        break;
                    case CAPITAL_SHIP:
                        DAMAGE_MULTIPLIER_CAP = DAMAGE_MULTIPLIER_CAPITAL;
                        break;
                    default:
                        DAMAGE_MULTIPLIER_CAP = 1.8f;
                        break;
                }
                return;
            }

            if(!(engine.isEntityInPlay(ship)) || ship.isHulk()){
                cleanUpIfNeeded(ship);
                return;
            }

            applyMultiplier(ship);

            String icon = Global.getSettings().getSpriteName("ui", "icon_tactical_electronic_warfare");

            PersonAPI currCaptain = engine.getPlayerShip().getOriginalCaptain();
            if(currCaptain == null) return;
            if(currCaptain.getStats().hasSkill(SKILL_ID)){
                float multiplier = calculateMultiplier(ship);

                String isMax = "";
                if(multiplier == DAMAGE_MULTIPLIER_CAP) isMax = " (max)";
                String title = "FURIOUS BRAWLER SKILL BONUS";
                String data = "+" + (int)((multiplier - 1f)*100f) + "% weapon damage" + isMax;
                if(multiplier == 1){
                    data = "INACTIVE";
                }

                engine.maintainStatusForPlayerShip(KEY_WEAPON_BUFF,icon,title,data,false);
            }
        }
        private void applyMultiplier(ShipAPI ship){
            float multiplier = calculateMultiplier(ship);
            ship.getMutableStats().getBallisticWeaponDamageMult().modifyMult(source,multiplier);
            ship.getMutableStats().getEnergyWeaponDamageMult().modifyMult(source,multiplier);
            ship.getMutableStats().getBeamWeaponDamageMult().modifyMult(source,multiplier);
            ship.getMutableStats().getMissileWeaponDamageMult().modifyMult(source,multiplier);
        }
        private float calculateMultiplier(ShipAPI pilot){
            float multiplier = 1f;
            if(pilot.getShipTarget() == null) return multiplier;

            float dist = MathUtils.getDistance(pilot,pilot.getShipTarget());
            if(dist <= LOWER_CAP) dist = LOWER_CAP;

            if(dist <= HIGHER_CAP){
                float difference = HIGHER_CAP - LOWER_CAP;
                float formula = (DAMAGE_MULTIPLIER_CAP - 1f) - (dist - (difference - 1f)) / difference * (DAMAGE_MULTIPLIER_CAP - 1f);
                float rounded = (float)(Math.round(formula * 100.0) / 100.0);
                multiplier = 1f + rounded;
            }
            return multiplier;
        }
        private void cleanUpIfNeeded(ShipAPI ship){
            ship.getMutableStats().getBallisticWeaponDamageMult().unmodify(source);
            ship.getMutableStats().getEnergyWeaponDamageMult().unmodify(source);
            ship.getMutableStats().getBeamWeaponDamageMult().unmodify(source);
            ship.getMutableStats().getMissileWeaponDamageMult().unmodify(source);
        }
    }
}
