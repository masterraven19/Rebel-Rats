package data.missions.ratfight;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
public class MissionDefinition implements MissionDefinitionPlugin {
	private String[] allied = {
			"rebelrats_porcellus_Balanced",
			"rebelrats_station_Orbital",
			"rebelrats_marmot_Balanced",
			"rebelrats_matkaboska_Lancer",
			"rebelrats_jerboa_Standard",
			"rebelrats_helmsman_Balanced",
			"rebelrats_firemodule_Balanced",
			"rebelrats_mellivora_Balanced",
			"rebelrats_monax_p_Balanced",
			"rebelrats_monax_Standard",
			"rebelrats_mus_Balanced",
			"rebelrats_musrattus_Balanced",
			"rebelrats_panya_Balanced",
			"rebelrats_patron_Balanced",
			"rebelrats_pybara_Balanced",
			"rebelrats_ratship_Angry",
			"rebelrats_rattus_Balanced",
			"rebelrats_rott_Balanced",
			"rebelrats_shieldmodule_Balanced",
			"rebelrats_sorex_Balanced",
			"rebelrats_steersman_Balanced"
	};
	private String[] enemies = {
			"rebelrats_porcellus_Balanced",
			"rebelrats_marmot_Balanced",
			"rebelrats_matkaboska_Lancer",
			"rebelrats_jerboa_Standard",
			"rebelrats_helmsman_Balanced",
			"rebelrats_mellivora_Balanced",
			"rebelrats_monax_p_Balanced",
			"rebelrats_monax_Standard",
			"rebelrats_mus_Balanced",
			"rebelrats_musrattus_Balanced",
			"rebelrats_panya_Balanced",
			"rebelrats_patron_Balanced",
			"rebelrats_pybara_Balanced",
			"rebelrats_rattus_Balanced",
			"rebelrats_rott_Balanced",
			"rebelrats_sorex_Balanced",
			"rebelrats_steersman_Balanced"
	};

	private void generateFleet(FleetSide side, String[] ships, MissionDefinitionAPI api) {
		for(int i = 0;i < ships.length;i++){
			if(ships[i].contains("porcellus")){
				api.addToFleet(side,ships[i],FleetMemberType.SHIP,true);
			}else{
				api.addToFleet(side,ships[i],FleetMemberType.SHIP,false);
			}
		}
	}

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "ISS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "ISS", FleetGoal.ATTACK, true, 5);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Your rats");
		api.setFleetTagline(FleetSide.ENEMY, "Enemy rats");

		// These show up as items in the bulleted list under
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Defeat all enemy forces or surrender your cheese");

		// Set up the fleets
		generateFleet(FleetSide.PLAYER,allied,api);
		generateFleet(FleetSide.ENEMY,enemies,api);

		// Set up the map.
		float width = 24000f;
		float height = 18000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);

		float minX = -width/2;
		float minY = -height/2;


		for (int i = 0; i < 50; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 400f;
			api.addNebula(x, y, radius);
		}

		// Add objectives
		api.addObjective(minX + width * 0.25f + 2000, minY + height * 0.25f + 2000, "nav_buoy");
		api.addObjective(minX + width * 0.75f - 2000, minY + height * 0.25f + 2000, "comm_relay");
		api.addObjective(minX + width * 0.75f - 2000, minY + height * 0.75f - 2000, "nav_buoy");
		api.addObjective(minX + width * 0.25f + 2000, minY + height * 0.75f - 2000, "comm_relay");

		String [] planets = {"rebelrats_fart_giant", "desert"};
		String planet = planets[(int) (Math.random() * (double) planets.length)];
		float radius = 100f + (float) Math.random() * 150f;
		api.addPlanet(0, 0, radius, planet, 200f, true);
	}

}





