package RicinMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import RicinMod.EchoBarHelper;
import RicinMod.RicinMod;

import java.util.ArrayList;

/**
 * 仿 DualWieldAction：从手牌选择带回响的卡牌放入铭刻槽。
 * 关键：打开选择界面前，将非回响卡牌从手牌临时移除（hand.removeAll），
 * 界面上仅显示可选卡牌；处理完成后通过 returnCards() 加回。
 */
public class PlaceCardsInHandIntoEchoAction extends AbstractGameAction {
    private static final String[] TEXT = getUIOptions();

    private static String[] getUIOptions() {
        try {
            return com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getUIString("RicinMod:EchoUIOptions").TEXT;
        } catch (Exception e) {
            return new String[]{"Choose a card to Eternalize.", "Select.", "place into Echo."};
        }
    }

    private final boolean anyNumber;
    /** 不可铭刻的卡牌（无回响效果），打开界面前从手牌移除，完成后加回。仿 DualWieldAction.cannotDuplicate */
    private final ArrayList<AbstractCard> cannotEternalize = new ArrayList<>();
    private boolean cardsReturned = false;

    public PlaceCardsInHandIntoEchoAction(AbstractCreature source, int amount, boolean anyNumber) {
        this(source, amount, anyNumber, false);
    }

    public PlaceCardsInHandIntoEchoAction(AbstractCreature source, int amount, boolean anyNumber, boolean endOfTurn) {
        setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = 0.5F;
        this.startDuration = 0.5F;
        this.anyNumber = anyNumber;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (!EchoBarHelper.hasEchoEffect(c)) cannotEternalize.add(c);
            }
            AbstractDungeon.player.hand.group.removeAll(cannotEternalize);

            int echoCount = AbstractDungeon.player.hand.size();
            amount = Math.min(amount, Math.min(echoCount, AbstractDungeon.player.hand.group.size()));
            if (amount == 0) {
                returnCards();
                isDone = true;
                return;
            }
            if (!RicinMod.canSpawnEchoOrb()) {
                returnCards();
                isDone = true;
                String msg = TEXT != null && TEXT.length > 3 ? TEXT[3] : "No empty Echo slot.";
                AbstractDungeon.effectList.add(new ThoughtBubble(
                        AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F,
                        msg, true));
                return;
            }

            AbstractDungeon.handCardSelectScreen.open(
                    TEXT != null && TEXT.length > 0 ? TEXT[0] : "Choose a card to Eternalize.",
                    amount, false, anyNumber, false, anyNumber);
            tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> selected = AbstractDungeon.handCardSelectScreen.selectedCards.group;
            if (!selected.isEmpty()) {
                for (int i = selected.size() - 1; i >= 0; i--) {
                    AbstractCard card = selected.get(i);
                    addToTop(new PlaceActualCardIntoEcho(card, AbstractDungeon.player.hand, true));
                }
                for (AbstractCard card : selected) {
                    AbstractDungeon.player.hand.addToTop(card);
                }
                returnCards();
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                isDone = true;
            }
        } else {
            returnCards();
            isDone = true;
        }

        tickDuration();
    }

    private void returnCards() {
        if (cardsReturned) return;
        cardsReturned = true;
        for (AbstractCard c : cannotEternalize) {
            AbstractDungeon.player.hand.addToTop(c);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
}
