package data.scripts.campaign.econ;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class rebelrats_Powerstation extends BaseMarketConditionPlugin {
    private float accessb = 0.2F;
    private float accessbtooltip = accessb * 100;
    private float upkeepmod = 0.8F;

    public void apply(String id) {
        market.getAccessibilityMod().modifyFlat(id,accessb,"Krysan Aid");
        for (Industry i : market.getIndustries()){
            i.getUpkeep().modifyMult(id,upkeepmod,"Krysan Aid");
        }
    }

    public void unapply(String id) {
        market.getAccessibilityMod().unmodify(id);
        for (Industry i : market.getIndustries()){
            i.getUpkeep().unmodify(id);
        }
    }

    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);

        tooltip.addPara("%s accessibility",
                10f, Misc.getHighlightColor(),
                "+" + (int)accessbtooltip + "%");
        tooltip.addPara("%s upkeep",
                10f,Misc.getHighlightColor(),
                "-" + (1 - (int)upkeepmod)*10  + "%");
    }
}
