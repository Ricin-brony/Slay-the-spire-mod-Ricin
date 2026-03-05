package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 铭刻：将手牌中的卡牌放入铭刻槽。
 * 委托给 PlaceCardsInHandIntoEchoAction 实现（与 Downfall 凝滞机制一致）。
 */
public class EternalizeAction extends AbstractGameAction {

    public EternalizeAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new PlaceCardsInHandIntoEchoAction(AbstractDungeon.player, amount, false));
        this.isDone = true;
    }
}
