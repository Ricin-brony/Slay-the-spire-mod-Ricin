# 🎴 杀戮尖塔原创角色：Ricin（芮卿）

<p align="center">
  <img src="https://img.shields.io/badge/版本-1.0.0--梦开始的地方-blue" alt="版本">
  <img src="https://img.shields.io/badge/Mod%20The%20Spire-支持-brightgreen" alt="Mod The Spire">
  <img src="https://img.shields.io/badge/许可证-MIT-green" alt="许可证">
  <img src="https://img.shields.io/badge/状态-开发中-orange" alt="状态">
</p>

> 🌙 **梦境与现实的边界守护者** —— 引导迷途的灵魂安然入梦，所有相遇终将被遗忘，但那份温暖将永远留在记忆的余温中。

## 📝 项目简介

一个为《杀戮尖塔》设计的全新原创角色模组，目前已完成环境搭建、角色添加以及初始卡牌的导入。所有内容均可通过 **Mod The Spire** 直接运行。

---

## ✨ 特色功能

- **原创角色**：全新角色 “Ricin”（芮卿），拥有独特的机制与玩法
- **自定义卡牌**：已完成基础卡牌的添加，持续扩充中
- **即装即玩**：通过 Mod The Spire 加载即可体验

---

## 🛠️ 安装方法

### 前置需求
- ✅ 已安装《杀戮尖塔》（Slay the Spire）
- ✅ 已安装 [Mod The Spire](https://github.com/kiooeht/ModTheSpire/releases)
- ✅ 已安装 [BaseMod](https://github.com/daviscook477/BaseMod/releases)

### 安装步骤
1. **下载模组**：从 [Releases](https://github.com/Ricin-brony/Slay-the-spire-mod-Ricin/releases) 页面下载最新的 `.jar` 文件。
2. **放入 mods 文件夹**：
   - **Windows**：`C:\Program Files (x86)\Steam\steamapps\common\SlayTheSpire\mods`
   - **Mac**：`~/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/mods`
3. **启动游戏**：打开 Mod The Spire 启动器，勾选 `BaseMod` 和本模组，点击 **Play** 即可。

---

## 🎮 角色介绍

### Ricin（芮卿）

> 诞生于梦境与现实交汇处的天角兽，职责是守护这条脆弱的边界，引导误入此地的迷途灵魂安全返航。每一位相遇者都会在离开后遗忘祂的样貌，只留下被深刻理解的温暖感。

#### 🧬 初始机制

| 机制 | 说明 |
|------|------|
| **铭刻 (Eternalize)** | 将可以回响的卡牌置于空闲的回响栏位中。 |
| **回响 (Echo)** | 拥有该关键字的卡牌能够被铭刻，并在引渡时触发该回响效果。 |
| **悲歌 (Lament)** | 触发所有处于回响栏的卡牌的所有回响效果。 |
| **引渡 (Charon)** | 将回响栏最右侧的卡牌消耗，触发余温层数次回响效果。 |

#### 💎 初始遗物

**悬梦铃铛 (Dream Chime)**  
> 在战斗开始时，获得一层 **余温**。并使所有单位获得 3 层 **金属化**。

#### 🃏 初始卡牌

| 卡牌 | 类型 | 效果 |
|------|------|------|
| 星辉打击 (Starlight Strike) | 攻击 | 造成 6(9) 点伤害。**回响**：随机对敌人造成 3(4) 点伤害 |
| 星辰防御 (Constellation Defend) | 技能 | 获得 5(8) 点格挡。**回响**：获得 2(3) 点格挡 |
| 记忆碎片 (Memento) | 技能 | **铭刻** 1 张手牌，抽 1 张牌 |
| 低语 (Whisper) | 技能 | **悲歌** 1，**引渡** 1 |

## 🎴 全部卡牌分类

| 分类 | 卡牌列表 |
|------|----------|
| **初始卡牌** | 低语、星辉打击、星辉防御、记忆碎片 |
| **普通卡牌** | 时序打击 |
| **能力卡牌** | 余波、余温 |
| **稀有卡牌（蓝）** | 余烬引渡、催眠、绝唱、记忆宫殿 |
| **稀有卡牌（金）** | 余波、孤注一掷 |
| **特殊遗物** | 悬梦铃铛 |

---

## 🚧 开发进度

### ✅ 已完成
- [x] 开发环境搭建
- [x] 基础模组框架
- [x] 卡牌添加功能实现
- [x] 角色类实现
- [x] 初始卡牌导入
- [x] 角色图标和立绘
- [x] 遗物设计

### 🔄 进行中
- [ ] 事件交互
- [ ] 专属机制开发

### 📅 计划中
- [ ] 药水效果
- [ ] 动画效果

---

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


## 🤝 贡献指南

欢迎任何形式的贡献！如果你有好的想法或发现了 Bug：

1. 提交 [Issue](https://github.com/Ricin-brony/Slay-the-spire-mod-Ricin/issues) 反馈问题
2. 提交 Pull Request 贡献代码
3. 在 [Discussion](https://github.com/Ricin-brony/Slay-the-spire-mod-Ricin/discussions) 中讨论新特性

---

## 📜 开源协议

本项目采用 **MIT 许可证** —— 最宽松、最流行的开源协议之一，旨在鼓励代码共享与学习，共同丰富杀戮尖塔的模组生态。

### 📋 协议要点

| 权限 | 允许 | 条件 | 限制 |
|------|------|------|------|
| ✅ **商用** | 可用于商业项目 | 📝 **保留版权声明** | ⚠️ **免责声明** |
| ✅ **修改** | 可自由修改代码 | | |
| ✅ **分发** | 可重新发布 | | |
| ✅ **私用** | 可私下使用 | | |
| ✅ **专利授权** | 隐含专利授权 | | |

**简单来说：**
- **你可以**：随意使用、修改、复制、发布本模组的代码
- **你可以**：将代码用于商业项目（包括闭源项目）
- **你只需**：在软件中保留原始的版权声明
- **你无需**：公开自己的修改或衍生代码
- **我们不承担**：任何使用风险或责任

### 📄 完整许可证

项目根目录已包含 [`LICENSE`](./LICENSE) 文件

---

## 🙏 致谢

- 感谢《杀戮尖塔》开发者 **MegaCrit** 制作的优秀游戏
- 感谢 **Mod The Spire** 社区提供的模组工具
- 感谢所有为模组开发提供帮助的朋友们

## 📧 联系方式

- GitHub：[@Ricin-brony](https://github.com/Ricin-brony)

---

**注意**：本项目仍在开发中，部分功能可能还不完善。如果你在游玩过程中遇到问题，欢迎反馈！
