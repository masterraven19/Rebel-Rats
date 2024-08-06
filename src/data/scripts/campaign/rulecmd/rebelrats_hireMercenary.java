package data.scripts.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class rebelrats_hireMercenary extends BaseCommandPlugin {
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String key = params.get(0).getString(memoryMap);

        FleetDataAPI playerFleet = Global.getSector().getPlayerFleet().getFleetData();
        TextPanelAPI text = dialog.getTextPanel();
        String[] ships;
        List<FleetMemberAPI> fleetMembers = new ArrayList<>();
        int crew;
        int marines;
        int supplies;
        int fuel;

        switch(key){
            case "rocimar":
                ships = new String[]{"rebelrats_sorex_Balanced", "rebelrats_mellivora_Outdated"};
                crew = 120;
                marines = 20;
                supplies = 60;
                fuel = 80;

                addShipsToFleet(ships,playerFleet,fleetMembers);
                playerFleet.getFleet().getCargo().addCrew(crew);
                playerFleet.getFleet().getCargo().addMarines(marines);
                playerFleet.getFleet().getCargo().addSupplies(supplies);
                playerFleet.getFleet().getCargo().addFuel(fuel);

                text.setFontSmallInsignia();
                for(FleetMemberAPI fleetMember : fleetMembers) {
                    text.addParagraph("Gained " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
                    text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
                }
                text.addPara("Gained " + crew + " crew",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+crew);
                text.addPara("Gained " + supplies + " supplies",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+supplies);
                text.addPara("Gained " + fuel + " fuel",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+fuel);
                text.addPara("Gained " + marines + " marines",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+marines);
                text.setFontInsignia();
                break;
            case "rufodorsalis":
                ships = new String[]{"rebelrats_mellivora_Outdated", "rebelrats_mellivora_Outdated", "rebelrats_rott_Balanced"};
                crew = 165;
                marines = 0;
                supplies = 80;
                fuel = 80;

                addShipsToFleet(ships,playerFleet,fleetMembers);
                playerFleet.getFleet().getCargo().addCrew(crew);
                playerFleet.getFleet().getCargo().addMarines(marines);
                playerFleet.getFleet().getCargo().addSupplies(supplies);
                playerFleet.getFleet().getCargo().addFuel(fuel);

                text.setFontSmallInsignia();
                for(FleetMemberAPI fleetMember : fleetMembers) {
                    text.addParagraph("Gained " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
                    text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
                }
                text.addPara("Gained " + crew + " crew",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+crew);
                text.addPara("Gained " + supplies + " supplies",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+supplies);
                text.addPara("Gained " + fuel + " fuel",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+fuel);
                text.setFontInsignia();
                break;
            case "mallomy":
                ships = new String[]{"rebelrats_pybara_Balanced", "rebelrats_patron_Balanced", "rebelrats_patron_Balanced"};
                crew = 45;
                marines = 0;
                supplies = 310;
                fuel = 120;

                addShipsToFleet(ships,playerFleet,fleetMembers);
                playerFleet.getFleet().getCargo().addCrew(crew);
                playerFleet.getFleet().getCargo().addMarines(marines);
                playerFleet.getFleet().getCargo().addSupplies(supplies);
                playerFleet.getFleet().getCargo().addFuel(fuel);
                playerFleet.getFleet().getCargo().addFighters("rebelrats_patron_F_wingt",1);

                text.setFontSmallInsignia();
                for(FleetMemberAPI fleetMember : fleetMembers) {
                    text.addParagraph("Gained " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
                    text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
                }
                text.addPara("Gained " + crew + " crew",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+crew);
                text.addPara("Gained " + supplies + " supplies",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+supplies);
                text.addPara("Gained " + fuel + " fuel",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+fuel);

                FleetMemberAPI fighter = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING,"rebelrats_patron_F_wingt");
                text.addPara("Gained " + fighter.getVariant().getFullDesignationWithHullNameForShip(),Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(), fighter.getVariant().getFullDesignationWithHullNameForShip());
                text.setFontInsignia();
                break;
            case "murinae":
                ships = new String[]{"rebelrats_jerboa_Balanced", "rebelrats_musrattus_Balanced", "rebelrats_musrattus_Balanced", "rebelrats_patron_Balanced"};
                crew = 150;
                marines = 30;
                supplies = 200;
                fuel = 130;

                addShipsToFleet(ships,playerFleet,fleetMembers);
                playerFleet.getFleet().getCargo().addCrew(crew);
                playerFleet.getFleet().getCargo().addMarines(marines);
                playerFleet.getFleet().getCargo().addSupplies(supplies);
                playerFleet.getFleet().getCargo().addFuel(fuel);

                text.setFontSmallInsignia();
                for(FleetMemberAPI fleetMember : fleetMembers) {
                    text.addParagraph("Gained " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
                    text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
                }
                text.addPara("Gained " + crew + " crew",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+crew);
                text.addPara("Gained " + supplies + " supplies",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+supplies);
                text.addPara("Gained " + fuel + " fuel",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+fuel);
                text.addPara("Gained " + marines + " marines",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+marines);
                text.setFontInsignia();
                break;
            default:
                ships = new String[]{"rebelrats_marmot_Balanced", "rebelrats_mellivora_Balanced", "rebelrats_rattus_Balanced",
                        "rebelrats_rattus_Balanced", "rebelrats_monax_Standard"};
                crew = 370;
                marines = 50;
                supplies = 280;
                fuel = 220;

                addShipsToFleet(ships,playerFleet,fleetMembers);
                playerFleet.getFleet().getCargo().addCrew(crew);
                playerFleet.getFleet().getCargo().addMarines(marines);
                playerFleet.getFleet().getCargo().addSupplies(supplies);
                playerFleet.getFleet().getCargo().addFuel(fuel);

                text.setFontSmallInsignia();
                for(FleetMemberAPI fleetMember : fleetMembers) {
                    text.addParagraph("Gained " + fleetMember.getVariant().getFullDesignationWithHullNameForShip(), Misc.getPositiveHighlightColor());
                    text.highlightInLastPara(Misc.getHighlightColor(), fleetMember.getVariant().getFullDesignationWithHullNameForShip());
                }
                text.addPara("Gained " + crew + " crew",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+crew);
                text.addPara("Gained " + supplies + " supplies",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+supplies);
                text.addPara("Gained " + fuel + " fuel",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+fuel);
                text.addPara("Gained " + marines + " marines",Misc.getPositiveHighlightColor());
                text.highlightInLastPara(Misc.getHighlightColor(),""+marines);
                text.setFontInsignia();
                break;
        }

        return true;
    }
    public static void addShipsToFleet(String[] variants, FleetDataAPI fleetdata, List<FleetMemberAPI> fleetMemberList){
        for(String variant : variants){
            FleetMemberAPI ship = Global.getFactory().createFleetMember(FleetMemberType.SHIP,variant);
            fleetMemberList.add(ship);
            fleetdata.addFleetMember(ship);
        }
    }
}
