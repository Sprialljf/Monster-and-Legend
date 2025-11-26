# Legends: Heroes and Monsters

## 📖 Project Overview
**Legends** is an immersive, modular, text-based Role-Playing Game (RPG) developed in Java. Players command a party of three heroes—Warrior, Sorcerer, and Paladin—to explore a dynamically generated world, trade in markets, and defeat legendary bosses.

The project demonstrates advanced **Object-Oriented Programming (OOP)** principles and uses the **MVC architecture** to separate game logic, data, and presentation.

---

## ✨ Key Features

### 1. Epic Boss Mechanics (New!)
The game features unique mechanics for floor bosses, handled by a specialized `BossBattle` logic engine:

* **Floor 1 - Skeleton King (Leoric)**:
    * **Bone Armor**: Highly resistant to **Sword/Blade** category weapons (only takes 10% damage).
    * **Weakness**: Vulnerable to **Blunt** weapons like Hammers/Maces (takes 150% damage).
* **Floor 2 - The Dragon King**:
    * **Aerial Defense**: **Immune** to all melee physical attacks (Swords, Axes, etc.).
    * **Ranged Vulnerability**: Can only be damaged physically by weapons in the **Bow** category.
    * **Magic Vulnerability**: Takes full damage from all **Spells**.
    * **Regeneration**: Heals **5% Max HP** at the start of every round.
* **Floor 3 - The Lich King (Arthas)**:
    * **Necromancy**: Summons new minions (Spirits & Dragons) every few rounds, sacrificing old ones.
    * **Legendary Event**: Survive until **Round 5** to trigger a game-changing event.
    * **Allies Assemble**: Previous bosses (Leoric & Dragon King) join your party as allies!
    * **Frostmourne**: The legendary sword drops mid-battle. It deals **True Damage (33% of Max HP)** to the Lich King.

### 2. Hero Class System
* **Warrior**: High HP and Strength. Favors physical damage.
* **Sorcerer**: High Dexterity (Magic Dmg) and Agility (Dodge).
* **Paladin**: Balanced stats with high durability.

### 3. Dynamic World & Economy
* **Randomized Map**: Features Common cells (battles), Market cells (shops), and Obstacles.
* **Map Scaling**: The world expands as you progress (Floor 1: 8x8 -> Floor 2: 10x10 -> Floor 3: 12x12).
* **Market**: Buy/Sell Weapons, Armor, Potions, and Spells using gold earned from combat.

---

## 🎮 Controls

### Exploration
| Key | Action | Description |
| :---: | :--- | :--- |
| **W/A/S/D** | Move | Up / Left / Down / Right |
| **E** | Interact | Enter Market / Check Cell |
| **I** | Info | View Inventory & Stats |
| **B** | Boss | Challenge the Floor Boss |
| **Q** | Quit | Exit Game |

### Combat
* `1` **Attack**: Physical attack (One-handed / Two-handed / Dual-wield).
* `2` **Potion**: Use consumable items.
* `3` **Spell**: Cast magic (Ice/Fire/Lightning).
* `4` **Stats**: View battle status.

---

## 📂 File Structure

### `legends.app`
* `Main.java`: Entry point. Initializes the game.

### `legends.game`
* `GameController.java`: Central controller managing game flow, map navigation, and state.
* `Battle.java`: Core turn-based combat loop.
* `BossBattle.java`: **[New]** Encapsulates special boss mechanics (Summoning, Story Events, Damage Logic).
* `UI.java`: Handles console formatting and color output.

### `legends.world`
* `GameMap.java`: Grid-based map logic.
* `Cell.java` & Subclasses: Represents different tile types (Common, Market, Inaccessible).

### `legends.characters`
* **Heroes**: `Hero` (Abstract), `Warrior`, `Sorcerer`, `Paladin`.
* **Monsters**: `Monster` (Abstract), `Dragon`, `Spirit`, `Exoskeleton`.
* **Bosses**: `LichKingBoss`, `SkeletonKingBoss`, `DragonKingBoss`.

### `legends.items` & `legends.io`
* Defines item entities and handles data loading from `.txt` configuration files via the **Factory Pattern**.
* Includes `WeaponCategory` enum for smart weapon classification.

---

## 🛠 Design Patterns Implementation

* **MVC Pattern**: Separates Data (`characters`, `world`), Logic (`GameController`), and View (`UI`).
* **Single Responsibility Principle**: `BossBattle` class was created specifically to handle complex boss scripts, keeping `Battle.java` clean.
* **Data-Driven Design**: Boss weaknesses (like `allowed_damage`) are loaded from configuration files, not just hard-coded.
* **Factory Pattern**: `GameData` loads assets from text files (`Weaponry.txt`, `Dragons.txt`, etc.) and instantiates objects.
* **Template Method**: The `Hero` class defines the leveling structure (`levelUp`), which subclasses implement differently.

---

## 🚀 How to Run

1.  **Compile**:
    ```bash
    cd src
    javac legends/app/Main.java
    ```
2.  **Run**:
    ```bash
    java legends.app.Main
    ```
3.  **Note**: Ensure the `legends/data` folder (containing .txt files) is correctly located in the source path.
