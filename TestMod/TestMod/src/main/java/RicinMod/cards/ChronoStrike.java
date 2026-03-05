package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.characters.RicinCharacter;

/**
 * 时序打击：造成当前回合数*multiplier的伤害。回响：同样效果。
 * 使用 AbstractDungeon.actionManager.turn 获取回合数（初始为1）。
 */
public class ChronoStrike extends CustomCard {
    public static final String ID = "RicinMod:ChronoStrike";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/ChronoStrike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int MULTIPLIER = 2;
    private static final int MULTIPLIER_UP = 3;

    public ChronoStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        this.damage = 0;
        this.baseMagicNumber = MULTIPLIER;
        this.magicNumber = baseMagicNumber;
        this.tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(MULTIPLIER_UP - MULTIPLIER);
        }
    }

    private int getChronoDamage() {
        int turn = Math.max(1, GameActionManager.turn);
        return turn * magicNumber;
    }

    @Override
    public void applyPowers() {
        this.baseDamage = getChronoDamage();
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.baseDamage = getChronoDamage();
        super.calculateCardDamage(mo);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = getChronoDamage();
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    /** 回响效果：对随机敌人造成时序伤害 */
    public void triggerEchoEffect(AbstractPlayer p) {
        int dmg = getChronoDamage();
        AbstractMonster target = AbstractDungeon.getRandomMonster();
        if (target != null && !target.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction(
                            new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }
}
