package RicinMod;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import RicinMod.cards.ChronoStrike;
import RicinMod.cards.ConstellationDefend;
import RicinMod.cards.StarlightStrike;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import RicinMod.orbs.EchoOrb;
import RicinMod.orbs.EchoSlotOrb;
import RicinMod.powers.EmberPower;

/**
 * 回响栏辅助：管理回响槽位中的卡牌。
 * 铭刻卡牌存入 EchoOrb（通过 Channel 添加），空槽位为 EchoSlotOrb。
 */
public class EchoBarHelper {

    /** 检查卡牌是否具有回响效果（悲歌/引渡时触发） */
    public static boolean hasEchoEffect(com.megacrit.cardcrawl.cards.AbstractCard card) {
        return card instanceof StarlightStrike || card instanceof ConstellationDefend || card instanceof ChronoStrike;
    }

    /** 触发所有回响槽位中卡牌的回响效果（EchoOrb 或 EchoSlotOrb 中有卡时） */
    public static void triggerAllEchoEffects(AbstractPlayer p) {
        if (p == null || p.orbs == null) return;
        for (com.megacrit.cardcrawl.orbs.AbstractOrb orb : p.orbs) {
            if (orb instanceof EchoOrb) {
                ((EchoOrb) orb).triggerEcho();
            } else if (orb instanceof EchoSlotOrb) {
                ((EchoSlotOrb) orb).triggerEcho();
            }
        }
    }

    /**
     * 引渡：消耗回响栏最右侧的铭刻卡牌，按余温层数触发回响效果。
     * 仅将卡牌消耗，栏位保留（变为空槽位）。
     * 仿原版 evokeOrb/Dualcast：Defect 布局中 orbs[0]=最右侧（主动），orbs[size-1]=最左侧。
     * 从索引 0 向左查找第一个有卡的 orb。
     */
    public static boolean charonRightmost(AbstractPlayer p) {
        if (p == null || p.orbs == null || p.orbs.isEmpty()) return false;
        int rightmostIdx = -1;
        for (int i = 0; i < p.orbs.size(); i++) {
            AbstractOrb orb = p.orbs.get(i);
            if (orb instanceof EchoOrb && ((EchoOrb) orb).storedCard != null) {
                rightmostIdx = i;
                break;
            }
            if (orb instanceof EchoSlotOrb && ((EchoSlotOrb) orb).storedCard != null) {
                rightmostIdx = i;
                break;
            }
        }
        if (rightmostIdx < 0) return false;
        AbstractOrb orb = p.orbs.get(rightmostIdx);
        com.megacrit.cardcrawl.cards.AbstractCard cardToExhaust = null;
        if (orb instanceof EchoOrb) {
            EchoOrb eo = (EchoOrb) orb;
            int ember = p.hasPower(EmberPower.POWER_ID) ? p.getPower(EmberPower.POWER_ID).amount : 1;
            for (int j = 0; j < ember; j++) {
                eo.triggerEcho();
            }
            cardToExhaust = eo.storedCard;
            eo.storedCard = null;
        } else if (orb instanceof EchoSlotOrb) {
            EchoSlotOrb es = (EchoSlotOrb) orb;
            int ember = p.hasPower(EmberPower.POWER_ID) ? p.getPower(EmberPower.POWER_ID).amount : 1;
            for (int j = 0; j < ember; j++) {
                es.triggerEcho();
            }
            cardToExhaust = es.storedCard;
            es.clearCard();
        }
        if (cardToExhaust == null) return false;
        p.exhaustPile.addToTop(cardToExhaust);

        // 空槽置于最左侧，右侧有卡 orb 向左（索引减小方向）补位
        AbstractOrb emptyOrb;
        if (orb instanceof EchoOrb) {
            EchoSlotOrb newSlot = new EchoSlotOrb();
            newSlot.cX = orb.cX;
            newSlot.cY = orb.cY;
            newSlot.tX = orb.tX;
            newSlot.tY = orb.tY;
            newSlot.hb.x = orb.hb.x;
            newSlot.hb.y = orb.hb.y;
            newSlot.hb.cX = orb.hb.cX;
            newSlot.hb.cY = orb.hb.cY;
            newSlot.hb.width = orb.hb.width;
            newSlot.hb.height = orb.hb.height;
            emptyOrb = newSlot;
        } else {
            emptyOrb = orb;
        }
        for (int i = rightmostIdx + 1; i < p.orbs.size(); i++) {
            p.orbs.set(i - 1, p.orbs.get(i));
        }
        p.orbs.set(p.orbs.size() - 1, emptyOrb);
        for (int i = 0; i < p.orbs.size(); i++) {
            p.orbs.get(i).setSlot(i, p.maxOrbs);
        }
        return true;
    }
}
