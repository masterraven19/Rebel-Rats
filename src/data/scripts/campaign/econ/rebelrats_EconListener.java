package data.scripts.campaign.econ;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

public class rebelrats_EconListener implements EconomyAPI.EconomyUpdateListener {

    @Override
    public void commodityUpdated(String commodityId) {

    }

    @Override
    public void economyUpdated() {
        String cheese = "rebelrats_cheese";
        String source = "rebelrats_EconListener";

        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()){
            if(market.hasIndustry("population")){
                market.getIndustry("population").getDemand(cheese).getQuantity().modifyFlat(source, market.getSize() - 2);
            }

            if(market.hasIndustry("farming")){
                market.getIndustry("farming").getSupply(cheese).getQuantity().modifyFlat(source, market.getSize() - 2);
            }
        }
    }

    @Override
    public boolean isEconomyListenerExpired() {
        return false;
    }
}
