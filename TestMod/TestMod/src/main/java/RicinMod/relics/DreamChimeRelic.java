package RicinMod.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import RicinMod.powers.EmberPower;

public class DreamChimeRelic extends CustomRelic {
    public static final String ID = "RicinMod:DreamChime";
    private static final String IMG_PATH = "RicinModResources/img/relics/DreamChime.png";
    private static final RelicTier TIER = RelicTier.STARTER;
    private static final LandingSound SOUND = LandingSound.FLAT;

    private static final int EMBER_AMOUNT = 1;
    private static final int METALLICIZE_AMOUNT = 3;

    public DreamChimeRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), TIER, SOUND);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        // 给玩家 1 层余温
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new EmberPower(AbstractDungeon.player, EMBER_AMOUNT), EMBER_AMOUNT));
        // 给玩家 3 层金属化
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new MetallicizePower(AbstractDungeon.player, METALLICIZE_AMOUNT), METALLICIZE_AMOUNT));
        // 给所有敌人 3 层金属化（source 为玩家，因为遗物由玩家持有）
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && !m.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(
                        m, AbstractDungeon.player,
                        new MetallicizePower(m, METALLICIZE_AMOUNT), METALLICIZE_AMOUNT));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DreamChimeRelic();
    }
}
