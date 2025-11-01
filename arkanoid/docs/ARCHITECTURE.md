# Kiến Trúc Project - Arkanoid Game

**Last Updated:** October 29, 2025

---

## 🎮 CẤU TRÚC TỔNG QUAN

### Project Structure:
```
arkanoid/
├── src/                    # Source code
├── bin/                    # Compiled classes
├── assets/                 # Game assets
├── docs/                   # Documentation
├── High Scores/           # Score data
├── config.properties      # Configuration
├── pom.xml               # Maven build
└── run.bat               # Run script
```

---

## 📂 SOURCE CODE STRUCTURE

### Core Game Files (5 files)

#### 1. **main.ArkanoidGame.java** (Main Entry Point)
**Chức năng:**
- Main class của game, extends JFrame
- Khởi tạo cửa sổ game (800x600)
- Quản lý CardLayout để chuyển đổi giữa các màn hình
- Chứa 3 panels: ui.MenuPanel, ui.GamePanel, ui.HighScorePanel

```java
import ui.GamePanel;
import ui.HighScorePanel;
import ui.MenuPanel;

public class main.ArkanoidGame extends

JFrame {
    private CardLayout cardLayout;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private HighScorePanel highScorePanel;
}
```

#### 2. **ui.GamePanel.java** (Gameplay Engine)
**Chức năng:**
- Panel chính chứa toàn bộ gameplay
- Implements game loop với Timer (60 FPS)
- Quản lý tất cả game objects (entities.Paddle, Balls, Bricks, Powerups)
- Xử lý input (keyboard: ←→ Space ESC)
- Collision detection và physics
- entities.Powerup logic (7 types)
- Level progression (5 levels)
- Lives system (3 lives)

**Methods quan trọng:**
```java
startNewGame()              // Bắt đầu game mới
initializeLevel()           // Load level mới
update()                    // Game loop - 60 FPS
paintComponent()            // Render graphics
checkBallBrickCollision()   // Collision detection
applyPowerup()             // Xử lý powerup effects
```

#### 3. **ui.MenuPanel.java** (Main Menu)
**Chức năng:**
- Panel menu chính với 4 options
- START GAME, HIGH SCORES, HELP, EXIT
- Keyboard navigation (↑↓ Enter)
- Load và hiển thị logo, background

#### 4. **ui.HighScorePanel.java** (High Scores Display)
**Chức năng:**
- Hiển thị bảng xếp hạng top 10
- Format: RANK | NAME | SCORE
- ESC để quay về menu

#### 5. **core.GameManager.java** (Game State Manager)
**Chức năng:**
- Quản lý trạng thái game (Score, Lives, Level)
- Methods: addScore(), loseLife(), addLife(), nextLevel()

---

## 🎯 GAME OBJECTS HIERARCHY

### Abstract Base Classes:

#### **entities.GameObject.java** (Abstract Base)
```java
public abstract class entities.GameObject {
    private double x, y;
    private int width, height;
    
    public abstract void update();
    public abstract void render(Graphics2D g2d);
    public Rectangle getBounds();
}
```

#### **entities.MovableObject.java** (Abstract Movable)

```java
import entities.GameObject;

public abstract class MovableObject extends GameObject {
    private double velocityX, velocityY, speed;

    @Override
    public void update() {
        x += velocityX;
        y += velocityY;
    }
}
```

### Concrete Game Objects:

#### **entities.Paddle.java** (extends entities.MovableObject)
- Player paddle điều khiển bởi ←→ keys
- Width: 80 (normal) hoặc 120 (enlarged)
- entities.Powerup states: hasLaser, hasCatch, enlarged
- Methods: moveLeft(), moveRight(), enlarge(), shrink(), fireLaser()

#### **entities.Ball.java** (extends entities.MovableObject)
- Bóng chính của game (radius: 8px)
- Attachment to paddle
- Launch với random angle
- Bounce physics (walls, paddle, bricks)
- Slow powerup support

#### **entities.Brick.java** (extends entities.GameObject)
- 9 types: WHITE, ORANGE, LIGHT_BLUE, GREEN, RED, BLUE, PURPLE, YELLOW, SILVER
- Points: 50-120 points
- SILVER brick cần 2 hits
- Methods: hit(), isDestroyed(), isSilver()

#### **entities.Powerup.java** (extends entities.MovableObject)
- 7 types: ENLARGE, LASER, CATCH, SLOW, DUPLICATE, BREAK, PLAYER
- Auto falling với velocity
- Size: 40x20

#### **entities.LaserBeam.java** (extends entities.MovableObject)
- Laser bắn từ paddle
- Auto upward movement (velocityY = -10)
- Size: 4x15

---

## 🏗️ INHERITANCE HIERARCHY

```
entities.GameObject (abstract)
├── entities.MovableObject (abstract)
│   ├── entities.Paddle ✅
│   ├── entities.Ball ✅
│   ├── entities.Powerup ✅
│   └── entities.LaserBeam ✅
└── entities.Brick ✅

JFrame
└── main.ArkanoidGame ✅

JPanel + KeyListener
├── ui.GamePanel ✅
├── ui.MenuPanel ✅
└── ui.HighScorePanel ✅
```

---

## 🔧 MANAGER CLASSES

### **core.LevelManager.java** (Factory Pattern)
- Static utility class
- Tạo brick layouts cho 5 levels
- Method: `loadLevel(int level)` → `ArrayList<entities.Brick>`

**Level Patterns:**
- Level 1: Simple rows (6x11)
- Level 2: Pyramid
- Level 3: Diamond with silver
- Level 4: Checkerboard
- Level 5: Complex spiral

