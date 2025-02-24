package data.scripts.campaign.skills;

import com.fs.starfarer.api.characters.ShipSkillEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class rebelrats_GunneryDrills {
    public static class Level1 implements ShipSkillEffect {
        private float FIRERATE_MULTIPLIER = 1.1F;
        private float RECOIL_MULTIPLIER = 1.3F;
        @Override
        public void apply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id, float level) {
            stats.getRecoilPerShotMultSmallWeaponsOnly().modifyMult(id,RECOIL_MULTIPLIER);
            stats.getBallisticRoFMult().modifyMult(id,FIRERATE_MULTIPLIER);
        }

        @Override
        public void unapply(MutableShipStatsAPI stats, ShipAPI.HullSize hullSize, String id) {
            stats.getRecoilPerShotMultSmallWeaponsOnly().unmodify(id);
            stats.getBallisticRoFMult().unmodify(id);
        }

        @Override
        public String getEffectDescription(float level) {
            return "+" + (int) (FIRERATE_MULTIPLIER * 100f) + "% firerate and +"
                    + (int) (RECOIL_MULTIPLIER * 100f) + "% recoil for smalls.";
        }

        @Override
        public String getEffectPerLevelDescription() {
            return null;
        }

        @Override
        public ScopeDescription getScopeDescription() {
            return ScopeDescription.PILOTED_SHIP;
        }

    }
}
