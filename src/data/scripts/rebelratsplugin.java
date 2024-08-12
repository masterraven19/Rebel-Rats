
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import data.scripts.world.rebelratsgen;
import data.scripts.world.rebelrats_gen_NPCs;
import exerelin.campaign.SectorManager;

import java.util.List;

public class rebelratsplugin extends BaseModPlugin {
    private final String krysa = "rattus_market";
    private final String magawa = "magawa_market";
    private final String rodentia = "rodentia_market";
    private final String nazarin = "rebelrats_nazarin";
    private final String thackery = "rebelrats_thackery";
    @Override
    public void onNewGame(){

        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode())
		    new rebelratsgen().generate(Global.getSector());
    }
    public void onGameLoad(boolean newGame){

        boolean haverats = Global.getSettings().getModManager().isModEnabled("rebelrats");
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");

        if (!haveNexerelin || haverats && SectorManager.getManager().isCorvusMode()){
            if (Global.getSector().getStarSystem("Dragon's Blessing") != null) {
                StarSystemAPI dragon = Global.getSector().getStarSystem("Dragon's Blessing");
                List<CustomCampaignEntityAPI> customentities = dragon.getCustomEntities();

                for (CustomCampaignEntityAPI c : customentities) {
                    if (c.getId().equals("rebelrats_powerstation")) {
                        c.setInteractionImage("illustrations", "rebelrats_powerstation");
                    }
                }
            }
            else{
                new rebelratsgen().generate(Global.getSector());
            }
        }

        PersonAPI pNaz = Global.getSector().getImportantPeople().getPerson(nazarin);
        PersonAPI pThackery = Global.getSector().getImportantPeople().getPerson(thackery);
        if(pNaz == null){
            MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(krysa);
            new rebelrats_gen_NPCs().generate_FRO(krysaMarket);
        }
        if(pThackery == null){
            MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(krysa);
            new rebelrats_gen_NPCs().generate_Thackery(krysaMarket);
        }
    }
    public void onNewGameAfterEconomyLoad() {
        MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(krysa);
        new rebelrats_gen_NPCs().generate_FRO(krysaMarket);
        new rebelrats_gen_NPCs().generate_Thackery(krysaMarket);
    }
}
    
   
    

