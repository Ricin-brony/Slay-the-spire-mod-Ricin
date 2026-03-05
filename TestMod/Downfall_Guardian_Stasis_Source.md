# 崩坠(Downfall) 守卫者(Guardian) 凝滞(Stasis) 功能实现 - 反编译源码

> 基于 Downfall.jar 反编译重构，仅供参考学习。原版属于 Downfall 模组。

---

## 1. StasisOrb - 凝滞球

```java
package guardian.orbs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class StasisOrb extends AbstractOrb {
    public static final String ID = "Guardian:StasisOrb";
    public static final String[] DESC;
    private static final OrbStrings orbString;

    /** 凝滞的卡牌 */
    public AbstractCard stasisCard;
    private AbstractGameEffect stasisStartEffect;
    public boolean cardExhausted;

    public StasisOrb(AbstractCard card) {
        this(card, null);
    }

    public StasisOrb(AbstractCard card, CardGroup source) {
        this(card, source, false);
    }

    public StasisOrb(AbstractCard card, CardGroup source, boolean skipInit) {
        this.ID = ID;
        this.cardExhausted = false;
        this.stasisCard = card;
        if (card instanceof AbstractGuardianCard) {
            ((AbstractGuardianCard) card).belongedOrb = this;
        }
        GuardianMod.logger.info("New Stasis Orb made");
        card.beginGlowing();
        this.name = orbString.NAME + card.name;
        this.channelAnimTimer = 0.5F;
        initialize(source, skipInit);
    }

    private void initialize(CardGroup source, boolean skipInit) {
        // 从 source 移除卡牌、设置数值等
        if (source != null) {
            source.removeCard(stasisCard);
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 根据 stasisCard 设置描述
    }

    @Override
    public void onEvoke() {
        // 凝滞球被引渡时的逻辑：将卡牌放入手牌或弃牌堆
    }

    @Override
    public void onStartOfTurn() {
        // 每回合开始减少 passiveAmount，为 0 时触发
    }

    @Override
    public AbstractOrb makeCopy() {
        return new StasisOrb(stasisCard.makeStatEquivalentCopy());
    }

    // update, render, renderActual 等...
}
```

---

## 2. PlaceActualCardIntoStasis - 将卡牌放入凝滞

```java
package guardian.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import guardian.GuardianMod;
import guardian.orbs.StasisOrb;

public class PlaceActualCardIntoStasis extends AbstractGameAction {
    private final AbstractCard card;
    private final CardGroup source;  // 卡牌来源，通常为 hand
    private boolean skipWait;
    private final boolean hadRetain;

    public PlaceActualCardIntoStasis(AbstractCard card) {
        this(card, null);
    }

    public PlaceActualCardIntoStasis(AbstractCard card, CardGroup source) {
        this.card = card;
        this.source = source;
        this.actionType = ActionType.DAMAGE;
        this.skipWait = false;
        this.hadRetain = card.retain;
        if (source != null && source.type == CardGroup.CardGroupType.HAND) {
            card.retain = true;  // 从手牌放入时暂时保留
        }
    }

    public PlaceActualCardIntoStasis(AbstractCard card, CardGroup source, boolean skipWait) {
        this(card, source);
        this.skipWait = skipWait;
    }

    @Override
    public void update() {
        if (GuardianMod.canSpawnStasisOrb()) {
            if (!AbstractDungeon.player.hasEmptyOrb()) {
                // 无空槽时：移除一个非StasisOrb的orb，触发evokeOrb腾出空间
                for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                    if (!(orb instanceof StasisOrb)) {
                        AbstractDungeon.player.orbs.remove(orb);
                        AbstractDungeon.player.orbs.add(0, orb);
                        AbstractDungeon.player.evokeOrb();
                        break;
                    }
                }
            }
            if (!skipWait && !Settings.FAST_MODE) {
                addToTop(new WaitAction(0.1F));
            }
            addToTop(new ChannelAction(new StasisOrb(card, source)));
        } else {
            card.retain = hadRetain;
            // 显示无法放入的提示
        }
        isDone = true;
    }
}
```

---

## 3. PlaceCardsInHandIntoStasisAction - 从手牌选择放入凝滞（核心流程）

