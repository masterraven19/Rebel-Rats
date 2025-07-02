package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.DebugFlags;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.*;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import data.scripts.campaign.econ.fleets.rebelrats_constableAssignment;

import java.util.Random;

public class rebelrats_Constabulary extends MilitaryBase {
    private float officer_prob = 0.1f;
    private float defense_bonus = 0.1f;
    private float fleet_spawn_rate = 1f;
    private int max_light_fleet_num_bonus = 4;
    private int max_medium_fleet_num_bonus = 2;
    private int max_heavy_fleet_num_bonus = 2;
    private int heavy_fleet_size_clearance = 5;
    private int heavy_fleet_stability = 7;
    private int light = 1;
    private int medium = 0;
    private int heavy = 0;
    public boolean isFunctional() {
        return super.isFunctional();
    }
    public void apply() {
        int size = market.getSize();
        applyIncomeAndUpkeep(3);

        if (size <= 3) {
            light = 2;
            medium = 0;
            heavy = 0;
        } else if (size == 4) {
            light = 2;
            medium = 0;
            heavy = 0;
        } else if (size == 5) {
            light = 2;
            medium = 1;
            heavy = 1;
        } else if (size == 6) {
            light = 3;
            medium = 1;
            heavy = 1;
        } else if (size == 7) {
            light = 3;
            medium = 2;
            heavy = 2;
        } else if (size == 8) {
            light = 3;
            medium = 3;
            heavy = 3;
        } else if (size >= 9) {
            light = 4;
            medium = 3;
            heavy = 3;
        }

        demand(Commodities.SUPPLIES, size - 1);
        demand(Commodities.FUEL, size - 1 );
        demand(Commodities.SHIPS, size - 1 ); //more ships when less stability

        supply(Commodities.CREW, size);
        supply(Commodities.MARINES, 1);

        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).modifyFlat(getModId(), light);
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).modifyFlat(getModId(), medium);
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).modifyFlat(getModId(), heavy);

        modifyStabilityWithBaseMod();

        float mult = getDeficitMult(Commodities.SUPPLIES);
        String extra = "";
        if (mult != 1) {
            String com = getMaxDeficit(Commodities.SUPPLIES).one;
            extra = " (" + getDeficitText(com).toLowerCase() + ")";
        }
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
                .modifyMult(getModId(), 1f + defense_bonus * mult, getNameForModifier() + extra);
        market.getStats().getDynamic().getMod(Stats.OFFICER_PROB_MOD).modifyFlat(getModId(0), officer_prob);

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), true, -1);

        if (!isFunctional()) {
            supply.clear();
            unapply();
        }
    }
    public void advance(float amount) {
        super.advance(amount);

        if (Global.getSector().getEconomy().isSimMode()) return;

        if (!isFunctional()) return;

        float days = Global.getSector().getClock().convertToDays(amount);

        float spawnRate = fleet_spawn_rate;
        float rateMult = market.getStats().getDynamic().getStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT).getModifiedValue();
        spawnRate *= rateMult;

        if (Global.getSector().isInNewGameAdvance()) {
            spawnRate *= 3f;
        }

        float extraTime = 0f;
        if (returningPatrolValue > 0) {
            // apply "returned patrols" to spawn rate, at a maximum rate of 1 interval per day
            float interval = tracker.getIntervalDuration();
            extraTime = interval * days;
            returningPatrolValue -= days;
            if (returningPatrolValue < 0) returningPatrolValue = 0;
        }
        tracker.advance(days * spawnRate + extraTime);

        //DebugFlags.FAST_PATROL_SPAWN = true;
        if (DebugFlags.FAST_PATROL_SPAWN) {
            tracker.advance(days * spawnRate * 100f);
        }

        if (tracker.intervalElapsed()) {
            float stability = market.getStabilityValue();
            float ratio;
            int size = market.getSize();
            int light_num = 0;
            int medium_num = 0;
            int heavy_num = 0;

            if(size > heavy_fleet_size_clearance && stability >= heavy_fleet_stability){
                heavy_num = max_heavy_fleet_num_bonus;
                light_num++;
            }
            if(stability < heavy_fleet_stability){
                ratio = 1f - (stability / heavy_fleet_stability);
                light_num = Math.round((ratio * max_light_fleet_num_bonus));
                medium_num = Math.round((ratio * max_medium_fleet_num_bonus));
            }

            market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).modifyFlat(getModId(), light + light_num);
            market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).modifyFlat(getModId(), medium + medium_num);
            market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).modifyFlat(getModId(), heavy + heavy_num);

            String sid = getRouteSourceId();

            int light = getCount(FleetFactory.PatrolType.FAST);
            int medium = getCount(FleetFactory.PatrolType.COMBAT);
            int heavy = getCount(FleetFactory.PatrolType.HEAVY);

            int maxLight = getMaxPatrols(FleetFactory.PatrolType.FAST);
            int maxMedium = getMaxPatrols(FleetFactory.PatrolType.COMBAT);
            int maxHeavy = getMaxPatrols(FleetFactory.PatrolType.HEAVY);

            WeightedRandomPicker<FleetFactory.PatrolType> picker = new WeightedRandomPicker<FleetFactory.PatrolType>();//
            picker.add(FleetFactory.PatrolType.HEAVY, maxHeavy - heavy);
            picker.add(FleetFactory.PatrolType.COMBAT, maxMedium - medium);
            picker.add(FleetFactory.PatrolType.FAST, maxLight - light);

            if (picker.isEmpty()) return;

            FleetFactory.PatrolType type = picker.pick();
            MilitaryBase.PatrolFleetData custom = new MilitaryBase.PatrolFleetData(type);

            RouteManager.OptionalFleetData extra = new RouteManager.OptionalFleetData(market);
            extra.fleetType = type.getFleetType();

            RouteManager.RouteData route = RouteManager.getInstance().addRoute(sid, market, Misc.genRandomSeed(), extra, this, custom);
            extra.strength = (float) getPatrolCombatFP(type, route.getRandom());
            extra.strength = Misc.getAdjustedStrength(extra.strength, market);


            float patrolDays = 35f + (float) Math.random() * 10f;
            route.addSegment(new RouteManager.RouteSegment(patrolDays, market.getPrimaryEntity()));
        }
    }
    public boolean isAvailableToBuild() {
        boolean canBuild = true;
        for (Industry ind : market.getIndustries()) {
            if (ind == this) {
                canBuild = false;
                break;
            }
            if (!ind.isFunctional()) continue;
            if (ind.getId() == Industries.PATROLHQ || ind.getId() == Industries.MILITARYBASE || ind.getId() == Industries.HIGHCOMMAND){
                canBuild = false;
                break;
            }
        }
        return canBuild;
    }
    public String getUnavailableReason() {
        return "Can not be built with Patrol HQ and higher.";
    }

    public CampaignFleetAPI spawnFleet(RouteManager.RouteData route) {
        MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
        FleetFactory.PatrolType type = custom.type;

        Random random = route.getRandom();

        CampaignFleetAPI fleet = createPatrol(type, market.getFactionId(), route, market, null, random);

        if (fleet == null || fleet.isEmpty()) return null;

        fleet.addEventListener(this);

        market.getContainingLocation().addEntity(fleet);
        fleet.setFacing((float) Math.random() * 360f);
        // this will get overridden by the patrol assignment AI, depending on route-time elapsed etc
        fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        fleet.addScript(new rebelrats_constableAssignment(fleet, route));

        fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, true, 0.3f);

        //market.getContainingLocation().addEntity(fleet);
        //fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        if (custom.spawnFP <= 0) {
            custom.spawnFP = fleet.getFleetPoints();
        }

        return fleet;
    }
}
