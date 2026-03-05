package RicinMod.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import RicinMod.cards.ChronoStrike;
import RicinMod.cards.ConstellationDefend;
import RicinMod.cards.StarlightStrike;

public class EchoSlotOrb extends AbstractOrb {
    public static final String ORB_ID = "RicinMod:EchoSlot";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    /** 仿 Guardian：栏位中卡牌缩放，悬停时恢复 1.0 */
    private static final float CARD_SCALE = 0.35F;

    /** 槽位中存储的卡牌，空则为 null */
    public AbstractCard storedCard = null;

    public EchoSlotOrb() {
        this.ID = ORB_ID;
        this.name = (orbStrings != null ? orbStrings.NAME : "Echo Slot");
        this.evokeAmount = 0;
        this.passiveAmount = 0;
        this.updateDescription();
        this.channelAnimTimer = 0.5F;
        this.hideEvokeValues();
    }

    @Override
    public void updateDescription() {
        if (orbStrings == null || orbStrings.DESCRIPTION == null) {
            this.description = storedCard == null ? "Echo slot: Cards can be placed through Eternalize." : ("Echo: " + storedCard.name);
            return;
        }
        if (storedCard == null) {
            this.description = orbStrings.DESCRIPTION[0];
        } else {
            // 仅显示回响效果，不重复卡牌名
            this.description = getEchoEffectText();
        }
    }

    private String getEchoEffectText() {
        if (orbStrings == null || orbStrings.DESCRIPTION == null || orbStrings.DESCRIPTION.length < 9) return "";
        if (storedCard instanceof StarlightStrike) {
            return ((StarlightStrike) storedCard).upgraded ? orbStrings.DESCRIPTION[4] : orbStrings.DESCRIPTION[3];
        }
        if (storedCard instanceof ConstellationDefend) {
            return ((ConstellationDefend) storedCard).upgraded ? orbStrings.DESCRIPTION[6] : orbStrings.DESCRIPTION[5];
        }
        if (storedCard instanceof ChronoStrike) {
            return ((ChronoStrike) storedCard).upgraded ? orbStrings.DESCRIPTION[8] : orbStrings.DESCRIPTION[7];
        }
        return "";
    }

    public void setCard(AbstractCard card) {
        this.storedCard = card;
        if (card != null) {
            card.targetAngle = 0;
        }
        this.updateDescription();
    }

    public void clearCard() {
        this.storedCard = null;
        this.updateDescription();
    }

    /** 触发此槽位卡牌的回响效果 */
    public void triggerEcho() {
        if (storedCard == null) return;
        if (storedCard instanceof StarlightStrike) {
            ((StarlightStrike) storedCard).triggerEchoEffect(AbstractDungeon.player);
        } else if (storedCard instanceof ConstellationDefend) {
            ((ConstellationDefend) storedCard).triggerEchoEffect(AbstractDungeon.player);
        } else if (storedCard instanceof ChronoStrike) {
            ((ChronoStrike) storedCard).triggerEchoEffect(AbstractDungeon.player);
        }
    }

    private boolean isHovered() {
        this.hb.move(this.cX, this.cY);
        return this.hb.hovered;
    }

    @Override
    public void onEvoke() {
        // 引渡时由 CharonAction 处理，此处可不做
    }

    @Override
    public void update() {
        super.update();
        if (storedCard != null) {
            storedCard.target_x = this.tX;
            storedCard.target_y = this.tY;
            storedCard.targetDrawScale = this.hb.hovered ? 1.0F : CARD_SCALE;
            storedCard.update();
        }
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 10.0F;
        this.hb.move(this.cX, this.cY);
        this.hb.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(ImageMaster.ORB_SLOT_2, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
        sb.draw(ImageMaster.ORB_SLOT_1, this.cX - 48.0F, this.cY - 48.0F - this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);

        if (storedCard != null) {
            storedCard.render(sb);
        }

        if (isHovered() && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(this.cX + 60.0F * Settings.scale, this.cY + 60.0F * Settings.scale, this.name, this.description);
        }
        this.hb.render(sb);
    }

    @Override
    public AbstractOrb makeCopy() {
        EchoSlotOrb orb = new EchoSlotOrb();
        if (this.storedCard != null) {
            orb.setCard(this.storedCard.makeStatEquivalentCopy());
        }
        return orb;
    }

    @Override
    public void playChannelSFX() {
    }
}
