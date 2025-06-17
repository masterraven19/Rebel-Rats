package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Pair;

public class rebelrats_CheeseFactory extends BaseIndustry {

    @Override
    public void apply() {
        super.apply(true);
        int size = market.getSize();

        demand(Commodities.FOOD,size - 2);
        supply("rebelrats_cheese",size);
        modifyStabilityWithBaseMod();

        Pair<String,Integer> deficit = getMaxDeficit(Commodities.FOOD);
        applyDeficitToProduction(1, deficit, "rebelrats_cheese");

        if (!isFunctional()) {
            supply.clear();
        }
    }
    @Override
    public void unapply() {
        super.unapply();

        unmodifyStabilityWithBaseMod();
    }
    @Override
    protected boolean canImproveToIncreaseProduction() {
        return true;
    }
}
