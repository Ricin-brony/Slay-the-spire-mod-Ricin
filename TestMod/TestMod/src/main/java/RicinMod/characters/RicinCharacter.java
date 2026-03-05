package RicinMod.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import RicinMod.RicinMod;
import RicinMod.cards.ConstellationDefend;
import RicinMod.cards.Memento;
import RicinMod.cards.StarlightStrike;
import RicinMod.cards.Whisper;
import RicinMod.relics.DreamChimeRelic;

import java.util.ArrayList;

public class RicinCharacter extends CustomPlayer {
    private static final String SHOULDER_1 = "RicinModResources/img/char/shoulder1.png";
    private static final String SHOULDER_2 = "RicinModResources/img/char/shoulder2.png";
    private static final String CORPSE_IMAGE = "RicinModResources/img/char/corpse.png";
    private static final String[] ORB_TEXTURES = new String[]{
            "RicinModResources/img/UI/orb/layer5.png",
            "RicinModResources/img/UI/orb/layer4.png",
            "RicinModResources/img/UI/orb/layer3.png",
            "RicinModResources/img/UI/orb/layer2.png",
            "RicinModResources/img/UI/orb/layer1.png",
            "RicinModResources/img/UI/orb/layer6.png",
            "RicinModResources/img/UI/orb/layer5d.png",
            "RicinModResources/img/UI/orb/layer4d.png",
            "RicinModResources/img/UI/orb/layer3d.png",
            "RicinModResources/img/UI/orb/layer2d.png",
            "RicinModResources/img/UI/orb/layer1d.png"
    };
    private static final float[] LAYER_SPEED = new float[]{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("RicinMod:RicinCharacter");

    public RicinCharacter(String name) {
        super(name, RicinCharacter.PlayerColorEnum.RICIN_CHARACTER, ORB_TEXTURES, "RicinModResources/img/UI/orb/vfx.png", LAYER_SPEED, null, null);

        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 150.0F * Settings.scale);

        this.initializeClass(
                "RicinModResources/img/char/character.png",
                SHOULDER_2, SHOULDER_1,
                CORPSE_IMAGE,
                this.getLoadout(),
                0.0F, 0.0F,
                200.0F, 220.0F,
                new EnergyManager(3)
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            retVal.add(StarlightStrike.ID);
        }
        for (int i = 0; i < 4; i++) {
            retVal.add(ConstellationDefend.ID);
        }
        retVal.add(Memento.ID);
        retVal.add(Whisper.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(DreamChimeRelic.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                characterStrings.NAMES[0],
                characterStrings.TEXT[0],
                75,
                75,
                3,
                99,
                5,
                this,
                this.getStartingRelics(),
                this.getStartingDeck(),
                false
        );
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return RicinCharacter.PlayerColorEnum.RICIN_BLUE;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new StarlightStrike();
    }

    @Override
    public Color getCardTrailColor() {
        return RicinMod.RICIN_COLOR;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new RicinCharacter(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return RicinMod.RICIN_COLOR;
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[0];
    }

    @Override
    public Color getCardRenderColor() {
        return RicinMod.RICIN_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    public static class PlayerColorEnum {
        @SpireEnum
        public static AbstractPlayer.PlayerClass RICIN_CHARACTER;

        @SpireEnum
        public static AbstractCard.CardColor RICIN_BLUE;
    }

    public static class PlayerLibraryEnum {
        @SpireEnum
        public static com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType RICIN_BLUE;
    }
}
