package data.scripts.campaign.rulecmd;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;

public class rebelrats_doIsTrueCheck extends BaseCommandPlugin {
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        boolean memFlag = params.get(0).getBoolean(memoryMap);
        final String option = params.get(1).getString(memoryMap);

        OptionPanelAPI options = dialog.getOptionPanel();

        if(memFlag){
            options.setEnabled(option,false);
            options.addOptionTooltipAppender(option,new OptionPanelAPI.OptionTooltipCreator() {
                public void createTooltip(TooltipMakerAPI tooltip, boolean hadOtherText) {
                    if(option.equals("rebelrats_nazar_storyPointEscort")){
                        tooltip.addPara("I don't think this will work again.",10);
                    }else{
                        tooltip.addPara("You have already hired these mercenaries.",10);
                    }
                }
            });
        }

        return true;
    }
}
