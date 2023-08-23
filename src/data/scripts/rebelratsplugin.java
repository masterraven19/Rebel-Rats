
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
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
    }
}
    
   
    