```java
package guardian.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import guardian.GuardianMod;

import java.util.ArrayList;

public class PlaceCardsInHandIntoStasisAction extends AbstractGameAction {
    public static final String[] TEXT;  // 从 Guardian:UIOptions 加载

    private final boolean anyNumber;
    private final boolean endOfTurn;
    private ArrayList<AbstractCard> invalidTargets;

    public PlaceCardsInHandIntoStasisAction(AbstractCreature source, int amount, boolean anyNumber) {
        this(source, amount, anyNumber, false);
    }

    public PlaceCardsInHandIntoStasisAction(AbstractCreature source, int amount, boolean anyNumber, boolean endOfTurn) {
        this.invalidTargets = new ArrayList<>();
        setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.anyNumber = anyNumber;
        this.endOfTurn = endOfTurn;
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {
            // 初始阶段：检查能否放入、打开选择界面
            if (endOfTurn) {
                ArrayList<AbstractCard> handCopy = new ArrayList<>(AbstractDungeon.player.hand.group);
                for (AbstractCard c : handCopy) {
                    if (c.isEthereal) {
                        invalidTargets.add(c);  // 虚空牌不可选
                    }
                }
            }
            if (amount > AbstractDungeon.player.hand.size()) {
                amount = AbstractDungeon.player.hand.size();
            }
            if (amount == 0) {
                isDone = true;
                return;
            }
            if (!GuardianMod.canSpawnStasisOrb()) {
                isDone = true;
                if (!AbstractDungeon.player.hasEmptyOrb()) {
                    AbstractDungeon.effectList.add(new ThoughtBubble(
                        AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F,
                        TEXT[5], true));
                }
                return;
            }
            // 打开 HandCardSelectScreen，6 个参数: msg, numCards, anyNumber, canPickZero, forUpgrade, forTransform
            AbstractDungeon.handCardSelectScreen.open(
                TEXT[3], amount, false, anyNumber, false, anyNumber);
            tickDuration();
            return;
        }

        // 关键：当 wereCardsRetrieved 为 false 且 selectedCards 有卡时处理
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> selected = AbstractDungeon.handCardSelectScreen.selectedCards.group;

            // 1. 先 addToTop 一个回调 action (PlaceCardsInHandIntoStasisAction$1)
            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    // 用于过滤 invalidTargets 等
                    isDone = true;
                }
            });

            // 2. 按倒序添加 PlaceActualCardIntoStasis（后添加的先执行）
            for (int i = selected.size() - 1; i >= 0; i--) {
                AbstractCard card = selected.get(i);
                addToTop(new PlaceActualCardIntoStasis(card, AbstractDungeon.player.hand, true));
            }

            // 3. 【关键】将选中的卡牌加回手牌！防止被游戏默认逻辑移入弃牌堆
            for (AbstractCard card : selected) {
                AbstractDungeon.player.hand.addToTop(card);
            }

            // 4. 设置 wereCardsRetrieved = true，告诉屏幕我们已处理完毕
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        tickDuration();
    }
}
```

---

## 4. SelfStasisPatch$DontMoveStasisedCards - 阻止凝滞卡牌被错误移动

```java
// 该 Patch 注入 UseCardAction，当使用的卡牌来自凝滞球时，采取特殊处理
// 避免卡牌被常规流程（如进入弃牌堆）错误处理

@SpirePatch(clz = UseCardAction.class, method = "update")  // 具体方法名以实际为准
public static class DontMoveStasisedCards {
    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<?> SelfStasis(UseCardAction __instance, AbstractCard card, float[] duration) {
        StasisOrb orb = Fields.stasis.get(__instance);
        if (orb != null) {
            AbstractDungeon.player.cardInUse = null;
            card.exhaustOnUseOnce = false;
            card.dontTriggerOnUseCard = false;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
            if (orb.passiveAmount <= 0) {
                card.freeToPlayOnce = true;
            }
            duration[0] -= Gdx.graphics.getDeltaTime();
            if (duration[0] <= 0) {
                __instance.isDone = true;
            }
            return SpireReturn.Return(null);  // 跳过原逻辑
        }
        return SpireReturn.Continue();
    }
}
```

---

## 5. 核心要点总结

| 要点 | 说明 |
|------|------|
| **卡牌需先加回手牌** | `HandCardSelectScreen` 选中时会从手牌移除卡牌。处理前必须 `hand.addToTop(card)` 加回，否则会被游戏默认逻辑移入弃牌堆 |
| **使用 ChannelAction** | 通过 `ChannelAction(new StasisOrb(card, source))` 添加新的凝滞球，source 传入 hand 以便 StasisOrb 从中移除卡牌 |
| **wereCardsRetrieved** | 处理完后设为 `true`，通知选择界面“已取回”，界面会关闭且不再对卡牌做默认处理 |
| **处理时机** | 在 `wereCardsRetrieved == false` 且 `selectedCards` 非空时处理，抢在界面默认逻辑之前 |
| **GuardianMod.canSpawnStasisOrb()** | 检查是否能生成凝滞球（如槽位数量等限制） |
| **hasEmptyOrb()** | 无空槽时会 evoke 一个非凝滞球以腾出空间 |

