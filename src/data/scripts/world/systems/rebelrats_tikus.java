package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;
import java.util.Collections;

public class rebelrats_tikus {
    public void generate(SectorAPI sector){
        StarSystemAPI system = sector.createStarSystem("Tikus");
        system.getLocation().set(-44000,-27000);
        system.setEnteredByPlayer(true);
        system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");
        EconomyAPI globalEconomy = Global.getSector().getEconomy();

        //yellow giant numba 2
        // will use orbit radius/30
        PlanetAPI systemstar = system.initStar(
                "Tikus",
                "star_yellow",
                1200f,
                600f);
        system.setLightColor(new Color(255,247,215));

        //asteroid rings / terrain
        system.addAsteroidBelt(systemstar,300,2000,260,66,28, Terrain.ASTEROID_BELT,"Brocconcini");
        system.addRingBand(systemstar,"misc","rings_asteroids0",260,1,Color.DARK_GRAY,260,2000,66);

        system.addAsteroidBelt(systemstar,700,6700,240,220,223, Terrain.ASTEROID_BELT,"Cotija");
        system.addRingBand(systemstar,"misc","rings_asteroids0",240,1,Color.DARK_GRAY,240,6700,223);

        //stable locs
        SectorEntityToken stableLoc1 = system.addCustomEntity("tikus_stableloc1", "Nav Buoy", "nav_buoy", "rebelrats");
        stableLoc1.setCircularOrbit(systemstar, 290, 4325, 144);

        //jump points
        JumpPointAPI jump1 = Global.getFactory().createJumpPoint("cabrales",
                "Cabrales");
        jump1.setStandardWormholeToHyperspaceVisual();
        jump1.setCircularOrbit(systemstar, 45, 2400f, 80);
        jump1.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump1);

        JumpPointAPI jump2 = Global.getFactory().createJumpPoint("gouda",
                "Gouda");
        jump2.setStandardWormholeToHyperspaceVisual();
        jump2.setCircularOrbit(systemstar, 110, 7100f, 236);
        jump2.setAutoCreateEntranceFromHyperspace(true);
        system.addEntity(jump2);
        //planets
        PlanetAPI tikus = system.addPlanet("rebelrats_tikus",
                systemstar,
                "Tikus I",
                "gas_giant",
                135,
                300,
                3500,
                116);
        tikus.setCustomDescriptionId("rebelrats_tikus_gasGiant");
        tikus.setFaction("rebelrats");

        SectorEntityToken stableLoc2 = system.addCustomEntity("tikus_stableloc2", "Comm Relay", "comm_relay", "rebelrats");
        stableLoc2.setCircularOrbit(tikus, 290, 600, 20);

        PlanetAPI tikusII = system.addPlanet("rebelrats_tikusII",
                systemstar,
                "Tikus II",
                "jungle",
                135,
                150,
                5150,
                171);
        tikusII.setCustomDescriptionId("rebelrats_tikusII_planet");
        tikusII.setFaction("rebelrats");

        PlanetAPI tikusIII = system.addPlanet("rebelrats_tikusIII",
                systemstar,
                "Tikus III",
                "cryovolcanic",
                135,
                140,
                8500,
                283);
        tikusIII.setCustomDescriptionId("rebelrats_tikusIII_planet");
        tikusIII.setFaction(Factions.INDEPENDENT);

