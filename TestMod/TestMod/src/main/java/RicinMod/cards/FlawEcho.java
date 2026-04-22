package RicinMod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import RicinMod.DebuffCountHelper;
import RicinMod.characters.RicinCharacter;

public class FlawEcho extends CustomCard {
    public static final String ID = "RicinMod:FlawEcho";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RicinModResources/img/cards/FlawEcho.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE = 7;
    private static final int DAMAGE_UP = 3;
    private static final int ECHO_DMG = 3;
    private static final int ECHO_DMG_UP = 1;

    public FlawEcho() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = ECHO_DMG;
        this.tags.add(AbstractCard.CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(DAMAGE_UP);
            this.upgradeMagicNumber(ECHO_DMG_UP);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1 + DebuffCountHelper.countPlayerDebuffs(p);
        for (int i = 0; i < hits; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    /** 回响：随机目标，每段为 magicNumber 伤害，段数 1+负面数 */
    public void triggerEchoEffect(AbstractPlayer p) {
        int hits = 1 + DebuffCountHelper.countPlayerDebuffs(p);
        for (int i = 0; i < hits; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageRandomEnemyAction(
                            new DamageInfo(p, this.magicNumber, DamageInfo.DamageType.NORMAL),
                            AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }
}
