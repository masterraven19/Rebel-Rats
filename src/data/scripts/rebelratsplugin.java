
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import data.scripts.world.rebelratsgen;
import exerelin.campaign.SectorManager;

import java.util.List;

public class rebelratsplugin extends BaseModPlugin {
    private final String krysa = "rattus_market";
    private final String magawa = "magawa_market";
    private final String rodentia = "rodentia_market";
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

           StarSystemAPI dragon = Global.getSector().getStarSystem("Dragon's Blessing");
           List <CustomCampaignEntityAPI> customentities = dragon.getCustomEntities();
           List <PlanetAPI> planets = dragon.getPlanets();

           for (PlanetAPI p : planets){
               if (p.getMarket() != null){
                   if (p.getMarket().getFactionId().equals("rebelrats")){
                       MarketAPI m = p.getMarket();
                        switch(p.getMarket().getId()){
                            case krysa:
                                if (m.hasCondition(Conditions.FARMLAND_ADEQUATE)){m.removeCondition(Conditions.FARMLAND_ADEQUATE);}
                                if (!m.hasCondition(Conditions.FARMLAND_BOUNTIFUL)){m.addCondition(Conditions.FARMLAND_BOUNTIFUL);}
                                if (!m.hasIndustry(Industries.LIGHTINDUSTRY)){m.addIndustry(Industries.LIGHTINDUSTRY);}
                                break;
                            case magawa:
                                if (m.getSize() == 4){m.setSize(5);}
                                if (m.hasCondition(Conditions.POPULATION_4)){m.removeCondition(Conditions.POPULATION_4);}
                                if (!m.hasCondition(Conditions.POPULATION_5)){m.addCondition(Conditions.POPULATION_5);}
                                if (m.hasCondition(Conditions.ORE_MODERATE)){m.removeCondition(Conditions.ORE_MODERATE);}
                                if (!m.hasCondition(Conditions.ORE_RICH)){m.addCondition(Conditions.ORE_RICH);}
                                if (m.hasIndustry(Industries.TECHMINING)){m.removeIndustry(Industries.TECHMINING, MarketAPI.MarketInteractionMode.LOCAL,false);}
                                if (!m.hasIndustry(Industries.REFINING)){m.addIndustry(Industries.REFINING);}
                                if (!m.hasIndustry(Industries.MILITARYBASE)){m.addIndustry(Industries.MILITARYBASE);}
                                break;
                        }
                   }
               }
           }

           for (CustomCampaignEntityAPI c : customentities){
               if (c.getId().equals("rebelrats_powerstation")){
                   c.setInteractionImage("illustrations","rebelrats_powerstation");
               }
           }
       }
    }
}
    
   
    

