package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import RicinMod.EchoBarHelper;
import RicinMod.powers.EmberPower;
import RicinMod.powers.EmberWakePower;
import RicinMod.powers.EmberferryPower;

/**
 * 引渡：消耗回响栏最右侧的卡牌，按余温层数触发回响效果。
 * 若有余波：每次成功引渡获得能量 = 余波层数（每层 1 点）。
 * 若有余烬引渡：每次成功引渡获得余温 = 余烬引渡层数（每层 1 层余温）。
 */
public class CharonAction extends AbstractGameAction {

    public CharonAction() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        boolean consumed = EchoBarHelper.charonRightmost(AbstractDungeon.player);
        if (consumed) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasPower(EmberWakePower.POWER_ID)) {
                int amount = p.getPower(EmberWakePower.POWER_ID).amount;
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
            }
            if (p.hasPower(EmberferryPower.POWER_ID)) {
                int emberferryStacks = p.getPower(EmberferryPower.POWER_ID).amount;
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(p, p, new EmberPower(p, emberferryStacks), emberferryStacks));
            }
        }
        this.isDone = true;
    }
}
