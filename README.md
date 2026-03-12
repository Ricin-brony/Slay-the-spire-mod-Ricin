# 杀戮尖塔原创角色：Ricin（芮卿）

> 一个为《杀戮尖塔》设计的全新原创角色模组

## 📝 项目简介

目前已完成环境搭建以及卡牌添加。
现在在制作角色添加并且将初始卡牌导入。
目前所有项目内容均可以通过 Mod The Spire 直接运行。

## ✨ 特色功能

- **原创角色**：全新角色 "Ricin"（芮卿），拥有独特的机制和玩法
- **自定义卡牌**：已完成基础卡牌的添加，持续扩充中
- **即装即玩**：通过 Mod The Spire 加载即可体验

## 🛠️ 安装方法

### 前置需求
- 已安装《杀戮尖塔》（Slay the Spire）
- 已安装 [Mod The Spire](https://github.com/kiooeht/ModTheSpire/releases)
- 已安装 [BaseMod](https://github.com/daviscook477/BaseMod/releases)

### 安装步骤
1. 下载本模组的最新 `.jar` 文件（从 Releases 页面）
2. 将下载的 `.jar` 文件放入游戏目录的 `mods` 文件夹中
   - Windows 默认路径：`C：\Program Files （x86）\Steam\steamapps\common\SlayTheSpire\mods`
   - Mac 默认路径：`~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/mods`
3. 启动 Mod The Spire 启动器
4. 勾选 "BaseMod" 和本模组
5. 点击 Play 开始游戏

---

## 🎮 角色介绍

### Ricin（芮卿）
梦境与现实的边界守护者。作为概念交汇处诞生的天角兽，祂引导迷途的灵魂安然入梦。所有相遇终将被遗忘，但那份温暖将永远留在记忆的余温中

**初始机制**：
- 初始遗物：
悬梦铃铛(Dream Chime)：在战斗开始时，获得一层余温。并使所有单位获得3层金属化。
- 新关键词：
铭刻(Eternalize)：将可以回响的卡牌置于空闲的回响栏位中。
回响(Echo)：拥有该关键字的卡牌能够被铭刻，并在引渡时触发该回响效果。
悲歌(Lament)：触发所有处于回响栏的卡牌的所有回响效果。
引渡(Charon)：将回响栏最右侧的卡牌消耗，触发余温层数次回响效果。

**初始卡牌**：
- 攻击-1-星辉打击 (Starlight Strike)：造成6(9)点伤害。回响：随机对敌人造成3(4)点伤害
- 技能-1-星辰防御 (Constellation defend)：获得5(8)点格挡。回响：获得2(3)点格挡
- 技能-1(0)-记忆碎片(Memento)：铭刻1张手牌，抽1张牌
- 技能-1(0)-低语(whisper)：悲歌1，引渡1。

---

## 🚧 开发进度

### 已完成 ✅
- [x] 开发环境搭建
- [x] 基础模组框架
- [x] 卡牌添加功能实现
- [x] 角色类实现
- [x] 初始卡牌导入
- [x] 角色图标和立绘
- [x] 遗物设计
### 进行中 🔄
- [ ] 事件交互
- [ ] 专属机制开发
### 计划中 📅
- [ ] 药水效果
- [ ] 动画效果

## 📁 项目结构

```
RicinMod/
├── src/main/java/RicinMod/          # 核心代码
│   ├── actions/                      # 卡牌动作/效果
│   ├── cards/                        # 卡牌定义
│   │   ├── ability/                   # 能力卡牌
│   │   ├── blue_cards/                # 稀有卡牌（蓝）
│   │   ├── golden_cards/              # 稀有卡牌（金）
│   │   ├── initial_cards/             # 初始卡组
│   │   └── white_cards/               # 普通卡牌（白）
│   ├── characters/                    # 角色实现
│   ├── orbs/                          # 球体机制
│   ├── powers/                        # 能力状态
│   ├── relics/                        # 遗物
│   │   └── legacy/                     # 特殊遗物
│   └── patches/                       # 游戏补丁/兼容
│
├── src/main/resources/               # 资源文件
│   ├── RicinModResources/            
│   │   ├── img/                        # 图片资源
│   │   │   ├── 1024/                    # 高分辨率图片
│   │   │   ├── 512/                     # 标准分辨率
│   │   │   ├── cards/                   # 卡牌立绘
│   │   │   ├── char/                    # 角色立绘
│   │   │   ├── powers/                  # 能力图标
│   │   │   ├── relics/                  # 遗物图标
│   │   │   └── UI/                      # 界面元素
│   │   │       └── orb/                  # 球体UI
│   │   └── localization/               # 本地化文件
│   │       ├── ENG/                      # 英文
│   │       └── ZHS/                      # 简体中文
│   └── META-INF/                       # 模组配置
│
└── lib/                               # 依赖库
```

---

## 🎴 卡牌分类

| 分类 | 说明 |
|------|------|
| **初始卡牌** | 低语、星辉打击、星辉防御、记忆碎片 |
| **普通卡牌** | 时序打击 |
| **能力卡牌** | 余波、余温 |
| **稀有卡牌（蓝）** | 余烬引渡、催眠、绝唱、记忆宫殿 |
| **稀有卡牌（金）** | 余波、孤注一掷 |
| **特殊遗物** | 悬梦铃铛 |

## 🎯 核心机制

- **记忆余温** - 角色核心资源，通过卡牌积累与消耗
- **梦境与现实** - 独特的双状态转换机制
- **引渡** - 特殊的卡牌效果触发方式

---

## 🤝 贡献指南

欢迎任何形式的贡献！如果你有好的想法或发现了 Bug：

1. 提交 [Issue](https：//github.com/Ricin-brony/Slay-the-spire-mod-Ricin/issues) 反馈问题
2. 提交 Pull Request 贡献代码
3. 在 Discussion 中讨论新特性


## 📜 开源协议

*（这里可以添加你选择的开源协议，如 MIT、GPL-3.0 等）*


## 🙏 致谢

- 感谢《杀戮尖塔》开发者 MegaCrit 制作的优秀游戏
- 感谢 Mod The Spire 社区提供的模组工具
- 感谢所有为模组开发提供帮助的朋友们

## 📧 联系方式

- GitHub：[@Ricin-brony](https：//github.com/Ricin-brony)

---

**注意**：本项目仍在开发中，部分功能可能还不完善。如果你在游玩过程中遇到问题，欢迎反馈！
