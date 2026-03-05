package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.actions.PlaceCardsInHandIntoEchoAction;
import RicinMod.characters.RicinCharacter;

public class Memento extends CustomCard {
    public static final String ID = "RicinMod:Memento";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/Memento.png";
    private static final int COST = 1;
    private static final int COST_UPGRADED = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int ETERNALIZE_AMOUNT = 1;
    private static final int DRAW_AMOUNT = 1;

    public Memento() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = ETERNALIZE_AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(COST_UPGRADED);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 铭刻 1 张手牌（仿 Downfall 蜷身升级效果），抽 1 张牌
        AbstractDungeon.actionManager.addToBottom(new PlaceCardsInHandIntoEchoAction(p, 1, false));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW_AMOUNT));
    }
}
