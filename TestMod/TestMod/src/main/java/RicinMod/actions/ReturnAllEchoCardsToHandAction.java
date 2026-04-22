package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import RicinMod.orbs.EchoOrb;
import RicinMod.orbs.EchoSlotOrb;

/**
 * 将回响栏（EchoOrb / EchoSlotOrb）中所有已铭刻的卡牌取回手牌；手牌满 10 张时溢至弃牌堆。
 */
public class ReturnAllEchoCardsToHandAction extends AbstractGameAction {
    private static final int MAX_HAND = 10;

    public ReturnAllEchoCardsToHandAction() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) {
            isDone = true;
            return;
        }
        for (int i = 0; i < p.orbs.size(); i++) {
            AbstractOrb orb = p.orbs.get(i);
            AbstractCard card = null;
            if (orb instanceof EchoOrb) {
                EchoOrb eo = (EchoOrb) orb;
                if (eo.storedCard != null) {
                    card = eo.storedCard;
                    eo.storedCard = null;
                    EchoSlotOrb empty = new EchoSlotOrb();
                    copyOrbLayout(orb, empty);
                    p.orbs.set(i, empty);
                }
            } else if (orb instanceof EchoSlotOrb) {
                EchoSlotOrb es = (EchoSlotOrb) orb;
                if (es.storedCard != null) {
                    card = es.storedCard;
                    es.clearCard();
                }
            }
            if (card != null) {
                card.stopGlowing();
                card.unhover();
                if (p.hand.size() < MAX_HAND) {
                    p.hand.addToHand(card);
                } else {
                    p.discardPile.addToTop(card);
                }
            }
        }
        p.hand.refreshHandLayout();
        for (int j = 0; j < p.orbs.size(); j++) {
            p.orbs.get(j).setSlot(j, p.maxOrbs);
        }
        isDone = true;
    }

    private static void copyOrbLayout(AbstractOrb from, AbstractOrb to) {
        to.cX = from.cX;
        to.cY = from.cY;
        to.tX = from.tX;
        to.tY = from.tY;
        to.hb.x = from.hb.x;
        to.hb.y = from.hb.y;
        to.hb.cX = from.hb.cX;
        to.hb.cY = from.hb.cY;
        to.hb.width = from.hb.width;
        to.hb.height = from.hb.height;
    }
}
