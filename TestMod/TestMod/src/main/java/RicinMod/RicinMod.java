package RicinMod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import RicinMod.cards.Defend;
import RicinMod.cards.Strike;
import RicinMod.characters.RicinCharacter;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@SpireInitializer
public class RicinMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber {
    
    private static final String CHAR_BUTTON = "RicinModResources/img/char/Character_Button.png";
    private static final String CHAR_PORTRAIT = "RicinModResources/img/char/Character_Portrait.png";
    private static final String BG_ATTACK_512 = "RicinModResources/img/512/bg_attack_512.png";
    private static final String BG_SKILL_512 = "RicinModResources/img/512/bg_skill_512.png";
    private static final String BG_POWER_512 = "RicinModResources/img/512/bg_power_512.png";
    private static final String BG_ATTACK_1024 = "RicinModResources/img/1024/bg_attack.png";
    private static final String BG_SKILL_1024 = "RicinModResources/img/1024/bg_skill.png";
    private static final String BG_POWER_1024 = "RicinModResources/img/1024/bg_power.png";
    private static final String SMALL_ORB = "RicinModResources/img/char/small_orb.png";
    private static final String BIG_ORB = "RicinModResources/img/char/card_orb.png";
    private static final String ENERGY_ORB = "RicinModResources/img/char/cost_orb.png";
    
    public static final Color RICIN_COLOR = new Color(0.0F, 26.0F / 255.0F, 255.0F / 255.0F, 1.0F);
    
    public RicinMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(RicinCharacter.PlayerColorEnum.RICIN_BLUE, RICIN_COLOR, RICIN_COLOR, RICIN_COLOR, RICIN_COLOR,
            RICIN_COLOR, RICIN_COLOR, RICIN_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512,
            ENERGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }
    
    public static void initialize() {
        new RicinMod();
    }
    
    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new RicinCharacter(CardCrawlGame.playerName), CHAR_BUTTON, CHAR_PORTRAIT, RicinCharacter.PlayerColorEnum.RICIN_CHARACTER);
    }
    
    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "RicinModResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "RicinModResources/localization/" + lang + "/characters.json");
    }

    @Override
    public void receiveEditKeywords() {
        String lang = (Settings.language == Settings.GameLanguage.ZHS) ? "ZHS" : "ENG";
        String path = "/RicinModResources/localization/" + lang + "/Keywords.json";
        java.io.InputStream is = RicinMod.class.getResourceAsStream(path);
        if (is == null) return;
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            KeywordsData[] keywords = gson.fromJson(reader, KeywordsData[].class);
            if (keywords != null) {
                for (KeywordsData kw : keywords) {
                    if (kw.NAMES != null && kw.NAMES.length > 0) {
                        BaseMod.addKeyword("ricinmod", kw.NAMES[0], kw.NAMES, kw.DESCRIPTION);
                    }
                }
            }
        } catch (Exception e) {
            BaseMod.logger.error("RicinMod: Failed to load keywords from " + path, e);
        }
    }
}
