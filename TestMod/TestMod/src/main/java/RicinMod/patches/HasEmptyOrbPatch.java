package RicinMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import RicinMod.characters.RicinCharacter;
import RicinMod.orbs.EchoSlotOrb;

/**
 * 使 Ricin 角色的 EchoSlotOrb（空槽位）被视为“空 orb 槽”，以便 ChannelAction 能正常添加 EchoOrb。
 */
@SpirePatch(clz = AbstractPlayer.class, method = "hasEmptyOrb")
public class HasEmptyOrbPatch {

    @SpirePostfixPatch
    public static boolean Postfix(boolean __result, AbstractPlayer __instance) {
        if (__result) return true;
        if (__instance.chosenClass != RicinCharacter.PlayerColorEnum.RICIN_CHARACTER || __instance.orbs == null) {
            return __result;
        }
        for (com.megacrit.cardcrawl.orbs.AbstractOrb orb : __instance.orbs) {
            if (orb instanceof EchoSlotOrb && ((EchoSlotOrb) orb).storedCard == null) {
                return true;
            }
        }
        return __result;
    }
}
