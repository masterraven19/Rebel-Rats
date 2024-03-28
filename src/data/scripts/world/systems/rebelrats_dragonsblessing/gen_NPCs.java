package data.scripts.world.systems.rebelrats_dragonsblessing;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;

public class gen_NPCs {
    public static void Administrator_Example(MarketAPI market){//doesnt appear
        PersonAPI p = Global.getFactory().createPerson();
        FullName name = new FullName("Nazarin","Dess", FullName.Gender.FEMALE);
        p.setName(name);
        p.setId("rebelrats_nazarin");
        p.setFaction("rebelrats");
        p.setImportance(PersonImportance.VERY_HIGH);
        p.setRankId("Rat Commander");
        market.getCommDirectory().addPerson(p,1);
        market.setAdmin(p);
        p.setMarket(market);
    }
    public static void generateNPCS(PlanetAPI planet, MarketAPI market){
        Administrator_Example(market);
    }
}
