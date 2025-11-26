# CS611-Assignment 2
## Legends: Heroes and Monsters

---------------------------------------------------------------------------
- **Name**: [Jingfeng Li]
- **Email**: [ljf628@bu.edu]
- **Student ID**: [U73840242]
---------------------------------------------------------------------------

## ✨ Key Features

### Boss Battle
The game features unique mechanics for floor bosses, handled by a specialized `BossBattle` logic engine:

* **Floor 1 - Skeleton King (Leoric)**:
    * **Bone Armor**: Highly resistant to **Sword/Blade** category weapons (only takes 10% damage).
    * **Weakness**: Vulnerable to **Blunt** weapons like Hammers/Maces (takes 150% damage).
* **Floor 2 - The Dragon King**:
    * **Aerial Defense**: **Immune** to all melee physical attacks (Swords, Axes, etc.).
    * **Ranged Vulnerability**: Can only be damaged physically by weapons in the **Bow** category.
    * **Magic Vulnerability**: Takes full damage from all **Spells**.
    * **Regeneration**: Heals **1% Max HP** at the start of every round.
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
<img width="331" height="1097" alt="image" src="https://github.com/user-attachments/assets/09c309a9-8919-4468-bd13-99c04be63839" />

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
---------------------------------------------------------------------------

```text

== Legends: Heroes and Monsters ==

1) Start New Game
2) Continue Exploration (no active game)
3) Exit
Enter choice: 1
Loading game data...
Choose your heroes (up to 3).
1) Warrior
2) Paladin
3) Sorcerer
q) Done
1
1) Gaerdal_Ironhand
2) Sehanine_Monnbow
3) Muamman_Duathall
4) Flandal_Steelskin
5) Undefeated_Yoj
6) Eunoia_Cyn
Choose hero number:
1
1) Warrior
2) Paladin
3) Sorcerer
q) Done
q

== Choose Region Size ==

1) Small  (5 x 5)
2) Medium (8 x 8)
3) Large  (12 x 12)
Enter 1, 2, or 3: 1


== Floor 1 ==

+---+---+---+---+---+
| I |   |   |   |   |
+---+---+---+---+---+
| X | X |   |   |   |
+---+---+---+---+---+
| M |   | M | M | M |
+---+---+---+---+---+
| M | X |   |   | X |
+---+---+---+---+---+
|   |   | M |   | M |
+---+---+---+---+---+
Select an option:
Move up:    w
Move down:  s
Move left:  a
Move right: d
Interact:   e
Check inventory / stats: i
Fight floor boss:        b
Quit to main menu:       q
i

== Party Status ==

Gaerdal_Ironhand, Level 1
Health: [████████████████████] 150/150
Mana:   [████████████████████] 800/800
EXP:    [███████---] 7/10
STR: 500  DEX: 600  AGI: 500  Gold: 1354
------------------------------


== Floor 1 ==

+---+---+---+---+---+
| I |   |   |   |   |
+---+---+---+---+---+
| X | X |   |   |   |
+---+---+---+---+---+
| M |   | M | M | M |
+---+---+---+---+---+
| M | X |   |   | X |
+---+---+---+---+---+
|   |   | M |   | M |
+---+---+---+---+---+
Select an option:
Move up:    w
Move down:  s
Move left:  a
Move right: d
Interact:   e
Check inventory / stats: i
Fight floor boss:        b
Quit to main menu:       q
b

=== Boss Battle ===
Skeleton King Leoric (Boss) L8 HP=6000 dmg=100 def=500 dodge=30%
Prepare yourself...


== Combat Begins! ==

Enemies:
Leoric, Level 8
Health: [████████████████████] 6000/800
------------------------------
Damage:  100
Defense: 500
Dodge:   30

--- Round 1 ---
== Monsters ==
Skeleton King Leoric (Boss) L8 HP=6000 dmg=100 def=500 dodge=30%
Gaerdal_Ironhand takes their turn!
1) Attack  2) Potion  3) Spell  4) Stats  q) Quit

