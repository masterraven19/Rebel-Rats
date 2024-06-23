package data.scripts.world.systems.rebelrats_dragonsblessing;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.People;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Tags;

public class gen_NPCs {
    public static void Administrator_Example(MarketAPI market){//finally works,
        PersonAPI p = Global.getFactory().createPerson();
        FullName name = new FullName("Nazarin","Dess", FullName.Gender.FEMALE);
        p.setName(name);
        p.setPortraitSprite(Global.getSettings().getSpriteName("characters", "rebelrats_nazarin"));
        p.setId("rebelrats_nazarin");
        p.setFaction("rebelrats");
        p.setImportance(PersonImportance.VERY_HIGH);
        String post = "rebelrats_FRO";
        p.setRankId(Ranks.SPACE_ADMIRAL);
        p.setPostId(post);
        p.addTag(Tags.CONTACT_TRADE);

        market.getCommDirectory().addPerson(p,0);
        market.addPerson(p);

        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        ip.addPerson(p);
        People.assignPost(market,post,p);

    }
    public static void generateNPCS(PlanetAPI planet, MarketAPI market){
        Administrator_Example(market);
    }
}
