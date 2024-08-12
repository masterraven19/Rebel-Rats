package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.Tags;

public class rebelrats_gen_NPCs {
    public void generate_FRO(MarketAPI market){
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
    }
    public void generate_Thackery(MarketAPI market){
        PersonAPI p = Global.getFactory().createPerson();
        FullName name = new FullName("Himichi","Thackery", FullName.Gender.FEMALE);
        p.setName(name);
        p.setPortraitSprite(Global.getSettings().getSpriteName("characters", "rebelrats_nazarin"));
        p.setId("rebelrats_thackery");
        p.setFaction("rebelrats");
        String post = "rebelrats_Officer";
        p.setRankId(Ranks.SPACE_CAPTAIN);
        p.setPostId(post);
        p.getStats().setLevel(5);
        p.getStats().setSkillLevel(Skills.SYSTEMS_EXPERTISE,2F);
        p.getStats().setSkillLevel(Skills.HELMSMANSHIP,2F);

        ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
        ip.addPerson(p);
        market.getCommDirectory().addPerson(p);
        market.addPerson(p);
    }
}
