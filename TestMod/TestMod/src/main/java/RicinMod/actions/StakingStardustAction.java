package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import RicinMod.cards.StakingStardust;

/**
 * 抽牌直到手牌抽满；并在牌堆中为该卡增加 1 费。
 * 参照精巧刺击 Masterful Stab：使用 updateCost(1)，时机改为「每次打出后」。
 */
public class StakingStardustAction extends AbstractGameAction {
    private static final int MAX_HAND_SIZE = 10;
    private final String cardUUID;

    public StakingStardustAction(AbstractCard card) {
        this.cardUUID = card.uuid.toString();
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            isDone = true;
            return;
        }
        int toDraw = Math.max(0, MAX_HAND_SIZE - p.hand.size());
        if (toDraw > 0) {
            addToBot(new DrawCardAction(p, toDraw));
        }
        // 等待卡牌进入弃牌堆后再调用 updateCost(1)（参照精巧刺击）
        addToBot(new WaitAction(0.25f));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : p.hand.group) {
                    if (c.uuid.toString().equals(cardUUID)) {
                        StakingStardust.increaseCostForCard(c);
                        isDone = true;
                        return;
                    }
                }
                for (AbstractCard c : p.discardPile.group) {
                    if (c.uuid.toString().equals(cardUUID)) {
                        StakingStardust.increaseCostForCard(c);
                        isDone = true;
                        return;
                    }
                }
                for (AbstractCard c : p.drawPile.group) {
                    if (c.uuid.toString().equals(cardUUID)) {
                        StakingStardust.increaseCostForCard(c);
                        isDone = true;
                        return;
                    }
                }
                // 若卡牌被消耗等特殊情况
                for (AbstractCard c : p.exhaustPile.group) {
                    if (c.uuid.toString().equals(cardUUID)) {
                        StakingStardust.increaseCostForCard(c);
                        break;
                    }
                }
                isDone = true;
            }
        });
        isDone = true;
    }
}
