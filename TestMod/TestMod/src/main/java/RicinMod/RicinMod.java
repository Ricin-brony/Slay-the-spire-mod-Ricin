package RicinMod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import RicinMod.cards.Strike;

@SpireInitializer
public class RicinMod implements EditCardsSubscriber {
    
    public RicinMod() {
        BaseMod.subscribe(this);
    }
    
    public static void initialize() {
        new RicinMod();
    }
    
    @Override
    public void receiveEditCards() {
        // 向basemod注册卡牌
        BaseMod.addCard(new Strike());
    }
}
