package RicinMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import RicinMod.characters.RicinCharacter;
import RicinMod.orbs.EchoSlotOrb;

import java.util.ArrayList;

/**
 * 将 Ricin 角色的 EmptyOrbSlot 替换为 EchoSlotOrb（回响槽位）。
 */
@SpirePatch(clz = AbstractPlayer.class, method = "increaseMaxOrbSlots")
public class EchoSlotPatch {

    @SpirePostfixPatch
    public static void Postfix(AbstractPlayer __instance, int amount, boolean __byValue) {
        if (__instance.chosenClass != RicinCharacter.PlayerColorEnum.RICIN_CHARACTER) {
            return;
        }
        ArrayList<AbstractOrb> orbs = __instance.orbs;
        for (int i = 0; i < orbs.size(); i++) {
            if (orbs.get(i) instanceof EmptyOrbSlot) {
                EchoSlotOrb echoOrb = new EchoSlotOrb();
                AbstractOrb old = orbs.get(i);
                echoOrb.cX = old.cX;
                echoOrb.cY = old.cY;
                echoOrb.tX = old.tX;
                echoOrb.tY = old.tY;
                echoOrb.hb.x = old.hb.x;
                echoOrb.hb.y = old.hb.y;
                echoOrb.hb.cX = old.hb.cX;
                echoOrb.hb.cY = old.hb.cY;
                echoOrb.hb.width = old.hb.width;
                echoOrb.hb.height = old.hb.height;
                orbs.set(i, echoOrb);
            }
        }
    }
}
