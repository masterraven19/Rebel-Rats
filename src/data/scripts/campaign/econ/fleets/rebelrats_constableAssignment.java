package data.scripts.campaign.econ.fleets;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.ai.FleetAssignmentDataAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAIV4;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.util.CountingMap;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;

import java.util.List;
import java.util.Random;

public class rebelrats_constableAssignment extends PatrolAssignmentAIV4 {
    public rebelrats_constableAssignment(CampaignFleetAPI fleet, RouteManager.RouteData route){
        super(fleet,route);
    }
    public SectorEntityToken pickEntityToGuard() {
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

        List<MarketAPI> markets = Misc.getMarketsInLocation(fleet.getContainingLocation());
        int hostileMax = 0;
        int ourMax = 0;
        for (MarketAPI market : markets) {
            if (market.getFaction().isHostileTo(fleet.getFaction())) {
                hostileMax = Math.max(hostileMax, market.getSize());
            } else if (market.getFaction() == fleet.getFaction()) {
                ourMax = Math.max(ourMax, market.getSize());
            }
        }
        boolean inControl = ourMax > hostileMax;

        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.OBJECTIVE)) {
            if (entity.getFaction() != fleet.getFaction()) continue;

            float w = 2f;
            for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }

        // patrol stable locations, will build there
        for (SectorEntityToken entity : loc.getEntitiesWithTag(Tags.STABLE_LOCATION)) {
            float w = 2f;
            for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

            if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

            picker.add(entity, w);
        }

        if (inControl) {
            for (SectorEntityToken entity : loc.getJumpPoints()) {
                float w = 2f;
                for (int i = 0; i < existing.getCount(entity); i++) w *= 0.1f;

                if (type == FleetFactory.PatrolType.HEAVY) w *= 0.1f;

                picker.add(entity, w);
            }

            if (loc instanceof StarSystemAPI && custom.type == FleetFactory.PatrolType.HEAVY) {
                StarSystemAPI system = (StarSystemAPI) loc;
                if (system.getHyperspaceAnchor() != null) {
                    float w = 3f;
                    for (int i = 0; i < existing.getCount(system.getHyperspaceAnchor()); i++) w *= 0.1f;
                    picker.add(system.getHyperspaceAnchor(), w);
                }
            }
        }

        for (MarketAPI market : markets) {
            if (market.getFaction().isHostileTo(fleet.getFaction())) continue;

            float w = 0f;
            if (market == route.getMarket()) {
                w = 5f;
            } else {
                // defend on-hostile non-military markets; prefer own faction
                if (market.getMemoryWithoutUpdate().getBoolean(MemFlags.MARKET_PATROL)) {
                    if (market.getFaction() != fleet.getFaction()) {
                        w = 0f; // don't patrol near patrolHQ/military markets of another faction
                    } else {
                        w = 4f;
                    }
                }
            }

            for (int i = 0; i < existing.getCount(market.getPrimaryEntity()); i++) w *= 0.1f;
            picker.add(market.getPrimaryEntity(), w);
        }

        if (fleet.getContainingLocation() instanceof StarSystemAPI && type != FleetFactory.PatrolType.HEAVY) {
            StarSystemAPI system = (StarSystemAPI) fleet.getContainingLocation();
            float w = 1f;
            for (int i = 0; i < existing.getCount(system.getCenter()); i++) w *= 0.1f;
            picker.add(system.getCenter(), w);

            float w2 = 3.5f;
            for (SectorEntityToken fleetInSystem : system.getFleets()){
                if (fleetInSystem.getMemoryWithoutUpdate().contains(MemFlags.MEMORY_KEY_TRADE_FLEET)){
                    for (int i = 0; i < existing.getCount(fleetInSystem); i++) w2 *= 0.1f;
                    picker.add(fleetInSystem,w2);
                }
            }
        }

        SectorEntityToken target = picker.pick();
        if (target == null) {
            target = route.getMarket().getPrimaryEntity();
        }

        return target;
    }
}
