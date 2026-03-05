package RicinMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import RicinMod.characters.RicinCharacter;
import RicinMod.orbs.EchoOrb;
import RicinMod.orbs.EchoSlotOrb;

/**
 * 使 Ricin 角色能够通过 ChannelAction 添加 EchoOrb。
 * 当 channel 的是 EchoOrb 时，将第一个空的 EchoSlotOrb 替换为 EchoOrb（仿 Downfall Stasis 的 channel 行为）。
 */
@SpirePatch(clz = AbstractPlayer.class, method = "channelOrb", optional = true)
public class ChannelOrbPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(AbstractPlayer __instance, AbstractOrb orbToChannel) {
        if (__instance.chosenClass != RicinCharacter.PlayerColorEnum.RICIN_CHARACTER) {
            return SpireReturn.Continue();
        }
        if (!(orbToChannel instanceof EchoOrb)) {
            return SpireReturn.Continue();
        }
        for (int i = 0; i < __instance.orbs.size(); i++) {
            AbstractOrb o = __instance.orbs.get(i);
            if (o instanceof EmptyOrbSlot || (o instanceof EchoSlotOrb && ((EchoSlotOrb) o).storedCard == null)) {
                orbToChannel.cX = o.cX;
                orbToChannel.cY = o.cY;
                orbToChannel.tX = o.tX;
                orbToChannel.tY = o.tY;
                orbToChannel.hb.x = o.hb.x;
                orbToChannel.hb.y = o.hb.y;
                orbToChannel.hb.cX = o.hb.cX;
                orbToChannel.hb.cY = o.hb.cY;
                orbToChannel.hb.width = o.hb.width;
                orbToChannel.hb.height = o.hb.height;
                __instance.orbs.set(i, orbToChannel);
                orbToChannel.playChannelSFX();
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}
