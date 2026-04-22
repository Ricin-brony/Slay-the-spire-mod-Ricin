package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 不丢弃、不移动现有手牌；仅向手牌补入 *晕眩*，使手牌张数达到上限（10）。
 * 例：剩余 5 张手牌则添加 5 张 Dazed。
 */
public class FillHandWithDazedAction extends AbstractGameAction {
    private static final int MAX_HAND = 10;

    public FillHandWithDazedAction(AbstractPlayer p) {
        this.source = this.target = p;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) {
            isDone = true;
            return;
        }
        int need = Math.max(0, MAX_HAND - p.hand.size());
        for (int i = 0; i < need; i++) {
            AbstractCard d = new Dazed();
            p.hand.addToTop(d);
        }
        p.hand.refreshHandLayout();
        isDone = true;
    }
}
