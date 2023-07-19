
package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.ai.CampaignFleetAIAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
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

import java.util.*;

public class rebelrats_dragonsblessing {
     public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Dragon's Blessing");
        system.getLocation().set(-39000,-26000); //top leftish -39000, 39000
        system.setEnteredByPlayer(true);
        system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");
        
        //entities 
        // create the star and generate the hyperspace anchor for this system
        PlanetAPI systemstar = system.initStar("Dragon's Blessing", // unique id for this star
        "star_yellow", // id in planets.json
        1100f, // radius (in pixels at default zoom)
        450); // corona radius, from star edge
        system.setLightColor(new Color(255,247,215)); // light color in entire system, affects all entities        
        
        //terrain
        system.addAsteroidBelt(systemstar, 0, 3000, 270f, 200, 250, Terrain.RING, "Hot Belt");
        system.addRingBand(systemstar, "misc", "rings_dust0", 270, 0, Color.yellow, 270, 3000, 200);
        
        system.addAsteroidBelt(systemstar, 400, 3500, 270f, 250, 300, Terrain.ASTEROID_BELT, "Astero Belt");
        system.addRingBand(systemstar, "misc", "rings_dust0", 270, 0, Color.yellow, 270, 3500, 300);
        
        system.addAsteroidBelt(systemstar, 0, 4000, 270f, 350, 400, Terrain.RING, "MARMOT ZZZZ");
        system.addRingBand(systemstar, "misc", "rings_dust0", 270, 0, Color.yellow, 270, 4000, 450);
        
        system.addAsteroidBelt(systemstar, 800, 15000, 270f, 600, 650, Terrain.ASTEROID_BELT, "Cold Belt");
        system.addRingBand(systemstar, "misc", "rings_dust0", 270, 0, Color.yellow, 270, 15000, 600);
        
