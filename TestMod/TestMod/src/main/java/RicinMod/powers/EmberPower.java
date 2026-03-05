package RicinMod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EmberPower extends AbstractPower {
    public static final String POWER_ID = "RicinMod:EmberPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final String IMG_84 = "RicinModResources/img/powers/Ember84.png";
    private static final String IMG_32 = "RicinModResources/img/powers/Ember32.png";

    /** 静态加载，避免重复占用内存 */
    private static TextureAtlas.AtlasRegion cached128;
    private static TextureAtlas.AtlasRegion cached48;

    public EmberPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;

        if (cached128 == null) {
            cached128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_84), 0, 0, 84, 84);
            cached48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(IMG_32), 0, 0, 32, 32);
        }
        this.region128 = cached128;
        this.region48 = cached48;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
}
