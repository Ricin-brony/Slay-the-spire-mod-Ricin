package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.actions.StakingStardustAction;
import RicinMod.characters.RicinCharacter;

public class StakingStardust extends CustomCard {
    public static final String ID = "RicinMod:StakingStardust";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/StakingStardust.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public StakingStardust() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // 确保 0 费正确显示（参照 Rampage 等自修改卡牌）
        this.cost = this.costForTurn = COST;
    }

    /** 参照精巧刺击 Masterful Stab：使用 AbstractCard.updateCost(1) 增加费用。
     *  精巧刺击在 tookDamage() 时调用，孤注一掷在每次打出后调用。 */
    public static void increaseCostForCard(AbstractCard c) {
        if (c instanceof StakingStardust && c.cost >= 0) {
            ((StakingStardust) c).updateCost(1);
            c.isCostModified = true;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new StakingStardustAction(this));
    }
}
