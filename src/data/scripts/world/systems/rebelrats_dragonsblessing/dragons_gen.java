
package data.scripts.world.systems.rebelrats_dragonsblessing;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.ai.CampaignFleetAIAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.Collections;

public class dragons_gen {
     public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Dragon's Blessing");
        system.getLocation().set(-39000,-26000); //top leftish -39000, 39000
        system.setEnteredByPlayer(true);
        system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");

        //entities 
        // create the star and generate the hyperspace anchor for this system
        // the yellow!! giant
        // to make the orbit days consistent I will divide the radius from whatever it orbits
        // by 30
        PlanetAPI systemstar = system.initStar("Dragon's Blessing", // unique id for this star
        "star_yellow", // id in planets.json
        1100f, // radius (in pixels at default zoom)
        450); // corona radius, from star edge
        //flood them in yellow
        system.setLightColor(new Color(255,247,215)); // light color in entire system, affects all entities        
        
        //terrain
        //asteroid bands
        //first ring
        system.addAsteroidBelt(systemstar, 0, 2250, 270f, 74, 75, Terrain.RING, "Hot Belt");
        system.addRingBand(systemstar, "misc", "rings_asteroids0", 270, 2, Color.lightGray, 270, 2250, 75);
        //second ring
        system.addAsteroidBelt(systemstar, 100, 2500, 270f, 82, 83, Terrain.ASTEROID_BELT, "Astero Belt");
        system.addRingBand(systemstar, "misc", "rings_asteroids0", 270, 2, Color.darkGray, 270, 2500, 83);
        //third ring
        system.addAsteroidBelt(systemstar, 0, 2900, 230f, 95, 96, Terrain.RING, "Mozarella");
        system.addRingBand(systemstar, "misc", "rings_dust0", 230, 0, Color.lightGray, 230, 2900, 96);
        //fourth ring (infinity rings)
        system.addAsteroidBelt(systemstar, 400, 6900, 200f, 229, 230, Terrain.ASTEROID_BELT, "Cold Belt");
        system.addRingBand(systemstar, "misc", "rings_dust0", 200, 0, Color.yellow, 200, 6900, 230);
        
