package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.awt.Color;

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

        getIncome().modifyFlat(id,30000);

        if (!isFunctional()) {
            supply.clear();
        }
    }
    @Override
    public void unapply() {
        super.unapply();

        getIncome().unmodify(id);
    }
    @Override
    protected void addPostDescriptionSection(TooltipMakerAPI tooltip, IndustryTooltipMode mode) {
        super.addPostDescriptionSection(tooltip, mode);
        String credits = "30,000";
        Color highlight = Misc.getHighlightColor();
        tooltip.addPara("Exports from this industry generates " + credits + " credits.",10,highlight,credits);
    }

    @Override
    protected boolean canImproveToIncreaseProduction() {
        return true;
    }
}
