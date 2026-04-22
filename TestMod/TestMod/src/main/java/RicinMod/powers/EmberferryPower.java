package RicinMod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * 余烬引渡：层数为 N 时，每次有牌被引渡则获得 N 层 {@link RicinMod.powers.EmberPower}（余温）。
 */
public class EmberferryPower extends AbstractPower {
    public static final String POWER_ID = "RicinMod:EmberferryPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String IMG_84 = "RicinModResources/img/powers/Emberferry84.png";
    private static final String IMG_32 = "RicinModResources/img/powers/Emberferry32.png";
    private static final String FALLBACK_84 = "RicinModResources/img/powers/Ember84.png";
    private static final String FALLBACK_32 = "RicinModResources/img/powers/Ember32.png";
    private static final String FALLBACK2_84 = "RicinModResources/img/powers/EmberWake84.png";
    private static final String FALLBACK2_32 = "RicinModResources/img/powers/EmberWake32.png";

    private static TextureAtlas.AtlasRegion cached128;
    private static TextureAtlas.AtlasRegion cached48;

    private static void ensureIconCache() {
        if (cached128 != null) {
            return;
        }
        String[][] tryPaths = new String[][]{
                {IMG_84, IMG_32},
                {FALLBACK_84, FALLBACK_32},
                {FALLBACK2_84, FALLBACK2_32},
        };
        for (String[] pair : tryPaths) {
            Texture t84 = ImageMaster.loadImage(pair[0]);
            Texture t32 = ImageMaster.loadImage(pair[1]);
            if (t84 != null && t32 != null) {
                cached128 = new TextureAtlas.AtlasRegion(t84, 0, 0, 84, 84);
                cached48 = new TextureAtlas.AtlasRegion(t32, 0, 0, 32, 32);
                return;
            }
        }
        throw new IllegalStateException(
                "EmberferryPower: no power textures found. Add Emberferry84/32.png or ensure Ember/EmberWake power images exist.");
    }

    public EmberferryPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

        ensureIconCache();
        this.region128 = cached128;
        this.region48 = cached48;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }
}
