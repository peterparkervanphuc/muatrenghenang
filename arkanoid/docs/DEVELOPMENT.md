# 🎮 ARKANOID - TÀI LIỆU PHÁT TRIỂN

**Version:** 2.1  
**Cập nhật:** 6 tháng 11, 2025  
**Project:** Java OOP Final Project

---

## 📋 MỤC LỤC

1. [Cấu trúc project](#-cấu-trúc-project)
2. [Kiến trúc OOP](#-kiến-trúc-oop)
3. [Cơ chế game](#-cơ-chế-game)
4. [Hệ thống tính năng](#-hệ-thống-tính-năng)
5. [Build & Run](#-build--run)
6. [Testing](#-testing)
7. [Troubleshooting](#-troubleshooting)

---

## 📂 CẤU TRÚC PROJECT

```
arkanoid/
├── src/
│   ├── main/              # Entry point
│   │   └── ArkanoidGame.java
│   ├── core/              # Game logic core
│   │   ├── GameManager.java
│   │   ├── LevelManager.java (18 levels)
│   │   └── GameBounds.java
│   ├── entities/          # Game objects
│   │   ├── GameObject.java (Abstract)
│   │   ├── MovableObject.java (Abstract)
│   │   ├── Ball.java
│   │   ├── Paddle.java
│   │   ├── Brick.java (14 types)
│   │   ├── Powerup.java (7 types)
│   │   └── LaserBeam.java
│   ├── factories/         # Factory Pattern
│   │   ├── BrickFactory.java
│   │   └── PowerUpFactory.java
│   ├── managers/          # Singleton managers
│   │   ├── ConfigManager.java
│   │   ├── SoundManager.java
│   │   ├── FontManager.java
│   │   ├── HighScoreManager.java
│   │   └── SaveGameManager.java (3 slots)
│   ├── ui/                # User Interface
│   │   ├── GamePanel.java (Main game + Combo system)
│   │   ├── MenuPanel.java
│   │   └── HighScorePanel.java
│   ├── effects/           # Visual effects
│   │   └── CameraShake.java
│   ├── utils/             # Utilities
│   │   ├── GameLogger.java
│   │   └── PerformanceMonitor.java
│   └── test/              # JUnit 5 tests
│       ├── core/
│       ├── entities/
│       ├── factories/
│       └── managers/
├── assets/
│   ├── Backgrounds/       # 18 stage backgrounds
│   ├── Sprites/           # Ball, bricks, powerups, paddle
│   ├── Sounds/            # 14 sound effects
│   └── Fonts/             # emulogic.ttf
├── docs/
│   ├── CLASS_DIAGRAM.puml
│   ├── DEVELOPMENT.md (this file)
│   ├── SAVE_GAME.md
│   ├── COMBO_SYSTEM.md
│   ├── SPEED_SYSTEM_ANALYSIS.md
│   ├── GAME_BALANCE_ANALYSIS.md
│   └── PROJECT_EVALUATION.md
├── High Scores/           # Runtime data (gitignored)
├── Saves/                 # Save game files (gitignored)
├── bin/                   # Compiled classes (gitignored)
├── target/                # Maven output (gitignored)
├── config.properties
├── pom.xml
├── run.bat
└── maven-compile.bat
```

---

## 🏗️ KIẾN TRÚC OOP

### 1. Hệ Thống Kế Thừa

```
GameObject (Abstract)
    ↓
    ├── MovableObject (Abstract)
    │   ├── Ball
    │   ├── Paddle
    │   ├── Powerup
    │   └── LaserBeam
    └── Brick
```

**Chi tiết:**
- **GameObject:** Base class cho tất cả entities (x, y, width, height)
- **MovableObject:** Extends GameObject, thêm velocity và speed
- **Concrete classes:** Ball, Paddle, Brick, Powerup, LaserBeam

### 2. Design Patterns

#### Factory Pattern
```java
// BrickFactory
public static Brick createBrick(int id, int x, int y)
public static Brick createRandomBrick(int x, int y)
public static Brick createSilverBrick(int x, int y)

// PowerUpFactory
public static Powerup createPowerUp(PowerupType type, double x, double y)
public static Powerup createRandomPowerUp(double x, double y)
public static Powerup createPowerUpFromBrick(double x, double y, double dropChance)
```

#### Singleton Pattern
```java
ConfigManager.getInstance()
SoundManager.getInstance()
SaveGameManager.getInstance()
```

#### Strategy Pattern (Enum-based)
```java
enum BrickType {
    WHITE, ORANGE, ..., SILVER, GOLD, MOVING_RF, MOVING_LF, ...
}

enum PowerupType {
    ENLARGE, LASER, CATCH, SLOW, DUPLICATE, BREAK, PLAYER
}
```

### 3. OOP Principles

**Encapsulation:**
```java
private int radius;                 // Private fields
private boolean attached;
public boolean isAttached() {       // Public getters
    return attached;
}
```

**Inheritance:**
```java
public class Ball extends MovableObject {
    // Inherit x, y, velocityX, velocityY, speed
    // Add ball-specific: radius, attached, speedMultiplier
}
```

**Polymorphism:**
```java
@Override
public void update() {
    // Custom update logic for each entity
}

@Override
public void render(Graphics2D g2d) {
    // Custom rendering for each entity
}
```

**Abstraction:**
```java
public abstract class GameObject {
    public abstract void update();
    public abstract void render(Graphics2D g2d);
}
```

---

## 🎯 CƠ CHẾ GAME

### 1. Ball Physics

**Speed System:**
```java
// Base speed
INITIAL_SPEED = 5.0 px/frame

// Level progression
levelSpeedBonus = Math.min((level - 1) * 0.07, 0.7)  // +7%/level, max 70%

// Speed multiplier (slow powerup)
speedMultiplier = 1.0  // normal
speedMultiplier = 0.5  // slowed

// Total speed
totalSpeed = INITIAL_SPEED * (1.0 + levelSpeedBonus) * speedMultiplier
totalSpeed = Math.min(totalSpeed, MAX_SPEED)  // Cap at 10.0
```

**Collision Detection:**
```java
// Circle-Rectangle intersection
double closestX = Math.max(rect.x, Math.min(ball.x, rect.x + rect.width));
double closestY = Math.max(rect.y, Math.min(ball.y, rect.y + rect.height));
double distance = sqrt((ball.x - closestX)^2 + (ball.y - closestY)^2);
return distance < ball.radius;
```

**Bounce Logic:**
- Paddle bounce: Angle based on hit position (-30° to +30°)
- Brick bounce: Reflect based on collision side (left/right/top/bottom)
- Minimum vertical speed: 3 px/frame (prevent horizontal trap)

### 2. Brick System (14 Types)

**Regular Bricks (8 colors):**
```
WHITE(1):      50 pts,  1 hit
ORANGE(2):     60 pts,  1 hit
LIGHT_BLUE(3): 70 pts,  1 hit
GREEN(4):      80 pts,  1 hit
RED(5):        90 pts,  1 hit
BLUE(6):       100 pts, 1 hit
PURPLE(7):     110 pts, 1 hit
YELLOW(8):     120 pts, 1 hit
```

**Special Bricks:**
```
SILVER(9):              150 pts, 3 hits, breakable
GOLD(10):               0 pts,   ∞ hits, unbreakable
MOVING_UNBREAKABLE_RF:  0 pts,   ∞ hits, speed 1.5 px/f →
MOVING_UNBREAKABLE_LF:  0 pts,   ∞ hits, speed 1.5 px/f ←
MOVING_RF:              100 pts, 1 hit,  speed 1.5 px/f →
MOVING_LF:              100 pts, 1 hit,  speed 1.5 px/f ←
```

### 3. Powerup System (7 Types)

**Drop System:**
```java
dropChance = 0.45  // 45% when brick destroyed

// Distribution (weighted random)
Common (55%):   ENLARGE 17%, CATCH 18%, LASER 20%
Uncommon (30%): SLOW 15%, DUPLICATE 15%
Rare (15%):     PLAYER 8%, BREAK 7%
```

**Powerup Effects:**

| Type | Effect | Duration |
|------|--------|----------|
| ENLARGE | Paddle width: 80 → 120 | Until hit |
| LASER | Fire lasers (0.3s cooldown) | 20 seconds |
| CATCH | Ball sticks to paddle | Permanent |
| SLOW | Ball speed × 0.5 | 10 seconds |
| DUPLICATE | +2 balls (max 10) | Instant |
| BREAK | Destroy bottom row | Instant |
| PLAYER | +1 life | Instant |

### 4. Combo System (NEW!)

**Mechanics:**
```java
// Combo window: 1 second
if (currentTime - lastHitTime < 1000) {
    comboCounter++;
} else {
    comboCounter = 0;
    comboMultiplier = 1;
}

// Multiplier tiers
5+ hits  → x2 "COMBO!"
10+ hits → x3 "GREAT!"
15+ hits → x4 "AWESOME!"
20+ hits → x5 "AMAZING!"

// Score calculation
score += brick.getPoints() * comboMultiplier;
```

**Visual Feedback:**
- Center screen text (fades out)
- Camera shake on high combos
- Multiplier indicator (top right)

### 5. Save/Load System

**3 Save Slots:**
```
Saves/save_slot_1.dat
Saves/save_slot_2.dat
Saves/save_slot_3.dat
```

**Saved Data:**
- Game state (score, lives, level)
- Paddle state (x, y, enlarged, hasLaser, hasCatch)
- All balls (position, velocity, attached, multipliers)
- All bricks (position, type, hits remaining)
- All powerups (position, type)
- Powerup timers (slow, laser)

**Key Bindings:**
```
F5 - Quick Save (slot 1)
F9 - Quick Load (slot 1)
F6 - Save/Load Menu (choose slot 1-3)
```

---

## 🎨 HỆ THỐNG TÍNH NĂNG

### Level Design (18 Levels)

**Progression:**
```
Level 1-5:   Tutorial & Easy
Level 6-10:  Medium difficulty
Level 11-15: Hard with special bricks
Level 16-18: Expert challenges
```

**Special Levels:**
- Level 1: Moving bricks tutorial
- Level 11: Zigzag pattern (RED + SILVER)
- Level 12: Eye pattern
- Level 15: Puzzle pattern
- Level 18: Final boss level

### Sound System (14 Sounds)

**Sound Effects:**
```
Break Powerup.wav       - Break powerup activation
Death.wav              - Ball lost
Enlarge Powerup.wav    - Enlarge powerup
Game Over.wav          - Game over
Game Start.wav         - Game start
High Scores.wav        - High scores music
Laser Beam Hit.wav     - Laser hits brick
Laser Beam.wav         - Laser fired
Menu.wav               - Menu music
Player Powerup.wav     - Extra life
Ship Hit.wav           - Paddle hit
Silver Wall Hit.wav    - Silver brick hit
Wall Hit.wav           - Normal brick hit
Win.wav                - Victory!
```

### Camera Shake

**Intensity Levels:**
```java
ball.bounceOffPaddle(): shake(3, 10)   - Light
silver brick hit:       shake(4, 8)    - Medium
death:                  shake(8, 20)   - Strong
combo x3:               shake(4, 10)   - Medium
combo x5:               shake(6, 15)   - Very strong
```

---

## 🔧 BUILD & RUN

### Option 1: Batch Script (Recommended)

```bash
cd arkanoid
run.bat
```

### Option 2: Maven

```bash
cd arkanoid
mvn clean compile
mvn exec:java -Dexec.mainClass="main.ArkanoidGame"
```

### Option 3: IntelliJ IDEA

1. Open `arkanoid/` folder
2. Mark directories:
   - `src/` → Sources Root
   - `assets/` → Resources Root
3. Run `main.ArkanoidGame.java`

### Option 4: Manual Compile

```bash
cd arkanoid

# Compile
javac -d bin -sourcepath src -encoding UTF-8 src/main/ArkanoidGame.java

# Run
java -cp "bin;assets" main.ArkanoidGame
```

---

## 🧪 TESTING

### Run Tests

**Maven:**
```bash
mvn test
```

**IntelliJ:**
Right-click on `test/` → Run 'All Tests'

### Test Coverage

**Current Coverage:**
- Entities: ~80%
- Managers: ~70%
- Factories: ~90%
- Core: ~75%

**Test Files:**
```
test/
├── core/
│   └── GameManagerTest.java
├── entities/
│   ├── BallTest.java
│   ├── BrickTest.java
│   ├── PaddleTest.java
│   ├── PowerUpTest.java
│   └── LaserBeamTest.java
├── factories/
│   ├── BrickFactoryTest.java
│   └── PowerUpFactoryTest.java
└── managers/
    ├── ConfigManagerTest.java
    ├── FontManagerTest.java
    ├── HighScoreManagerTest.java
    └── SoundManagerTest.java
```

---

## 🐛 TROUBLESHOOTING

### Compilation Errors

**Problem:** `javac: command not found`  
**Solution:** Install JDK 17+ and add to PATH

**Problem:** `package does not exist`  
**Solution:** Ensure `-sourcepath src` flag is used

**Problem:** `cannot find symbol`  
**Solution:** Check all source files are present in `src/`

### Runtime Errors

**Problem:** Assets not found  
**Solution:** Ensure assets are in classpath: `java -cp "bin;assets" ...`

**Problem:** Config not loading  
**Solution:** Copy `config.properties` to `bin/` directory

**Problem:** Sound not playing  
**Solution:** Check `sound.enabled=true` in config.properties

### Game Issues

**Problem:** Ball too fast on high levels  
**Solution:** Speed is capped at 10.0 px/f (configurable in config.properties)

**Problem:** Save/Load not working  
**Solution:** Ensure `Saves/` directory exists (created automatically)

**Problem:** Combo not working  
**Solution:** Must hit bricks within 1 second window

---

## 📚 DOCUMENTATION FILES

| File | Purpose |
|------|---------|
| `CLASS_DIAGRAM.puml` | Complete UML class diagram |
| `DEVELOPMENT.md` | This file - Development guide |
| `SAVE_GAME.md` | Save/Load system documentation |
| `COMBO_SYSTEM.md` | Combo feature documentation |
| `SPEED_SYSTEM_ANALYSIS.md` | Speed balancing analysis |
| `GAME_BALANCE_ANALYSIS.md` | Gameplay balance report |
| `PROJECT_EVALUATION.md` | Final evaluation (10.9/11) |

---

## 📧 SUPPORT

For issues or questions:
1. Check documentation in `docs/`
2. Review `arkanoid.log` for errors
3. Check git commit history
4. Contact team members

---

**Last Updated:** November 6, 2025  
**Version:** 2.1  
**Status:** Production Ready ✅