        SectorEntityToken astfield1 = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                400f, // min radius
                500f, // max radius
                19, // min asteroid count
                30, // max asteroid count
                4f, // min asteroid radius
                16f, // max asteroid radius
                "Asteroids Field")); // null for default name
        astfield1.setCircularOrbit(systemstar, 360f * (float) Math.random(), 10000, 270);
        
        SectorEntityToken astfield2 = system.addTerrain(Terrain.ASTEROID_FIELD,
        new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                400f, // min radius
                500f, // max radius
                19, // min asteroid count
                30, // max asteroid count
                4f, // min asteroid radius
                16f, // max asteroid radius
                "Asteroids Field")); // null for default name
        astfield2.setCircularOrbit(systemstar, 360f * (float) Math.random(), 12000, 360);
        
        //planets
                       
        PlanetAPI malus = system.addPlanet("rebelrats_malus", 
                systemstar, 
                "Malus", 
                "arid", 
                200, 
                160f, 
                3250, 
                225);
        malus.setCustomDescriptionId("rebelrats_malus_planet"); //reference descriptions.csv
        malus.setFaction(Factions.PIRATES);
        
        PlanetAPI rattus = system.addPlanet("rebelrats_krysa",
                systemstar,
                "Krysa",
                Planets.PLANET_TERRAN,
                235,
                190f,
                7000,
                300);
        rattus.setCustomDescriptionId("rebelrats_rattus_planet"); //reference descriptions.csv
        rattus.setFaction("rebelrats");
        
        PlanetAPI magawa = system.addPlanet("rebelrats_magawa", 
                rattus, 
                "Magawa", 
                "barren", 
                150, 
                60f, 
                1000, 
                30);
        magawa.setCustomDescriptionId("rebelrats_magawa_moon"); //reference descriptions.csv
        magawa.setFaction("rebelrats");
        
        PlanetAPI rodentia = system.addPlanet("rebelrats_rodentia", 
                systemstar, 
                "Rodentia", 
                "rebelrats_fart_giant",
                221, 
                300f, 
                7000, 
                300);
        rodentia.setCustomDescriptionId("rebelrats_rodentia_planet"); //reference descriptions.csv
        rodentia.setFaction(Factions.NEUTRAL);
        
        system.addAsteroidBelt(rodentia, 0, 550, 270f, 20, 30, Terrain.RING, "Rodentia Belt");
        system.addRingBand(rodentia, "misc", "rings_dust0", 270, 0, Color.yellow, 270, 400, 20);
        
        PlanetAPI nutria = system.addPlanet("rebelrats_nutria", 
                systemstar, 
                "Nutria", 
                Planets.TUNDRA, 
                180, 
                150f, 
                18500,
                700);
        nutria.setCustomDescriptionId("rebelrats_nutria_planet"); //reference descriptions.csv
        nutria.setFaction(Factions.INDEPENDENT);
        
        //stations
        
        SectorEntityToken powerstation1 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation1.setCircularOrbitPointingDown(systemstar, 0, 1400, 200);
        powerstation1.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation2 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation2.setCircularOrbitPointingDown(systemstar, 90, 1400, 200);
        powerstation2.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation3 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation3.setCircularOrbitPointingDown(systemstar, 180, 1400, 200);
        powerstation3.setCustomDescriptionId("rebelrats_powerstation");
        
        SectorEntityToken powerstation4 = system.addCustomEntity("rebelrats_powerstation", "Power Station", "rebelrats_powerstation", "rebelrats");
        powerstation4.setCircularOrbitPointingDown(systemstar, 270, 1400, 200);
        powerstation4.setCustomDescriptionId("rebelrats_powerstation");

        SectorEntityToken outerstation = system.addCustomEntity("outerstation","Asteroid Central Station", "station_lowtech1","independent");
        outerstation.setCircularOrbit(systemstar, 90,15000,650);
        outerstation.setCustomDescriptionId("rebelrats_outerstation");

        //stable locs
        SectorEntityToken stableLoc1 = system.addCustomEntity("dragons_blessing_stableloc1", "Nav Buoy", "nav_buoy", "rebelrats");
        stableLoc1.setCircularOrbit(systemstar, 290, 14000, 520);
        
        SectorEntityToken stableLoc2 = system.addCustomEntity("dragons_blessing_stableloc2", "Comm Relay", "comm_relay", "rebelrats");
        stableLoc2.setCircularOrbit(systemstar, 195, 16000, 520);
        
        SectorEntityToken stableLoc3 = system.addCustomEntity("dragons_blessing_stableloc3", "Sensor Array", "sensor_array", "rebelrats");
        stableLoc3.setCircularOrbit(systemstar, 190, 5000, 520);
        //markets and conditions
        //krysa
        MarketAPI rattus_market = Global.getFactory().createMarket("rattus_market", "", 7);
        rattus.setMarket(rattus_market);
        rattus_market.setPrimaryEntity(rattus);
        rattus_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
                            
        rattus_market.setFactionId("rebelrats");
        rattus_market.setName("Krysa");
        rattus_market.getTariff().modifyFlat("generator", 0.3f);
        rattus.setInteractionImage("illustrations", "eventide");
        
        rattus_market.addCondition(Conditions.POPULATION_7);
        rattus_market.addCondition(Conditions.HABITABLE);
        rattus_market.addCondition(Conditions.TERRAN);
        rattus_market.addCondition(Conditions.ORE_MODERATE);
        rattus_market.addCondition(Conditions.FARMLAND_ADEQUATE);
        rattus_market.addCondition(Conditions.REGIONAL_CAPITAL);
        
        rattus_market.addSubmarket(Submarkets.GENERIC_MILITARY);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
        rattus_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        
        rattus_market.addIndustry(Industries.POPULATION);
        rattus_market.addIndustry(Industries.MEGAPORT, Collections.singletonList(Items.FULLERENE_SPOOL));
        rattus_market.addIndustry(Industries.FARMING);
        rattus_market.addIndustry(Industries.HIGHCOMMAND);
        rattus_market.addIndustry(Industries.STARFORTRESS);
        rattus_market.addIndustry(Industries.HEAVYBATTERIES);
        rattus_market.addIndustry(Industries.ORBITALWORKS, Collections.singletonList(Items.CORRUPTED_NANOFORGE));

        rattus_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        globalEconomy.addMarket(rattus_market, false);
        
        //malus
        MarketAPI malus_market = Global.getFactory().createMarket("malus_market", "", 5);
        malus.setMarket(malus_market);
        malus_market.setPrimaryEntity(malus);
        malus_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        
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

        globalEconomy.addMarket(malus_market, false);
        
        //nutria
        MarketAPI nutria_market = Global.getFactory().createMarket("nutria_market", "", 4);
        nutria.setMarket(nutria_market);
        nutria_market.setPrimaryEntity(nutria);
        nutria_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        
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
        
        globalEconomy.addMarket(nutria_market, false);

        MarketAPI magawa_market = Global.getFactory().createMarket("magawa_market", "", 5);
        magawa.setMarket(magawa_market);
        magawa_market.setPrimaryEntity(magawa);
        magawa_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);

        magawa_market.setFactionId("rebelrats");
        magawa_market.setName("Magawa");
        magawa_market.getTariff().modifyFlat("generator", 0.13f);
        magawa.setInteractionImage("illustrations", "mine");

        magawa_market.addCondition(Conditions.ORE_MODERATE);
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
        magawa_market.addIndustry(Industries.MINING);
        magawa_market.addIndustry(Industries.TECHMINING);
        magawa_market.addIndustry(Industries.SPACEPORT);
        magawa_market.addIndustry(Industries.WAYSTATION);
        magawa_market.addIndustry(Industries.GROUNDDEFENSES);
        magawa_market.addIndustry(Industries.ORBITALSTATION);
        magawa_market.addIndustry(Industries.MILITARYBASE);

        globalEconomy.addMarket(magawa_market, false);

        MarketAPI rodentia_market = Global.getFactory().createMarket("rodentia_market", "", 4);
        rodentia.setMarket(rodentia_market);
        rodentia_market.setPrimaryEntity(rodentia);
        rodentia_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);

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

        globalEconomy.addMarket(rodentia_market, false);

        MarketAPI outerstation_market = Global.getFactory().createMarket("outerstation_market", "", 4);
        outerstation.setMarket(outerstation_market);
        outerstation_market.setPrimaryEntity(outerstation);
        outerstation_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);

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

        globalEconomy.addMarket(outerstation_market,false);
        //persons 
       //* PersonAPI person = Global.getFactory().createPerson();
	//		person.setId(IBRAHIM);
	//		person.setFaction(Factions.INDEPENDENT);
	//		person.setGender(Gender.FEMALE);
	//		person.setRankId(Ranks.CITIZEN);
	//		person.setPostId(Ranks.POST_ENTREPRENEUR);
	//		person.setImportance(PersonImportance.HIGH);
	//		person.getName().setFirst("Callisto");
	//		person.getName().setLast("Ibrahim");
	//		person.addTag(Tags.CONTACT_TRADE);
	//		person.setVoice(Voices.SPACER);
	//		person.setPortraitSprite(Global.getSettings().getSpriteName("characters", person.getId()));
	//		person.getStats().setSkillLevel(Skills.SALVAGING, 1);
	//		person.getStats().setSkillLevel(Skills.BULK_TRANSPORT, 1);
	//		person.getStats().setSkillLevel(Skills.NAVIGATION, 1);
	//		
	//		rattus_market.setAdmin(person);
	//		rattus_market.getCommDirectory().addPerson(person, 0);
	//		rattus_market.addPerson(person);
        //jumpoints
        JumpPointAPI jump1 = Global.getFactory().createJumpPoint("lower_jaw",
                "Dragon's Lower Jaw");
        jump1.setStandardWormholeToHyperspaceVisual();
        jump1.setCircularOrbit(rattus, 235, 4500f, 300);
        jump1.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump1);

        JumpPointAPI jump2 = Global.getFactory().createJumpPoint("upper_jaw",
                "Dragon's Upper Jaw");
        jump2.setStandardWormholeToHyperspaceVisual();
        jump2.setCircularOrbit(rattus, 235, 14000f, 300);
        jump2.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump2);

        //gate
        SectorEntityToken dragons_gate = system.addCustomEntity("dragons_gate",
                "Dragons Gate",
                "inactive_gate",
                null);
        dragons_gate.setCircularOrbit(systemstar,180,20000,3000);

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
