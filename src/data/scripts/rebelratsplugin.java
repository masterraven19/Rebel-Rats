
package data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.AutofireAIPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.campaign.econ.rebelrats_EconListener;
import data.scripts.campaign.ids.rebelrats_Markets;
import data.scripts.combat.ids.rebelrats_Weapons;
import data.scripts.combat.ai.rebelrats_amosAutofireAI;
import data.scripts.combat.rebelrats_combatUtils;
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

    @Override
    public PluginPick<AutofireAIPlugin> pickWeaponAutofireAI(WeaponAPI weapon) {
        String id = weapon.getId();
        switch (id){
            case rebelrats_Weapons.amosSmall:
                rebelrats_combatUtils.logMessage(this.getClass(),"Picking custom AutofireAI for amos");
                return new PluginPick<>(
                        (AutofireAIPlugin) new rebelrats_amosAutofireAI(weapon),
                        CampaignPlugin.PickPriority.MOD_SPECIFIC
                );
            default:
                return super.pickWeaponAutofireAI(weapon);
        }
    }
}
    
   
    

