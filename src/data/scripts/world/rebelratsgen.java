
package data.scripts.world;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.rebelrats_dragonsblessing;
import com.fs.starfarer.api.campaign.FactionAPI;

public class rebelratsgen {
    public void generate(SectorAPI sector) {
        new rebelrats_dragonsblessing().generate(sector);
        FactionAPI rebelrats = sector.getFaction("rebelrats");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("rebelrats");
        //rebelrats.setRelationship(Factions.INDEPENDENT, 1.0f);
        //rebelrats.setRelationship(Factions.PIRATES, -1.0f);
        //rebelrats.setRelationship(Factions.NEUTRAL, 0.3f);
        //rebelrats.setRelationship(Factions.HEGEMONY, -0.2f);
        //rebelrats.setRelationship(Factions.DIKTAT, 0.5f);
    }
}
