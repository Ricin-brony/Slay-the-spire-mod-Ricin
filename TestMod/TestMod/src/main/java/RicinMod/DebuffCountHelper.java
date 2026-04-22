package RicinMod;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;

/** 统计玩家当前身上的负面能力（Debuff）数量。 */
public final class DebuffCountHelper {
    private DebuffCountHelper() {
    }

    public static int countPlayerDebuffs(AbstractPlayer p) {
        if (p == null || p.powers == null) {
            return 0;
        }
        int n = 0;
        for (AbstractPower pow : p.powers) {
            if (pow != null && pow.type == AbstractPower.PowerType.DEBUFF) {
                n++;
            }
        }
        return n;
    }
}
