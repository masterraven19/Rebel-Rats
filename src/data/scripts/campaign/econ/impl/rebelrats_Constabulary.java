package data.scripts.campaign.econ.impl;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.DebugFlags;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.*;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteFleetSpawner;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

public class rebelrats_Constabulary extends BaseIndustry implements RouteFleetSpawner, FleetEventListener {
    private float officer_prob = 0.1f;
    private float defense_bonus = 0.1f;
    private float fleet_spawn_rate = 1f;
    private int max_light_fleet_num_bonus = 4;
    private int max_medium_fleet_num_bonus = 2;
    private int max_heavy_fleet_num_bonus = 2;
    private int heavy_fleet_size_clearance = 5;
    private int heavy_fleet_stability = 9;
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
    public void unapply() {
        super.unapply();

        MemoryAPI memory = market.getMemoryWithoutUpdate();
        Misc.setFlagWithReason(memory, MemFlags.MARKET_PATROL, getModId(), false, -1);

        unmodifyStabilityWithBaseMod();

        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).unmodifyFlat(getModId());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).unmodifyFlat(getModId());
        market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).unmodifyFlat(getModId());

        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(getModId());

        market.getStats().getDynamic().getMod(Stats.OFFICER_PROB_MOD).unmodifyFlat(getModId(0));
    }
    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
        return mode != IndustryTooltipMode.NORMAL || isFunctional();
    }
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            addStabilityPostDemandSection(tooltip, hasDemand, mode);

            addGroundDefensesImpactSection(tooltip, defense_bonus, Commodities.SUPPLIES);
        }
    }
    protected int getBaseStabilityMod() {
        int stability_mod = 1;
        return stability_mod;
    }
    public String getNameForModifier() {
        return getSpec().getName();
    }
    protected Pair<String, Integer> getStabilityAffectingDeficit() {
        return getMaxDeficit(Commodities.SUPPLIES, Commodities.FUEL, Commodities.SHIPS);
    }
    public boolean isDemandLegal(CommodityOnMarketAPI com) {
        return true;
    }
    public boolean isSupplyLegal(CommodityOnMarketAPI com) {
        return true;
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
    protected IntervalUtil tracker = new IntervalUtil(Global.getSettings().getFloat("averagePatrolSpawnInterval") * 0.7f,
            Global.getSettings().getFloat("averagePatrolSpawnInterval") * 1.3f);
    protected float returningPatrolValue = 0f;
    protected void buildingFinished() {
        super.buildingFinished();

        tracker.forceIntervalElapsed();
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
    public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, CampaignEventListener.FleetDespawnReason reason, Object param) {
        if (!isFunctional()) return;

        if (reason == CampaignEventListener.FleetDespawnReason.REACHED_DESTINATION) {
            RouteManager.RouteData route = RouteManager.getInstance().getRoute(getRouteSourceId(), fleet);
            if (route.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) route.getCustom();
                if (custom.spawnFP > 0) {
                    float fraction  = fleet.getFleetPoints() / custom.spawnFP;
                    returningPatrolValue += fraction;
                }
            }
        }
    }

    public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {

    }
    public static int getPatrolCombatFP(FleetFactory.PatrolType type, Random random) {
        float combat = 0;
        switch (type) {
            case FAST:
                combat = Math.round(3f + (float) random.nextFloat() * 2f) * 5f;
                break;
            case COMBAT:
                combat = Math.round(6f + (float) random.nextFloat() * 3f) * 5f;
                break;
            case HEAVY:
                combat = Math.round(10f + (float) random.nextFloat() * 5f) * 5f;
                break;
        }
        return (int) Math.round(combat);
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

        fleet.addScript(new PatrolAssignmentAIV4(fleet, route));

        fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_IGNORES_OTHER_FLEETS, true, 0.3f);

        //market.getContainingLocation().addEntity(fleet);
        //fleet.setLocation(market.getPrimaryEntity().getLocation().x, market.getPrimaryEntity().getLocation().y);

        if (custom.spawnFP <= 0) {
            custom.spawnFP = fleet.getFleetPoints();
        }

        return fleet;
    }
    public static CampaignFleetAPI createPatrol(FleetFactory.PatrolType type, String factionId, RouteManager.RouteData route, MarketAPI market, Vector2f locInHyper, Random random) {
        if (random == null) random = new Random();


        float combat = getPatrolCombatFP(type, random);
        float tanker = 0f;
        float freighter = 0f;
        String fleetType = type.getFleetType();
        switch (type) {
            case FAST:
                break;
            case COMBAT:
                tanker = Math.round((float) random.nextFloat() * 5f);
                break;
            case HEAVY:
                tanker = Math.round((float) random.nextFloat() * 10f);
                freighter = Math.round((float) random.nextFloat() * 10f);
                break;
        }

        FleetParamsV3 params = new FleetParamsV3(
                market,
                locInHyper,
                factionId,
                route == null ? null : route.getQualityOverride(),
                fleetType,
                combat, // combatPts
                freighter, // freighterPts
                tanker, // tankerPts
                0f, // transportPts
                0f, // linerPts
                0f, // utilityPts
                0f // qualityMod
        );
        if (route != null) {
            params.timestamp = route.getTimestamp();
        }
        params.random = random;
        CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);

        if (fleet == null || fleet.isEmpty()) return null;

        if (!fleet.getFaction().getCustomBoolean(Factions.CUSTOM_PATROLS_HAVE_NO_PATROL_MEMORY_KEY)) {
            fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PATROL_FLEET, true);
            if (type == FleetFactory.PatrolType.FAST || type == FleetFactory.PatrolType.COMBAT) {
                fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_CUSTOMS_INSPECTOR, true);
            }
        } else if (fleet.getFaction().getCustomBoolean(Factions.CUSTOM_PIRATE_BEHAVIOR)) {
            fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PIRATE, true);

            // hidden pather and pirate bases
            // make them raid so there's some consequence to just having a colony in a system with one of those
            if (market != null && market.isHidden()) {
                fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_RAIDER, true);
            }
        }

        String postId = Ranks.POST_PATROL_COMMANDER;
        String rankId = Ranks.SPACE_COMMANDER;
        switch (type) {
            case FAST:
                rankId = Ranks.SPACE_LIEUTENANT;
                break;
            case COMBAT:
                rankId = Ranks.SPACE_COMMANDER;
                break;
            case HEAVY:
                rankId = Ranks.SPACE_CAPTAIN;
                break;
        }

        fleet.getCommander().setPostId(postId);
        fleet.getCommander().setRankId(rankId);

        return fleet;
    }
    public boolean shouldCancelRouteAfterDelayCheck(RouteManager.RouteData route) {
        return false;
    }

    public boolean shouldRepeat(RouteManager.RouteData route) {
        return false;
    }
    public int getMaxPatrols(FleetFactory.PatrolType type) {
        if (type == FleetFactory.PatrolType.FAST) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.COMBAT) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).computeEffective(0);
        }
        if (type == FleetFactory.PatrolType.HEAVY) {
            return (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).computeEffective(0);
        }
        return 0;
    }
    public int getCount(FleetFactory.PatrolType... types) {
        int count = 0;
        for (RouteManager.RouteData data : RouteManager.getInstance().getRoutesForSource(getRouteSourceId())) {
            if (data.getCustom() instanceof MilitaryBase.PatrolFleetData) {
                MilitaryBase.PatrolFleetData custom = (MilitaryBase.PatrolFleetData) data.getCustom();
                for (FleetFactory.PatrolType type : types) {
                    if (type == custom.type) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
    public String getRouteSourceId() {
        return getMarket().getId() + "_" + "military";
    }
    public void reportAboutToBeDespawnedByRouteManager(RouteManager.RouteData route) {

    }
}
