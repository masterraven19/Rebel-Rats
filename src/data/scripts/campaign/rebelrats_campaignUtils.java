package data.scripts.campaign;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import org.jetbrains.annotations.Nullable;

public class rebelrats_campaignUtils {
    public static FleetMemberAPI getBiggestShip(CampaignFleetAPI fleet, @Nullable String ToIgnore){
        String shipToIgnore = "";
        if(ToIgnore != null){
            shipToIgnore = ToIgnore;
        }
        FleetMemberAPI biggestShip = null;
        float biggestSize = 0;
        float currSize = 0;

        for(FleetMemberAPI m : fleet.getMembersWithFightersCopy()){
            if(m.getHullSpec().getHullSize() == ShipAPI.HullSize.FIGHTER) continue;
            if(m.getHullId().matches(shipToIgnore)) continue;
            switch(m.getHullSpec().getHullSize()){
                case FRIGATE:
                    currSize = 1;
                    break;
                case DESTROYER:
                    currSize = 2;
                    break;
                case CRUISER:
                    currSize = 3;
                    break;
                case CAPITAL_SHIP:
                    currSize = 4;
                    break;
            }
            if(currSize > biggestSize){
                biggestShip = m;
                biggestSize = currSize;
            }
        }

        return biggestShip;
    }
}
