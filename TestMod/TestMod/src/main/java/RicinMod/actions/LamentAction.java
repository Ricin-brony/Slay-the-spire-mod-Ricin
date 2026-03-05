package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import RicinMod.EchoBarHelper;

/**
 * 悲歌：触发回响栏中所有卡牌的回响效果。
 */
public class LamentAction extends AbstractGameAction {
    private final int times;

    public LamentAction(int times) {
        this.times = times;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        for (int i = 0; i < times; i++) {
            EchoBarHelper.triggerAllEchoEffects(AbstractDungeon.player);
        }
        this.isDone = true;
    }
}
