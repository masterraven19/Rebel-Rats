package data.scripts.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.ImportantPeopleAPI.PersonDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;

public class rebelrats_hireOfficer extends BaseCommandPlugin {
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {

        PersonAPI person;
        String key = params.get(0).getString(memoryMap);
        PersonDataAPI data = Global.getSector().getImportantPeople().getData(key);
        person = data.getPerson();

        if (person == null)return false;

        FleetDataAPI playerFleet = Global.getSector().getPlayerFleet().getFleetData();
        playerFleet.addOfficer(person);
        AddRemoveCommodity.addOfficerGainText(person,dialog.getTextPanel());

        if(key.matches("rebelrats_thackery")){
            FleetMemberAPI fleetMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"rebelrats_musrattus_Balanced");
            TextPanelAPI text = dialog.getTextPanel();
            fleetMember.setShipName("ALMAYER");
            fleetMember.setCaptain(person);

            Global.getSector().getPlayerFleet().getCargo().addCrew((int)fleetMember.getMinCrew());
            playerFleet.addFleetMember(fleetMember);
            text.setFontSmallInsignia();
            text.addParagraph("Gained her personal ship, a " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
            text.addParagraph("Gained " + (int)fleetMember.getMinCrew() + " crew", Misc.getPositiveHighlightColor());
            text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
            text.highlightInLastPara(Misc.getHighlightColor(), (int)fleetMember.getMinCrew() + " crew");
            text.setFontInsignia();
        }
        return true;
    }
}