        SectorEntityToken astfield1 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                        180f, // min radius
                        200f, // max radius
                        27, // min asteroid count
                        39, // max asteroid count
                        4f, // min asteroid radius
                        9f, // max asteroid radius
                        "Asteroids Field")); // null for default name
        astfield1.setCircularOrbit(tikusIII, 0, 0, 1);

        //extra
        system.autogenerateHyperspaceJumpPoints(true,false);

        //hyperspace nebulas
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);

        //markets
        //tikus 1 (not green this time)
        MarketAPI tikus_market = Global.getFactory().createMarket("rebelrats_tikus_market","",4);
        tikus.setMarket(tikus_market);
        tikus_market.setPrimaryEntity(tikus);

        tikus_market.setFactionId("rebelrats");
        tikus_market.setName("Tikus");
        tikus_market.getTariff().modifyFlat("generator", 0.3f);
        tikus.setInteractionImage("illustrations", "orbital");

        tikus_market.addCondition(Conditions.POPULATION_4);
        tikus_market.addCondition(Conditions.VOLATILES_PLENTIFUL);
        tikus_market.addCondition(Conditions.EXTREME_WEATHER);
        tikus_market.addCondition(Conditions.HOT);
        tikus_market.addCondition(Conditions.NO_ATMOSPHERE);

        tikus_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        tikus_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        tikus_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        tikus_market.addIndustry(Industries.POPULATION);
        tikus_market.addIndustry(Industries.SPACEPORT);
        tikus_market.addIndustry(Industries.MINING, Collections.singletonList(Items.PLASMA_DYNAMO));
        tikus_market.addIndustry(Industries.FUELPROD);
        tikus_market.addIndustry(Industries.GROUNDDEFENSES);
        tikus_market.addIndustry(Industries.PATROLHQ);
        tikus_market.addIndustry(Industries.ORBITALSTATION);
        tikus_market.addIndustry(Industries.WAYSTATION);

        tikus_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(tikus_market, false);
        //tikus II jungle!!
        MarketAPI tikusII_market = Global.getFactory().createMarket("rebelrats_tikusII_market","",5);
        tikusII.setMarket(tikusII_market);
        tikusII_market.setPrimaryEntity(tikusII);

        tikusII_market.setFactionId("rebelrats");
        tikusII_market.setName("Tikus II");
        tikusII_market.getTariff().modifyFlat("generator", 0.3f);
        tikusII.setInteractionImage("illustrations", "gilead");

        tikusII_market.addCondition(Conditions.POPULATION_5);
        tikusII_market.addCondition(Conditions.FARMLAND_RICH);
        tikusII_market.addCondition(Conditions.HABITABLE);
        tikusII_market.addCondition(Conditions.MILD_CLIMATE);
        tikusII_market.addCondition(Conditions.ORE_ULTRARICH);
        tikusII_market.addCondition(Conditions.RARE_ORE_MODERATE);

        tikusII_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        tikusII_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        tikusII_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        tikusII_market.addIndustry(Industries.POPULATION);
        tikusII_market.addIndustry(Industries.MEGAPORT);
        tikusII_market.addIndustry(Industries.MINING);
        tikusII_market.addIndustry(Industries.FARMING);
        tikusII_market.addIndustry(Industries.WAYSTATION);
        tikusII_market.addIndustry(Industries.GROUNDDEFENSES);
        tikusII_market.addIndustry(Industries.MILITARYBASE);
        tikusII_market.addIndustry(Industries.BATTLESTATION);

        tikusII_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        tikusII_market.getIndustry(Industries.MINING).setAICoreId(Commodities.BETA_CORE);
        globalEconomy.addMarket(tikusII_market, true);
        //tikus 3 COLD
        MarketAPI tikusIII_market = Global.getFactory().createMarket("rebelrats_tikusIII_market","",4);
        tikusIII.setMarket(tikusIII_market);
        tikusIII_market.setPrimaryEntity(tikusIII);

        tikusIII_market.setFactionId(Factions.INDEPENDENT);
        tikusIII_market.setName("Tikus III");
        tikusIII_market.getTariff().modifyFlat("generator", 0.3f);
        tikusIII.setInteractionImage("illustrations", "ilm");

        tikusIII_market.addCondition(Conditions.POPULATION_4);
        tikusIII_market.addCondition(Conditions.COLD);
        tikusIII_market.addCondition(Conditions.VOLATILES_DIFFUSE);
        tikusIII_market.addCondition(Conditions.FARMLAND_POOR);
        tikusIII_market.addCondition(Conditions.HABITABLE);

        tikusIII_market.addSubmarket(Submarkets.SUBMARKET_OPEN);
        tikusIII_market.addSubmarket(Submarkets.SUBMARKET_BLACK);
        tikusIII_market.addSubmarket(Submarkets.SUBMARKET_STORAGE);

        tikusIII_market.addIndustry(Industries.POPULATION);
        tikusIII_market.addIndustry(Industries.MINING);
        tikusIII_market.addIndustry(Industries.FARMING);
        tikusIII_market.addIndustry(Industries.SPACEPORT);
        tikusIII_market.addIndustry(Industries.GROUNDDEFENSES);
        tikusIII_market.addIndustry(Industries.PATROLHQ);

        tikusIII_market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        globalEconomy.addMarket(tikusIII_market, true);
    }
}
