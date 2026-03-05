package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.characters.RicinCharacter;

public class Whisper extends CustomCard {
    public static final String ID = "RicinMod:Whisper";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/Whisper.png";
    private static final int COST = 1;
    private static final int COST_UPGRADED = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int LAMENT_AMOUNT = 1;

    public Whisper() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = LAMENT_AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 悲歌 1 - 触发回响栏中所有卡牌的回响效果
        AbstractDungeon.actionManager.addToBottom(new RicinMod.actions.LamentAction(LAMENT_AMOUNT));
        // 引渡回响栏最右侧的卡牌 - 消耗最右侧卡，按余温层数触发回响，获得1费
        AbstractDungeon.actionManager.addToBottom(new RicinMod.actions.CharonAction());
    }
}
