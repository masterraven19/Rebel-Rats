package data.scripts.hullmods;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.HullModFleetEffect;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.campaign.rebelrats_campaignUtils;

import java.awt.*;

public class rebelrats_HyperSpectralCamoNet extends BaseHullMod implements HullModFleetEffect {
    private final float SENSOR_MULT = 0.4f;

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        if (isForModSpec || ship == null) return;
        if (Global.getSettings().getCurrentState() == GameState.TITLE) return;

        CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();

        float pad = 5f;
        Color highlight = Misc.getHighlightColor();

        String biggest = rebelrats_campaignUtils.getBiggestShip(fleet,"rebelrats_jerboa").getShipName();
        if(biggest == null) biggest = "None";
        biggest = " " + biggest;

        tooltip.addPara("The biggest ship is" + biggest,
                pad,highlight,""+biggest);
    }

    @Override
    public void advanceInCampaign(CampaignFleetAPI fleet) {
        String key = "$updatedRebelratsJerboaModifier";
        if (fleet.isPlayerFleet() && fleet.getMemoryWithoutUpdate() != null &&
                !fleet.getMemoryWithoutUpdate().getBoolean(key) &&
                fleet.getMemoryWithoutUpdate().getBoolean(MemFlags.JUST_TOGGLED_TRANSPONDER)) {
            onFleetSync(fleet);
            fleet.getMemoryWithoutUpdate().set(key, true, 0.1f);
        }
    }

    @Override
    public boolean withAdvanceInCampaign() {
        return true;
    }

    @Override
    public boolean withOnFleetSync() {
        return true;
    }

    @Override
    public void onFleetSync(CampaignFleetAPI fleet) {
        if(fleet != Global.getSector().getPlayerFleet()) return;

        float multiplier = getMult(fleet);
        if(fleet.isTransponderOn()){multiplier = 0f;}
        if(multiplier > 0){
            fleet.getStats().getDetectedRangeMod().modifyMult(this.getClass().getName(),1 - (multiplier * SENSOR_MULT),"Jerboa Camo Net");
        }else{
            fleet.getStats().getDetectedRangeMod().unmodifyMult(this.getClass().getName());
        }
    }
    private float getMult(CampaignFleetAPI fleet){
        float shipsWithMod = 0;
        float currSize = 0;

        for(FleetMemberAPI m : fleet.getMembersWithFightersCopy()){
            if(m.getHullSpec().isBuiltInMod("rebelrats_hyperspectralcamonet")){
                shipsWithMod++;
            }
        }

        if(shipsWithMod == 0) return 0;

        float multiplier = 0;
        FleetMemberAPI biggestShip = rebelrats_campaignUtils.getBiggestShip(fleet,"rebelrats_jerboa");
        if(biggestShip == null){
            return multiplier = 0.375f;
        }
        switch(biggestShip.getHullSpec().getHullSize()){
            case FRIGATE:
                currSize = 1;
                multiplier = 1f;
                break;
            case DESTROYER:
                currSize = 2;
                multiplier = 0.625f;
                break;
            case CRUISER:
                currSize = 3;
                multiplier = 0.375f;
                break;
            case CAPITAL_SHIP:
                currSize = 4;
                multiplier = 0.25f;
                break;
        }

        return multiplier;
    }
}