---

## 6. 文件路径（Downfall 解压后）

- `guardian/orbs/StasisOrb.class`
- `guardian/actions/PlaceActualCardIntoStasis.class`
- `guardian/actions/PlaceCardsInHandIntoStasisAction.class`
- `guardian/actions/PlaceCardsInHandIntoStasisAction$1.class`
- `guardian/patches/SelfStasisPatch.class`
- `guardian/patches/SelfStasisPatch$DontMoveStasisedCards.class`
- `guardian/patches/SelfStasisPatch$Fields.class`
- `guardian/patches/SelfStasisPatch$PutInStasis.class`

---

*反编译仅供参考，实际实现以 Downfall 官方源码为准。*

---

## 7. 蜷身 (CurlUp) - 守卫者初始卡牌完整反编译

> 蜷身是 Guardian 初始卡组中的一张技能卡，效果：将手牌一张卡放入凝滞 + Brace（提防）。未升级随机选，升级后手动选。

---

### 7.1 CurlUp - 蜷身卡牌类

```java
package guardian.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import guardian.GuardianMod;
import guardian.actions.PlaceCardsInHandIntoStasisAction;
import guardian.actions.PlaceRandomCardInHandIntoStasisAction;
import guardian.patches.AbstractCardEnum;

public class CurlUp extends AbstractGuardianCard {
    public static final String ID = GuardianMod.makeID("CurlUp");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = GuardianMod.getResourcePath("cards/curlUp.png");
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;
    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int MULTICOUNT = 0;
    private static final int SOCKETS = 0;
    private static final boolean SOCKETSAREAFTER = false;

    public CurlUp() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, AbstractCardEnum.GUARDIAN, RARITY, TARGET);
        baseBlock = 10;
        multihit = 0;
        socketCount = Integer.valueOf(0);
        baseMagicNumber = magicNumber = 10;  // Brace 数值：未升级 10，升级后 12
        updateDescription();
        loadGemMisc();
        GuardianMod.loadJokeCardImage(this, "CurlUp.png");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);  // 调用父类 use（处理宝石等）
        if (!upgraded) {
            addToBot(new PlaceRandomCardInHandIntoStasisAction(p));  // 未升级：随机一张手牌放入凝滞
        } else {
            addToBot(new PlaceCardsInHandIntoStasisAction(p, 1, false));  // 升级：手动选择一张放入凝滞
        }
        brace(magicNumber);  // 提防 magicNumber 点（10 或 12）
    }

    @Override
    public AbstractCard makeCopy() {
        return new CurlUp();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(2);  // 10 -> 12
            rawDescription = UPGRADE_DESCRIPTION;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        if (socketCount != null && socketCount > 0) {
            rawDescription = upgraded && UPGRADED_DESCRIPTION != null
                ? updateGemDescription(UPGRADED_DESCRIPTION, true)
                : updateGemDescription(DESCRIPTION, true);
        }
        initializeDescription();
    }
}
```

---

### 7.2 PlaceRandomCardInHandIntoStasisAction - 随机手牌放入凝滞

```java
package guardian.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlaceRandomCardInHandIntoStasisAction extends AbstractGameAction {
    public static final String[] TEXT;  // 从 Guardian:UIOptions 加载

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Guardian:UIOptions").TEXT;
    }

    public PlaceRandomCardInHandIntoStasisAction(AbstractCreature source) {
        setValues(AbstractDungeon.player, source);
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (duration == 0.5f) {
            // 初始阶段：手牌为空则直接结束
            if (AbstractDungeon.player.hand.isEmpty()) {
                isDone = true;
                return;
            }
            addToBottom(new WaitAction(0.25f));
            tickDuration();
        } else {
            // 第二阶段：从手牌随机取一张，放入凝滞
            addToTop(new WaitAction(0.1f));
            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            addToTop(new PlaceActualCardIntoStasis(card, AbstractDungeon.player.hand));
            isDone = true;
        }
    }
}
```

---

### 7.3 AbstractGuardianCard.brace() - 提防静态方法

```java
// 位于 guardian.cards.AbstractGuardianCard 中
public static void brace(int amount) {
    if (AbstractDungeon.player == null) return;
    if (!AbstractDungeon.player.hasPower("Guardian:ModeShiftPower")) {
        // 玩家没有模式转换 Power 时，先应用 ModeShiftPower(20)
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
            AbstractDungeon.player, AbstractDungeon.player,
            new ModeShiftPower(AbstractDungeon.player, AbstractDungeon.player, 20), 20));
    }
    AbstractDungeon.actionManager.addToBottom(new BraceAction(amount));
}
```