### **managers.SoundManager.java** (Singleton Pattern)
- Load và play tất cả sound effects
- HashMap<String, Clip> để cache sounds
- Methods: playMenuMusic(), playWallHitSound(), etc.

### **managers.HighScoreManager.java** (Utility)
- File I/O cho high scores
- File: "High Scores/High Scores.txt"
- Format: NAME:SCORE
- Keep top 10 scores

### **managers.FontManager.java** (Utility)
- Load custom font "Emulogic.ttf"
- Fallback to "Monospaced"
- Methods: getGameFont(size), getGameFont(style, size)

### **managers.ConfigManager.java** (Singleton)
- Load config từ config.properties
- Centralized configuration
- Methods: getInt(), getBoolean(), getString()

### **GameLogger.java** (Utility)
- Java Util Logging wrapper
- Console + File logging
- Methods: info(), warning(), error(), debug()

---

## 🏭 FACTORY CLASSES

### **BrickFactory.java**

```java
import entities.Brick;

public static Brick createBrick(BrickType type, int x, int y)

public static Brick createBrick(String typeName, int x, int y)

public static Brick createRandomBrick(int x, int y)

public static Brick createSilverBrick(int x, int y)
```

### **PowerUpFactory.java**

```java
import entities.Powerup;

public static Powerup createPowerUp(PowerupType type, int x, int y)

public static Powerup createRandomPowerUp(int x, int y)

public static Powerup createPowerUpFromBrick(int x, int y, double dropChance)

public static Powerup createBonusPowerUp(int x, int y)
```

---

## 🛠️ UTILITY CLASSES

### **core.GameBounds.java** (Constants)
```java
public static final int LEFT_BORDER = 26;
public static final int RIGHT_BORDER = 40;
public static final int TOP_BORDER = 20;
public static final int BOTTOM_BORDER = 0;
// ... play area calculations
```

### **effects.CameraShake.java**
- Screen shake effect
- Methods: shake(intensity, duration), update(), getOffsetX(), getOffsetY()

### **PerformanceMonitor.java**
- FPS tracking
- Performance metrics
- Debug information

---

## 🔄 EXECUTION FLOW

### 1. Game Start:
```
main() → main.ArkanoidGame()
→ Create Panels (Menu, Game, HighScore)
→ Show ui.MenuPanel
→ Play menu music
```

### 2. Start Game:
```
ui.MenuPanel: Enter on "START GAME"
→ main.ArkanoidGame.startGame()
→ ui.GamePanel.startNewGame()
→ initializeLevel()
→ core.LevelManager.loadLevel(1)
→ Create entities.Paddle, entities.Ball, Bricks
→ Start Timer (60 FPS)
```

### 3. Game Loop (60 FPS):
```
Timer tick every 16ms
→ ui.GamePanel.update()
  → Update paddle (keyboard)
  → Update balls (physics)
  → Check collisions
  → Update powerups
  → Update lasers
→ ui.GamePanel.paintComponent()
  → Draw all objects
  → Draw UI
```

### 4. entities.Ball Lost:
```
entities.Ball.y > PLAY_BOTTOM
→ core.GameManager.loseLife()
→ Reset powerups
→ Respawn ball (attached)
→ If lives == 0: Game Over
```

### 5. Level Complete:
```
All bricks destroyed
→ core.GameManager.nextLevel()
→ Wait 2s
→ initializeLevel()
→ Continue
```

---

## 🎨 ASSETS STRUCTURE

```
assets/
├── Backgrounds/
│   ├── Stage 1.png
│   ├── Stage 2.png
│   └── ... (5 levels)
├── Fonts/
│   └── emulogic.ttf
├── Sounds/
│   ├── Menu.wav
│   ├── Game Start.wav
│   ├── Wall Hit.wav
│   └── ... (15+ sounds)
└── Sprites/
    ├── ball.png
    ├── Menu/
    ├── Powerups/
    ├── Spacecraft/
    └── Walls/
```

---

## 💡 OOP PRINCIPLES APPLIED

### 1. Encapsulation ✅
- All fields private
- Constants private static final
- Access through getters/setters

### 2. Inheritance ✅
- entities.GameObject → entities.MovableObject → Game entities
- JFrame/JPanel for UI
- Code reuse and hierarchy

### 3. Polymorphism ✅
- Method overriding: update(), render()
- Enum types: BrickType, PowerupType
- Polymorphic rendering

### 4. Abstraction ✅
- Abstract classes: entities.GameObject, entities.MovableObject
- Abstract methods force implementation
- Hide complex logic

---

## 🎯 DESIGN PATTERNS

### Singleton:
- managers.SoundManager
- managers.ConfigManager
- GameLogger (static methods)

### Factory:
- BrickFactory
- PowerUpFactory
- core.LevelManager

### MVC-like:
- ui.GamePanel = Controller
- entities.GameObject = Model
- render() = View

---

## 📊 FILE STATISTICS

| Category | Files | Lines |
|----------|-------|-------|
| Main Game | 5 | ~1000 |
| Game Objects | 7 | ~1100 |
| Managers | 7 | ~1200 |
| Factories | 2 | ~300 |
| Utilities | 3 | ~200 |
| **TOTAL** | **24** | **~3800** |

---

## 🎊 KEY FEATURES

### Gameplay:
- 5 Progressive levels
- 7 Types of powerups
- 9 entities.Brick types
- Physics-based ball movement
- Laser shooting
- Camera shake effects

### Technical:
- 60 FPS game loop
- Collision detection
- Sound system
- High score persistence
- Configuration system
- Logging system
- Error handling

### Architecture:
- Clean OOP design
- Design patterns
- Modular structure
- Extensible code
- Professional quality

---

**Architecture Rating: ⭐⭐⭐⭐⭐ Professional Grade!**

*Generated: October 29, 2025*

