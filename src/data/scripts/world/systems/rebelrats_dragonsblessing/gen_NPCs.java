package data.scripts.world.systems.rebelrats_dragonsblessing;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;

public class gen_NPCs {//fix later, plan is to make class that returns a given person
    public static PersonAPI Administrator_Example(MarketAPI market){
        PersonAPI p = Global.getFactory().createPerson();
        FullName name = new FullName("Nazrin","Naides", FullName.Gender.FEMALE);
        p.setName(name);
        p.setFaction("rebelrats");
        p.setImportance(PersonImportance.VERY_HIGH);
        p.setRankId("Rat Commander");
        p.setMarket(market);
        p.getMarket().setAdmin(p);
        return p;
    }
    public static void generateNPCS(PlanetAPI planet, MarketAPI market){
        switch(planet.getName()){
            case "Krysa":
                Administrator_Example(market);
                break;
            default:
                break;
        }
    }
}
