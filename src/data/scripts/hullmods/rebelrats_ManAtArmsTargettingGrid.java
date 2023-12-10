package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class rebelrats_ManAtArmsTargettingGrid extends BaseHullMod {
	public static float BONUS_SMALL_1 = 10;
	public static float BONUS_MEDIUM_1 = 10;
	public static float BONUS_LARGE_1 = 5;
	public static float BONUS_PD = 50;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		WeaponSize largest = null;
		for (WeaponSlotAPI slot : ship.getHullSpec().getAllWeaponSlotsCopy()) {
			if (slot.isDecorative() ) continue;
			if (slot.getWeaponType() == WeaponType.BALLISTIC) {
				if (largest == null || largest.ordinal() < slot.getSlotSize().ordinal()) {
					largest = slot.getSlotSize();
				}
			}
		}
		if (largest == null) return;
		float smallb = 0f;
		float mediumb = 0f;
		float largeb = 0f;
		float pdb = BONUS_PD; //check the weaponmount size and apply the bonuses to the variable
		switch (largest) {
			case LARGE:
				largeb = BONUS_LARGE_1;
				break;
			case MEDIUM:
				mediumb = BONUS_MEDIUM_1;
				break;
			case SMALL:
				smallb = BONUS_SMALL_1;
				break;
		}

		ship.addListener(new RangeModifier(smallb, mediumb, largeb, pdb));
	}

	public static class RangeModifier implements WeaponBaseRangeModifier {
		public float smallb, mediumb, largeb, pdb;
		//assign variables to baserangemodifier
		public RangeModifier(float smallb, float mediumb, float largeb, float pdb) {
			this.smallb = smallb;
			this.mediumb = mediumb;
			this.largeb = largeb;
			this.pdb = pdb;
		}

		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			if (weapon.getSlot() == null || weapon.getSlot().getWeaponType() != WeaponType.BALLISTIC) {
				return 0f;
			}
			float bonus = 0;

			switch (weapon.getSize()) {
				case SMALL:
					if (weapon.hasAIHint(AIHints.PD))
						bonus = pdb;
					else
						bonus = smallb;
					break;
				case MEDIUM:
					bonus = mediumb;
					break;
				case LARGE:
					bonus = largeb;
					break;
			}
			return bonus;
		}

		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}

		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			return 0f;
		}

	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) BONUS_SMALL_1 + "%";
		if (index == 1) return "" + (int) BONUS_MEDIUM_1 + "%";
		if (index == 2) return "" + (int) BONUS_LARGE_1 + "%";
		if (index == 3) return "" + (int) BONUS_PD + "%";
		return null;
	}
	public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
		float pad = 10f;
		float cW = 100f;
		Color h = Misc.getHighlightColor();
		tooltip.addSectionHeading("Stat Effects", Alignment.MID,pad);

		tooltip.beginTable(Color.GRAY,Color.DARK_GRAY,Color.WHITE,10f,true,true,
				new Object[] {"LARGE",cW,"MEDIUM",cW,"SMALL",cW});
		tooltip.addRow(Alignment.MID,h,Misc.getRoundedValue(BONUS_LARGE_1) + "%",
				Alignment.MID,h,Misc.getRoundedValue(BONUS_MEDIUM_1) + "%",
				Alignment.MID,h,Misc.getRoundedValue(BONUS_SMALL_1) + "%");
		tooltip.addTable("",0,pad);

		tooltip.beginTable(Color.GRAY,Color.DARK_GRAY,Color.WHITE,10f,true,true,
				new Object[]{"SMALL PD",cW});
		tooltip.addRow(Alignment.MID,h,Misc.getRoundedValue(BONUS_PD) + "%");
		tooltip.addTable("",0,pad);

		//tooltip.addPara("%s",2F,Color.gray,new String[]{ "The rats are armed" }).italicize();
	}
	public Color getBorderColor() {
		return Global.getSettings().getDesignTypeColor("Arms & Armor");
	}
	public Color getNameColor() {
		return Global.getSettings().getDesignTypeColor("Arms & Armor");
	}
}
