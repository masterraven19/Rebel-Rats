
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.rebelratsgen;
import exerelin.campaign.SectorManager;
     
public class rebelratsplugin extends BaseModPlugin {  

   @Override
   public void onNewGame(){
      boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode())
		    new rebelratsgen().generate(Global.getSector());
 }
}
    
   
    

