package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import RicinMod.RicinMod;
import RicinMod.orbs.EchoOrb;

/**
 * 仿 PlaceActualCardIntoStasis：将指定卡牌放入铭刻槽（通过 ChannelAction 添加 EchoOrb）。
 */
public class PlaceActualCardIntoEcho extends AbstractGameAction {
    private final AbstractCard card;
    private final CardGroup source;
    private final boolean skipWait;
    private final boolean hadRetain;

    public PlaceActualCardIntoEcho(AbstractCard card) {
        this(card, null);
    }

    public PlaceActualCardIntoEcho(AbstractCard card, CardGroup source) {
        this(card, source, false);
    }

    public PlaceActualCardIntoEcho(AbstractCard card, CardGroup source, boolean skipWait) {
        this.card = card;
        this.source = source;
        this.skipWait = skipWait;
        this.hadRetain = card.retain;
        this.actionType = ActionType.CARD_MANIPULATION;
        if (source != null && source.type == CardGroup.CardGroupType.HAND) {
            card.retain = true;
        }
    }

    @Override
    public void update() {
        if (RicinMod.canSpawnEchoOrb()) {
            if (!skipWait && !Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
            addToTop(new ChannelAction(new EchoOrb(card, source)));
        } else {
            card.retain = hadRetain;
        }
        isDone = true;
    }
}
