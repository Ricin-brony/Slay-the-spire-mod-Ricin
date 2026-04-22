package RicinMod.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import RicinMod.EchoBarHelper;
import RicinMod.cards.ChronoStrike;
import RicinMod.cards.ConstellationDefend;
import RicinMod.cards.FlawEcho;
import RicinMod.cards.StarlightStrike;

/**
 * 铭刻球：仿照 Downfall StasisOrb，通过 ChannelAction 添加，装有一张卡牌。
 * 与 EchoSlotOrb（空槽位）配合使用。
 */
public class EchoOrb extends AbstractOrb {
    public static final String ORB_ID = "RicinMod:EchoOrb";
    private static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    /** 仿 Guardian stasisCardRenderScale：栏位中卡牌缩放比例，悬停时恢复 1.0 */
    private static final float CARD_SCALE = 0.25F;

    /** 铭刻的卡牌 */
    public AbstractCard storedCard;

    /**
     * 仿 StasisOrb：从 source 移除卡牌并存入此 orb。
     */
    public EchoOrb(AbstractCard card, CardGroup source) {
        this.ID = ORB_ID;
        this.evokeAmount = 0;
        this.passiveAmount = 0;
        this.storedCard = card;
        if (source != null) {
            source.removeCard(card);
        }
        this.name = orbStrings != null ? (orbStrings.NAME + card.name) : ("Echo: " + card.name);
        this.updateDescription();
        this.channelAnimTimer = 0.5F;
        this.hideEvokeValues();
        if (card != null) {
            card.beginGlowing();
            card.targetAngle = 0;
        }
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
    public void updateDescription() {
        if (orbStrings == null || orbStrings.DESCRIPTION == null) {
            this.description = storedCard == null ? "Empty" : ("Echo: " + storedCard.name);
            return;
        }
        if (storedCard == null) {
            this.description = orbStrings.DESCRIPTION[0];
        } else {
            // 仅显示回响效果，不重复卡牌名（name 已包含）
            this.description = getEchoEffectText();
        }
    }

    private String getEchoEffectText() {
        if (orbStrings == null || orbStrings.DESCRIPTION == null || orbStrings.DESCRIPTION.length < 11) {
            return "";
        }
        if (storedCard instanceof StarlightStrike) {
            return ((StarlightStrike) storedCard).upgraded ? orbStrings.DESCRIPTION[4] : orbStrings.DESCRIPTION[3];
        }
        if (storedCard instanceof ConstellationDefend) {
            return ((ConstellationDefend) storedCard).upgraded ? orbStrings.DESCRIPTION[6] : orbStrings.DESCRIPTION[5];
        }
        if (storedCard instanceof ChronoStrike) {
            return ((ChronoStrike) storedCard).upgraded ? orbStrings.DESCRIPTION[8] : orbStrings.DESCRIPTION[7];
        }
        if (storedCard instanceof FlawEcho) {
            return ((FlawEcho) storedCard).upgraded ? orbStrings.DESCRIPTION[10] : orbStrings.DESCRIPTION[9];
        }
        return "";
    }

    /** 触发此 orb 中卡牌的回响效果 */
    public void triggerEcho() {
        if (storedCard == null) return;
        if (EchoBarHelper.hasEchoEffect(storedCard)) {
            if (storedCard instanceof StarlightStrike) {
                ((StarlightStrike) storedCard).triggerEchoEffect(AbstractDungeon.player);
            } else if (storedCard instanceof ConstellationDefend) {
                ((ConstellationDefend) storedCard).triggerEchoEffect(AbstractDungeon.player);
            } else if (storedCard instanceof ChronoStrike) {
                ((ChronoStrike) storedCard).triggerEchoEffect(AbstractDungeon.player);
            } else if (storedCard instanceof FlawEcho) {
                ((FlawEcho) storedCard).triggerEchoEffect(AbstractDungeon.player);
            }
        }
    }

    private boolean isHovered() {
        this.hb.move(this.cX, this.cY);
        return this.hb.hovered;
    }

    @Override
    public void onEvoke() {
        if (storedCard != null) {
            AbstractDungeon.player.exhaustPile.addToTop(storedCard);
            storedCard = null;
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
        return new EchoOrb(storedCard != null ? storedCard.makeStatEquivalentCopy() : null, null);
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ORB_CHANNEL", 0.1F);
    }
}