---

### 7.4 BraceAction - 提防效果

```java
package guardian.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import guardian.powers.ModeShiftPower;

public class BraceAction extends AbstractGameAction {
    public int braceValue;

    public BraceAction(int amount) {
        braceValue = amount;
        if (AbstractDungeon.player.hasRelic("Guardian:DefensiveModeMoreBlock")) {
            braceValue += 1;  // 若有该遗物，Brace +1
        }
        duration = Settings.FAST_MODE ? 0.1f : Settings.ACTION_DUR_FASTER;
        startDuration = duration;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hasPower("Guardian:ModeShiftPower")) {
            ModeShiftPower power = (ModeShiftPower) AbstractDungeon.player.getPower("Guardian:ModeShiftPower");
            power.onSpecificTrigger(braceValue);  // 已有 ModeShiftPower 时直接叠加 Brace
        } else {
            addToTop(new BraceAction$1(this));  // 可能为动画或占位
            addToTop(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new ModeShiftPower(AbstractDungeon.player, AbstractDungeon.player, 20), 20));
        }
        isDone = true;
    }
}
```

---

### 7.5 GuardianCharacter.getStartingDeck() - 初始卡组注册

```java
@Override
public ArrayList<String> getStartingDeck() {
    ArrayList<String> ret = new ArrayList<>();
    ret.add(Strike_Guardian.ID);
    ret.add(Strike_Guardian.ID);
    ret.add(Strike_Guardian.ID);
    ret.add(Strike_Guardian.ID);
    ret.add(Defend_Guardian.ID);
    ret.add(Defend_Guardian.ID);
    ret.add(Defend_Guardian.ID);
    ret.add(Defend_Guardian.ID);
    ret.add(CurlUp.ID);       // 蜷身，初始 1 张
    ret.add(TwinSlam.ID);
    return ret;
}
```

---

### 7.6 本地化 (CardStrings)

**英文 (eng):**
```json
"Guardian:CurlUp": {
  "NAME": "Curl Up",
  "DESCRIPTION": "Place a random card from your hand into guardianmod:Stasis. NL guardianmod:Brace !M!.",
  "UPGRADE_DESCRIPTION": "Place a card in your hand into guardianmod:Stasis. NL guardianmod:Brace !M!."
}
```

**简体中文 (zhs):**
```json
"Guardian:CurlUp": {
  "NAME": "蜷身",
  "DESCRIPTION": "随机 guardianmod:凝滞 1张手牌。 NL guardianmod:提防 !M! 。",
  "UPGRADE_DESCRIPTION": "guardianmod:凝滞 1张手牌。 NL guardianmod:提防 !M! 。"
}
```

---

### 7.7 CurlUp 引用关系图

```
CurlUp
├── use()
│   ├── super.use()                    [AbstractGuardianCard]
│   ├── PlaceRandomCardInHandIntoStasisAction  (未升级)
│   │   └── PlaceActualCardIntoStasis(card, hand)
│   │       └── ChannelAction(StasisOrb)
│   ├── PlaceCardsInHandIntoStasisAction(1, false)  (升级)
│   │   └── HandCardSelectScreen → PlaceActualCardIntoStasis
│   └── brace(magicNumber)
│       └── BraceAction(amount)
│           └── ModeShiftPower.onSpecificTrigger()
└── GuardianCharacter.getStartingDeck()
    └── ret.add(CurlUp.ID)
```

---

### 7.8 Downfall.jar 中相关文件路径

| 类型 | 路径 |
|------|------|
| 卡牌 | `guardian/cards/CurlUp.class` |
| 随机放入 | `guardian/actions/PlaceRandomCardInHandIntoStasisAction.class` |
| 选择放入 | `guardian/actions/PlaceCardsInHandIntoStasisAction.class` |
| 实际放入 | `guardian/actions/PlaceActualCardIntoStasis.class` |
| 提防 | `guardian/actions/BraceAction.class` |
| 父类 | `guardian/cards/AbstractGuardianCard.class` |
| 凝滞球 | `guardian/orbs/StasisOrb.class` |
| 角色 | `guardian/characters/GuardianCharacter.class` |
| 模式转换 | `guardian/powers/ModeShiftPower.class` |
| 卡图 | `guardianResources/GuardianImages/cards/curlUp.png` |
