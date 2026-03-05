package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.characters.RicinCharacter;

public class ConstellationDefend extends CustomCard {
    public static final String ID = "RicinMod:ConstellationDefend";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/ConstellationDefend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BLOCK = 5;
    private static final int BLOCK_UP = 3;
    private static final int ECHO_BLOCK = 2;
    private static final int ECHO_BLOCK_UP = 1;

    public ConstellationDefend() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = ECHO_BLOCK;
        this.tags.add(AbstractCard.CardTags.STARTER_DEFEND);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(BLOCK_UP);
            this.upgradeMagicNumber(ECHO_BLOCK_UP);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    /** 回响效果：获得格挡 */
    public void triggerEchoEffect(AbstractPlayer p) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, magicNumber));
    }
}
