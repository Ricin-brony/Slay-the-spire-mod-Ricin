package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import RicinMod.EchoBarHelper;
import RicinMod.powers.EmberWakePower;

/**
 * 引渡：消耗回响栏最右侧的卡牌，按余温层数触发回响效果。
 * 若有余波，每层在引渡时回1能量。
 */
public class CharonAction extends AbstractGameAction {

    public CharonAction() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        boolean consumed = EchoBarHelper.charonRightmost(AbstractDungeon.player);
        if (consumed) {
            if (AbstractDungeon.player.hasPower(EmberWakePower.POWER_ID)) {
                int amount = AbstractDungeon.player.getPower(EmberWakePower.POWER_ID).amount;
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
            }
        }
        this.isDone = true;
    }
}
