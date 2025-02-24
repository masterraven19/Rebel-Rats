package data.scripts.campaign.skills;

import com.fs.starfarer.api.characters.DescriptionSkillEffect;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;

public class rebelrats_Aptitude {
    public static class Level0 implements DescriptionSkillEffect{

        @Override
        public Color getTextColor() {
            return Misc.getTextColor();
        }

        @Override
        public String getString() {
            return "How are you reading this?!?!? get OUT!!";
        }

        @Override
        public String[] getHighlights() {
            return new String[]{"OUT"};
        }

        @Override
        public Color[] getHighlightColors() {
            Color h = Misc.getHighlightColor();
            return new Color[]{h};
        }
    }
}
