package data.scripts.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.BaseCampaignObjectivePlugin;
import com.fs.starfarer.api.util.Misc;
import data.scripts.campaign.ids.rebelrats_Conditions;

public class rebelrats_powerstationEntityPlugin extends BaseCampaignObjectivePlugin {
    //private float accessbonus = 20;
    //private float upkeepbonus = 0.8F;
    //private int reqrep = 75;
    // market condition will do the effects to be less buggy
    private float elapsed = 0;
    public void init(SectorEntityToken entity, Object pluginParams) {
        super.init(entity, pluginParams);
        readResolve();
    }
    Object readResolve() {
        return this;
    }

    public void advance(float amount) {
        if (entity.getContainingLocation() == null || entity.isInHyperspace()) return;
        float dur = Global.getSector().getClock().convertToSeconds(1);
        elapsed += amount;

        if (elapsed > dur) {
            elapsed -= dur;
            String krysanOwner = Global.getSector().getEconomy().getMarket("rattus_market").getFactionId();
            float aidRange = 15f;

            if (!krysanOwner.matches("rebelrats")){
                aidRange = 20F;
            }

            for (MarketAPI m : Misc.getNearbyMarkets(entity.getLocationInHyperspace(), aidRange)) {
                boolean hasmc = m.hasCondition(rebelrats_Conditions.KRYSAN_AID);
                FactionAPI faction = m.getFaction();

                if(!hasmc){
                    if (faction.getId().matches(krysanOwner) || faction.getRelationship(krysanOwner) >= 0.3f){
                        m.addCondition(rebelrats_Conditions.KRYSAN_AID);
                    }
                }
                if(hasmc && !faction.getId().matches(krysanOwner) && faction.getRelationship(krysanOwner) < 0.3f){
                    m.removeCondition(rebelrats_Conditions.KRYSAN_AID);
                }

            }
        }
    }
}