        SectorEntityToken astfield1 = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                500f, // min radius
                600f, // max radius
                30, // min asteroid count
                60, // max asteroid count
                4f, // min asteroid radius
                19f, // max asteroid radius
                "Asteroids Field")); // null for default name
        astfield1.setCircularOrbit(systemstar, 270, 4900, 163);
        
        SectorEntityToken astfield2 = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                400f, // min radius
                500f, // max radius
                19, // min asteroid count
                30, // max asteroid count
                4f, // min asteroid radius
                16f, // max asteroid radius
                "Asteroids Field")); // null for default name
        astfield2.setCircularOrbit(systemstar, 0, 4900, 163);
        
        //planets
        //pirates!!!
        PlanetAPI malus = system.addPlanet("rebelrats_malus", 
                systemstar, 
                "Malus", 
                "arid", 
                200, 
                140f,
                2700,
                90);
        malus.setCustomDescriptionId("rebelrats_malus_planet"); //reference descriptions.csv
        malus.setFaction(Factions.PIRATES);
        //stinky
        PlanetAPI rodentia = system.addPlanet("rebelrats_rodentia",
                systemstar,
                "Rodentia",
                "rebelrats_fart_giant",
                260,
                300f,
                4800,
                160);
        rodentia.setCustomDescriptionId("rebelrats_rodentia_planet"); //reference descriptions.csv
        rodentia.setFaction("rebelrats");
        //the rebel rats home
        PlanetAPI rattus = system.addPlanet("rebelrats_krysa",
                rodentia,
                "Krysa",
                Planets.PLANET_TERRAN,
                235,
                170f,
                1300,
                43);
        rattus.setCustomDescriptionId("rebelrats_rattus_planet"); //reference descriptions.csv
        rattus.setFaction("rebelrats");
        //the rats mine
        PlanetAPI magawa = system.addPlanet("rebelrats_magawa", 
                rodentia,
                "Magawa", 
                "barren", 
                235,
                70f,
                1800,
                60);
        magawa.setCustomDescriptionId("rebelrats_magawa_moon"); //reference descriptions.csv
        magawa.setFaction("rebelrats");
        
        system.addAsteroidBelt(rodentia, 0, 550, 270f, 8, 9, Terrain.RING, "Rodentia Belt");
        system.addRingBand(rodentia, "misc", "rings_dust0", 270, 0, Color.lightGray, 270, 550, 9);
        //Outer Belters!!
        PlanetAPI nutria = system.addPlanet("rebelrats_nutria", 
                systemstar, 
                "Nutria", 
                Planets.TUNDRA, 
                180, 
                150f, 
                7900,
                263);
        nutria.setCustomDescriptionId("rebelrats_nutria_planet"); //reference descriptions.csv
        nutria.setFaction(Factions.INDEPENDENT);
        
        //stations
        
        SectorEntityToken powerstation1 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation1.setCircularOrbitPointingDown(systemstar, 0, 1400, 46);
        powerstation1.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation2 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation2.setCircularOrbitPointingDown(systemstar, 90, 1400, 46);
        powerstation2.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation3 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation3.setCircularOrbitPointingDown(systemstar, 180, 1400, 46);
        powerstation3.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation4 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation4.setCircularOrbitPointingDown(systemstar, 270, 1400, 46);
        powerstation4.setCustomDescriptionId("rebelrats_powerstation");

        SectorEntityToken outerstation = system.addCustomEntity("outerstation","Asteroid Central Station", "station_lowtech1","independent");
        outerstation.setCircularOrbit(systemstar, 90,6900,230);
        outerstation.setCustomDescriptionId("rebelrats_outerstation");

        //stable locs
        SectorEntityToken stableLoc1 = system.addCustomEntity("dragons_blessing_stableloc1", "Nav Buoy", "nav_buoy", "rebelrats");
        stableLoc1.setCircularOrbit(systemstar, 290, 6600, 220);
        
        SectorEntityToken stableLoc2 = system.addCustomEntity("dragons_blessing_stableloc2", "Comm Relay", "comm_relay", "rebelrats");
        stableLoc2.setCircularOrbit(systemstar, 135, 7200, 240);
        
        SectorEntityToken stableLoc3 = system.addCustomEntity("dragons_blessing_stableloc3", "Sensor Array", "sensor_array", "rebelrats");
        stableLoc3.setCircularOrbit(systemstar, 45, 6600, 220);

        //markets and conditions
        //krysa
        MarketAPI rattus_market = Global.getFactory().createMarket("rattus_market", "", 7);
        rattus.setMarket(rattus_market);
        rattus_market.setPrimaryEntity(rattus);
                            
        rattus_market.setFactionId("rebelrats");
        rattus_market.setName("Krysa");
        rattus_market.getTariff().modifyFlat("generator", 0.3f);
        rattus.setInteractionImage("illustrations", "eventide");
        
        rattus_market.addCondition(Conditions.POPULATION_7);
        rattus_market.addCondition(Conditions.HABITABLE);
        rattus_market.addCondition(Conditions.TERRAN);
        rattus_market.addCondition(Conditions.ORE_MODERATE);
        rattus_market.addCondition(Conditions.FARMLAND_BOUNTIFUL);
        rattus_market.addCondition(Conditions.REGIONAL_CAPITAL);
        
        rattus_market.addSubmarket(Submarkets.GENERIC_MILITARY);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        
        rattus_market.addIndustry(Industries.POPULATION);
        rattus_market.addIndustry(Industries.MEGAPORT, Collections.singletonList(Items.FULLERENE_SPOOL));
        rattus_market.addIndustry(Industries.FARMING, Collections.singletonList(Items.SOIL_NANITES));
        rattus_market.addIndustry(Industries.LIGHTINDUSTRY);
        rattus_market.addIndustry(Industries.HIGHCOMMAND);
        rattus_market.addIndustry("rebelrats_starfortress");
        rattus_market.addIndustry(Industries.HEAVYBATTERIES);
        rattus_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.CORRUPTED_NANOFORGE));

        rattus_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.GAMMA_CORE);
        rattus_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);
        rattus_market.getIndustry("rebelrats_starfortress").setAICoreId(Commodities.ALPHA_CORE);
        rattus_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        globalEconomy.addMarket(rattus_market, false);
        
        //malus
        MarketAPI malus_market = Global.getFactory().createMarket("rebelrats_malus_market", "", 5);
        malus.setMarket(malus_market);
        malus_market.setPrimaryEntity(malus);
        
        malus_market.setFactionId(Factions.PIRATES);
        malus_market.setName("Malus");
        malus_market.getTariff().modifyFlat("generator", 0f);
        malus.setInteractionImage("illustrations", "desert_moons_ruins");
        
        malus_market.addCondition(Conditions.POPULATION_5);
        malus_market.addCondition(Conditions.ARID);
        malus_market.addCondition(Conditions.FREE_PORT);
        malus_market.addCondition(Conditions.FARMLAND_POOR);
        malus_market.addCondition(Conditions.ORE_SPARSE);
        malus_market.addCondition(Conditions.RUINS_EXTENSIVE);
        malus_market.addCondition("rebelrats_tortuga_mc");
        
        malus_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        malus_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        malus_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
        
        malus_market.addIndustry(Industries.POPULATION);
        malus_market.addIndustry(Industries.FARMING);
        malus_market.addIndustry(Industries.MINING);
        malus_market.addIndustry(Industries.SPACEPORT);
        malus_market.addIndustry(Industries.GROUNDDEFENSES);
        malus_market.addIndustry(Industries.PATROLHQ);

        malus_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(malus_market, false);
        
        //nutria
        MarketAPI nutria_market = Global.getFactory().createMarket("rebelrats_nutria_market", "", 4);
        nutria.setMarket(nutria_market);
        nutria_market.setPrimaryEntity(nutria);
        
        nutria_market.setFactionId(Factions.INDEPENDENT);
        nutria_market.setName("Nutria");
        nutria_market.getTariff().modifyFlat("generator", 0.13f);
        nutria.setInteractionImage("illustrations", "city_from_above");
        
        nutria_market.addCondition(Conditions.POPULATION_4);
        nutria_market.addCondition(Conditions.FREE_PORT);
        nutria_market.addCondition(Conditions.HABITABLE);
        nutria_market.addCondition(Conditions.FARMLAND_ADEQUATE);
        nutria_market.addCondition(Conditions.COLD);
        nutria_market.addCondition(Conditions.DARK);
         
        nutria_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        nutria_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        nutria_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
         
        nutria_market.addIndustry(Industries.POPULATION);
        nutria_market.addIndustry(Industries.SPACEPORT);
        nutria_market.addIndustry(Industries.GROUNDDEFENSES);
        nutria_market.addIndustry(Industries.PATROLHQ);
        nutria_market.addIndustry(Industries.WAYSTATION);
        nutria_market.addIndustry(Industries.FARMING);

        nutria_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(nutria_market, false);

        MarketAPI magawa_market = Global.getFactory().createMarket("rebelrats_magawa_market", "", 5);
        magawa.setMarket(magawa_market);
        magawa_market.setPrimaryEntity(magawa);

        magawa_market.setFactionId("rebelrats");
        magawa_market.setName("Magawa");
        magawa_market.getTariff().modifyFlat("generator", 0.13f);
        magawa.setInteractionImage("illustrations", "mine");

        magawa_market.addCondition(Conditions.ORE_RICH);
        magawa_market.addCondition(Conditions.RARE_ORE_RICH);
        magawa_market.addCondition(Conditions.NO_ATMOSPHERE);
        magawa_market.addCondition(Conditions.LOW_GRAVITY);
        magawa_market.addCondition(Conditions.RUINS_VAST);
        magawa_market.addCondition(Conditions.POPULATION_5);

        magawa_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        magawa_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        magawa_market.addSubmarket(Submarkets.GENERIC_MILITARY);
        magawa_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        magawa_market.addIndustry(Industries.POPULATION);
        magawa_market.addIndustry(Industries.MINING, Collections.singletonList(Items.MANTLE_BORE));
        magawa_market.addIndustry(Industries.REFINING, Collections.singletonList(Items.CATALYTIC_CORE));
        magawa_market.addIndustry(Industries.SPACEPORT);
        magawa_market.addIndustry(Industries.WAYSTATION);
        magawa_market.addIndustry(Industries.HEAVYBATTERIES, Collections.singletonList(Items.DRONE_REPLICATOR));
        magawa_market.addIndustry("rebelrats_starfortress");
        magawa_market.addIndustry(Industries.HIGHCOMMAND);

        magawa_market.getIndustry(Industries.MINING).setAICoreId(Commodities.GAMMA_CORE);
        magawa_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(magawa_market, false);

        MarketAPI rodentia_market = Global.getFactory().createMarket("rebelrats_rodentia_market", "", 4);
        rodentia.setMarket(rodentia_market);
        rodentia_market.setPrimaryEntity(rodentia);

        rodentia_market.setFactionId("rebelrats");
        rodentia_market.setName("Rodentia");
        rodentia_market.getTariff().modifyFlat("generator", 0.13f);
        rodentia.setInteractionImage("illustrations", "industrial_megafacility");

        rodentia_market.addCondition(Conditions.VOLATILES_PLENTIFUL);
        rodentia_market.addCondition(Conditions.POPULATION_4);
        rodentia_market.addCondition(Conditions.EXTREME_WEATHER);

        rodentia_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        rodentia_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        rodentia_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        rodentia_market.addIndustry(Industries.POPULATION);
        rodentia_market.addIndustry(Industries.SPACEPORT);
        rodentia_market.addIndustry(Industries.MINING);
        rodentia_market.addIndustry(Industries.FUELPROD, Collections.singletonList(Items.SYNCHROTRON));
        rodentia_market.addIndustry(Industries.GROUNDDEFENSES);
        rodentia_market.addIndustry(Industries.ORBITALSTATION);

        rodentia_market.getIndustry(Industries.FUELPROD).setAICoreId(Commodities.BETA_CORE);
        rodentia_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(rodentia_market, false);

        MarketAPI outerstation_market = Global.getFactory().createMarket("rebelrats_outerstation_market", "", 4);
        outerstation.setMarket(outerstation_market);
        outerstation_market.setPrimaryEntity(outerstation);

        outerstation_market.setFactionId(Factions.INDEPENDENT);
        outerstation_market.setName(outerstation.getName());
        outerstation_market.getTariff().modifyFlat("generator", 0.13f);
        outerstation.setInteractionImage("illustrations", "free_orbit");

        outerstation_market.addCondition(Conditions.POPULATION_4);
        outerstation_market.addCondition(Conditions.ORE_MODERATE);

        outerstation_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        outerstation_market.addSubmarket(Submarkets.SUBMARKET_BLACK);

        outerstation_market.addIndustry(Industries.POPULATION);
        outerstation_market.addIndustry(Industries.SPACEPORT);
        outerstation_market.addIndustry(Industries.HEAVYBATTERIES);
        outerstation_market.addIndustry(Industries.WAYSTATION);
        outerstation_market.addIndustry(Industries.REFINING);
        outerstation_market.addIndustry(Industries.LIGHTINDUSTRY);
        outerstation_market.addIndustry(Industries.ORBITALSTATION);

        outerstation_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(outerstation_market,false);
        //persons
        //gen_NPCs.generate_FRO(rattus_market);
        //gen_NPCs.generate_Thackery(rattus_market);
        //moved to onNewGameAfterEconomyLoad()
        //jumpoints
        JumpPointAPI jump1 = Global.getFactory().createJumpPoint("lower_jaw",
                "Dragon's Lower Jaw");
        jump1.setStandardWormholeToHyperspaceVisual();
        jump1.setCircularOrbit(systemstar, 235, 3100f, 103);
        jump1.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump1);

        JumpPointAPI jump2 = Global.getFactory().createJumpPoint("upper_jaw",
                "Dragon's Upper Jaw");
        jump2.setStandardWormholeToHyperspaceVisual();
        jump2.setCircularOrbit(systemstar, 235, 7300f, 243);
        jump2.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump2);

        //gate
        SectorEntityToken dragons_gate = system.addCustomEntity("dragons_gate",
                "Dragons Gate",
                "inactive_gate",
                null);
        dragons_gate.setCircularOrbit(nutria,180,700,23);

        //fleet
        CampaignFleetAPI krysafleet = FleetFactoryV3.createEmptyFleet("rebelrats", FleetTypes.PATROL_LARGE,null);
        krysafleet.setName("Rat King's Right Hand");
        krysafleet.setMarket(rattus_market);

        PersonAPI captain = OfficerManagerEvent.createOfficer(rattus.getFaction(),10, OfficerManagerEvent.SkillPickPreference.NO_ENERGY_YES_BALLISTIC_YES_MISSILE_NO_DEFENSE,false,krysafleet,false,true,3,null);
        captain.getName().setFirst("Ben");
        captain.getName().setLast("Dover");
        captain.getName().setGender(FullName.Gender.MALE);
        captain.setPortraitSprite("portrait_ai2");
        captain.setPersonality("steady");


        krysafleet.getFleetData().addFleetMember("rebelrats_porcellus_Balanced");
        FleetMemberAPI porc1 = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"rebelrats_porcellus_Balanced");
        krysafleet.getFleetData().addFleetMember(porc1);

        krysafleet.getFleetData().setFlagship(porc1);

        krysafleet.getFleetData().addOfficer(captain);
        krysafleet.setCommander(captain);
        krysafleet.getFlagship().setCaptain(captain);

        krysafleet.getFleetData().sort();
        system.addEntity(krysafleet);
        krysafleet.setLocation(rattus.getLocation().x,rattus.getLocation().y);

        CampaignFleetAIAPI krysafleetai = Global.getFactory().createFleetAI(krysafleet);
        krysafleet.setAI(krysafleetai);
        krysafleetai.addAssignment(FleetAssignment.DEFEND_LOCATION,rattus,99999,"Patrolling Krysa",null);

        //extras
        system.addRadioChatter(rattus, 0);
        system.autogenerateHyperspaceJumpPoints(true, false);

        //create hyperspace around system
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
     }    
}
