package data.scripts.campaign.econ.fleets;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.ai.FleetAssignmentDataAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.procgen.themes.RouteFleetAssignmentAI;
import com.fs.starfarer.api.util.CountingMap;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import java.util.Random;

public class rebelrats_constableAssignment extends RouteFleetAssignmentAI implements FleetActionTextProvider {
    public rebelrats_constableAssignment(CampaignFleetAPI fleet, RouteManager.RouteData route){
        super(fleet,route);
    }
    @Override
    protected void giveInitialAssignments() {

    }
    @Override
    public void advance(float amount) {
        super.advance(amount);
        checkCapture(amount);
        checkBuild(amount);
    }
    private SectorEntityToken pickEntityToGuard() {
        SectorEntityToken target = null;
        Random random = route.getRandom(1);

        MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        LocationAPI loc = fleet.getContainingLocation();
        if (loc == null) return null;

        WeightedRandomPicker<SectorEntityToken> picker = new WeightedRandomPicker<SectorEntityToken>(random);
        CountingMap<SectorEntityToken> existing = new CountingMap<SectorEntityToken>();
        for (RouteManager.RouteData data : RouteManager.getInstance().getRoutesForSource(route.getSource())) {
            CampaignFleetAPI other = data.getActiveFleet();
            if (other == null) continue;
            FleetAssignmentDataAPI curr = other.getCurrentAssignment();
            if (curr == null || curr.getTarget() == null ||
                    curr.getAssignment() != FleetAssignment.PATROL_SYSTEM) {
                continue;
            }
            existing.add(curr.getTarget());
        }
        return target;
    }
    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return "";
    }
}
