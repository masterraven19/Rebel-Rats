package data.scripts.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc.Token;

import java.util.List;
import java.util.Map;

public class rebelrats_powerstationDialog extends BaseCommandPlugin {
    public rebelrats_powerstationDialog(){

    }
    String powerstationid = "rebelrats_powerstation";
    SectorEntityToken powerstation1 = Global.getSector().getEntityById(powerstationid);

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
        SectorEntityToken target = dialog.getInteractionTarget();
        if (target == powerstation1){
            if(target.getCustomInteractionDialogImageVisual() != null){
                dialog.getVisualPanel().showImageVisual(target.getCustomInteractionDialogImageVisual());
            }
        }
        return true;
    }
}
