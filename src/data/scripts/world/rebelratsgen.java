
package data.scripts.world;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.rebelrats_dragonsblessing.dragons_gen;
import com.fs.starfarer.api.campaign.FactionAPI;
import data.scripts.world.systems.rebelrats_tikus;

public class rebelratsgen {
    public void generate(SectorAPI sector) {

        new dragons_gen().generate(sector);
        new rebelrats_tikus().generate(sector);

        FactionAPI rebelrats = sector.getFaction("rebelrats");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("rebelrats");
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");

        if (!haveNexerelin){
            rebelrats.setRelationship(Factions.INDEPENDENT, 1.0f);
            rebelrats.setRelationship(Factions.PIRATES, -1.0f);
            rebelrats.setRelationship(Factions.PLAYER, 0.3f);
            rebelrats.setRelationship(Factions.HEGEMONY, -0.2f);
            rebelrats.setRelationship(Factions.DIKTAT, 0.5f);
            rebelrats.setRelationship(Factions.PERSEAN, 0.6F);
        }
    }
}
