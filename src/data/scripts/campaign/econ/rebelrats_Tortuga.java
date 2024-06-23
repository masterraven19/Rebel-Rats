package data.scripts.campaign.econ;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;

public class rebelrats_Tortuga extends BaseMarketConditionPlugin {
    private float stabilityb = 5;
    public void apply(String id) {
        market.getStability().modifyFlat(id, stabilityb, "Tortuga Base");
    }

    public void unapply(String id) {
        market.getStability().unmodify(id);
    }

}
