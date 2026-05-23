
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import data.scripts.campaign.econ.rebelrats_EconListener;
import data.scripts.campaign.ids.rebelrats_Markets;
import data.scripts.world.rebelratsgen;
import data.scripts.world.rebelrats_gen_NPCs;
import exerelin.campaign.SectorManager;

public class rebelratsplugin extends BaseModPlugin {

    private boolean haveNexerelin(){
        return Global.getSettings().getModManager().isModEnabled("nexerelin");
    }
    @Override
    public void onNewGame(){

        if (!haveNexerelin() || SectorManager.getManager().isCorvusMode())
		    new rebelratsgen().generate(Global.getSector());
    }
    public void onGameLoad(boolean newGame){

        Global.getSector().getEconomy().addUpdateListener(new rebelrats_EconListener());
//        boolean haveNexerelin = haveNexerelin();
//        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()) {
//            if (Global.getSector().getStarSystem("Dragon's Blessing") != null) {
//                new rebelratsgen().generate(Global.getSector());
//            }
//        }
//        PersonAPI pNaz = Global.getSector().getImportantPeople().getPerson(nazarin);
//        PersonAPI pThackery = Global.getSector().getImportantPeople().getPerson(thackery);
//        if(pNaz == null){
//            MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(krysa);
//            new rebelrats_gen_NPCs().generate_FRO(krysaMarket);
//        }
//        if(pThackery == null){
//            MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(krysa);
//            new rebelrats_gen_NPCs().generate_Thackery(krysaMarket);
//        }

    }
//    public void onNewGameAfterEconomyLoad() {
//
//    }
    public void onNewGameAfterTimePass() {
        if(!haveNexerelin() || SectorManager.getManager().isCorvusMode()){
            MarketAPI krysaMarket = Global.getSector().getEconomy().getMarket(rebelrats_Markets.krysa);

            if (krysaMarket != null){
                new rebelrats_gen_NPCs().generate_FRO(krysaMarket);
                new rebelrats_gen_NPCs().generate_Thackery(krysaMarket);
            }
        }
    }
}
    
   
    

