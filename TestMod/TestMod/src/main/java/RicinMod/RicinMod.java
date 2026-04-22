package RicinMod;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import RicinMod.cards.ChronoStrike;
import RicinMod.cards.ConstellationDefend;
import RicinMod.cards.DreamWard;
import RicinMod.cards.Emberferry;
import RicinMod.cards.EmberWake;
import RicinMod.cards.Finale;
import RicinMod.cards.FlawEcho;
import RicinMod.cards.MemoryPalace;
import RicinMod.cards.Memento;
import RicinMod.cards.SolemnOath;
import RicinMod.cards.StakingStardust;
import RicinMod.cards.StarlightStrike;
import RicinMod.cards.Whisper;
import RicinMod.orbs.EchoSlotOrb;
import RicinMod.relics.DreamChimeRelic;
import RicinMod.characters.RicinCharacter;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@SpireInitializer
public class RicinMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, OnStartBattleSubscriber {
    
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

    /** 仿 GuardianMod.canSpawnStasisOrb：Ricin 角色是否有空铭刻槽可放入卡牌 */
    public static boolean canSpawnEchoOrb() {
        com.megacrit.cardcrawl.characters.AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        if (p == null || p.chosenClass != RicinCharacter.PlayerColorEnum.RICIN_CHARACTER || p.orbs == null) {
            return false;
        }
        if (p.hasEmptyOrb()) return true;
        for (com.megacrit.cardcrawl.orbs.AbstractOrb orb : p.orbs) {
            if (orb instanceof EchoSlotOrb && ((EchoSlotOrb) orb).storedCard == null) {
                return true;
            }
        }
        return false;
    }
    
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
        BaseMod.addCard(new StarlightStrike());
        BaseMod.addCard(new ChronoStrike());
        BaseMod.addCard(new FlawEcho());
        BaseMod.addCard(new Finale());
        BaseMod.addCard(new ConstellationDefend());
        BaseMod.addCard(new MemoryPalace());
        BaseMod.addCard(new DreamWard());
        BaseMod.addCard(new Memento());
        BaseMod.addCard(new Whisper());
        BaseMod.addCard(new Emberferry());
        BaseMod.addCard(new EmberWake());
        BaseMod.addCard(new SolemnOath());
        BaseMod.addCard(new StakingStardust());
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
        BaseMod.loadCustomStringsFile(PowerStrings.class, "RicinModResources/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "RicinModResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, "RicinModResources/localization/" + lang + "/orbs.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "RicinModResources/localization/" + lang + "/UIStrings.json");
    }

    @Override
    public void receiveOnBattleStart(com.megacrit.cardcrawl.rooms.AbstractRoom room) {
        com.megacrit.cardcrawl.characters.AbstractPlayer p = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        if (p != null && p.chosenClass == RicinCharacter.PlayerColorEnum.RICIN_CHARACTER && p.orbs != null) {
            for (int i = 0; i < p.orbs.size(); i++) {
                if (p.orbs.get(i) instanceof com.megacrit.cardcrawl.orbs.EmptyOrbSlot) {
                    EchoSlotOrb echoOrb = new EchoSlotOrb();
                    com.megacrit.cardcrawl.orbs.AbstractOrb old = p.orbs.get(i);
                    echoOrb.cX = old.cX;
                    echoOrb.cY = old.cY;
                    echoOrb.tX = old.tX;
                    echoOrb.tY = old.tY;
                    echoOrb.hb.x = old.hb.x;
                    echoOrb.hb.y = old.hb.y;
                    echoOrb.hb.cX = old.hb.cX;
                    echoOrb.hb.cY = old.hb.cY;
                    echoOrb.hb.width = old.hb.width;
                    echoOrb.hb.height = old.hb.height;
                    p.orbs.set(i, echoOrb);
                }
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new DreamChimeRelic(), RicinCharacter.PlayerColorEnum.RICIN_BLUE);
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
