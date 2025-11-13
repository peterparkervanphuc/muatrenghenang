# 🎮 ARKANOID GAME - JAVA OOP PROJECT

[![Java](https://img.shields.io/badge/Java-17+-orange)](https://www.oracle.com/java/)
[![Build](https://img.shields.io/badge/Build-Maven-blue)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

Game phá gạch cổ điển được xây dựng bằng Java Swing với kiến trúc OOP chuyên nghiệp.


---

## 👥 TEAM

| Developer | GitHub |
|-----------|--------|
| **Duy** | [@peterparkervanphuc](https://github.com/peterparkervanphuc) |
| **An** | [@SZKMZT](https://github.com/SZKMZT) |
| **Bình** | [@TBinh24022617](https://github.com/TBinh24022617) |
| **Giang** | [@Lilwrx201206](https://github.com/Lilwrx201206) |

---

## 📋 MỤC LỤC

- [Team](#-team)
- [Tính năng](#-tính-năng)
- [Cách chạy game](#-cách-chạy-game)
- [Điều khiển](#-điều-khiển)
- [Kiến trúc OOP](#-kiến-trúc-oop)
- [Cấu trúc project](#-cấu-trúc-project)
- [Công nghệ](#-công-nghệ)

---

## ✨ TÍNH NĂNG

### 🎮 Gameplay Core
- ⭐ **18 levels** với độ khó tăng dần
- 🎯 **Combo System** - Multiplier x2, x3, x4, x5
- ⚡ **Dynamic Speed** - Tăng 7%/level (capped 70%)
- 🧱 **14 loại gạch**:
  - 8 màu thường (WHITE, ORANGE, LIGHT_BLUE, GREEN, RED, BLUE, PURPLE, YELLOW)
  - SILVER (3 hits)
  - GOLD (Unbreakable)
  - 4 loại MOVING (di chuyển trái/phải, vỡ được/bất tử)
- ❤️ **5 mạng** ban đầu
- 🎬 **60 FPS** silky smooth

### 🎁 Power-ups (7 loại)
| Icon | Name | Effect | Duration |
|------|------|--------|----------|
| E | ENLARGE | Phóng to paddle | Cho đến khi bị đánh |
| L | LASER | Bắn laser phá gạch | 20 giây |
| C | CATCH | Bắt bóng vào paddle | Permanent |
| S | SLOW | Làm chậm bóng 50% | 10 giây |
| D | DUPLICATE | Nhân đôi bóng | Instant |
| B | BREAK | Phá hàng dưới cùng | Instant |
| P | PLAYER | +1 mạng | Instant |

### 🎯 Combo System (MỚI!)
- 5+ hits trong 1 giây → **x2 multiplier** "COMBO!"
- 10+ hits → **x3 multiplier** "GREAT!"
- 15+ hits → **x4 multiplier** "AWESOME!"
- 20+ hits → **x5 multiplier** "AMAZING!"

### 💾 Lưu/Tải Game
- ✅ **3 save slots** độc lập
- ✅ **Quick Save:** F5 (slot 1)
- ✅ **Quick Load:** F9 (slot 1)
- ✅ **Save Menu:** F6 (chọn slot 1-3)
- ✅ Lưu toàn bộ trạng thái: level, score, lives, bricks, balls, powerups

### 🏆 Bảng Xếp Hạng
- Top 10 điểm cao nhất
- Tự động save vào file
- Hiển thị khi Game Over hoặc Victory

### 🎵 Âm thanh & Hình ảnh
- 14 sound effects
- 18 stage backgrounds
- Custom retro font (emulogic.ttf)
- Sprites cho tất cả entities

---

## 🚀 CÁCH CHẠY GAME

### Phương án 1: Sử dụng file BAT (Khuyến nghị)

```bash
cd arkanoid
run.bat
```

### Phương án 2: Maven

```bash
cd arkanoid
mvn clean compile
mvn exec:java -Dexec.mainClass="main.ArkanoidGame"
```

### Phương án 3: IntelliJ IDEA

1. Mở project `arkanoid/`
2. Đánh dấu thư mục:
   - `src/` → **Sources Root**
   - `assets/` → **Resources Root**
3. Run `main.ArkanoidGame.java`

### Phương án 4: Command Line

```bash
cd arkanoid
javac -d bin -sourcepath src src/main/ArkanoidGame.java
java -cp "bin;assets" main.ArkanoidGame
```

---

## 🎮 ĐIỀU KHIỂN

### Menu
- **↑↓** hoặc **Arrow Keys** - Di chuyển lựa chọn
- **Enter** - Xác nhận
- **ESC** - Quay lại

### Trong Game
- **← →** hoặc **A D** - Di chuyển paddle
- **Space** - Phóng bóng / Bắn laser (khi có)
- **ESC** - Pause/Resume
- **F5** - Quick Save (slot 1)
- **F6** - Save/Load menu (3 slots)
- **F9** - Quick Load (slot 1)

---

## 🏗️ KIẾN TRÚC OOP

### 4 Tính Chất OOP

#### 1️⃣ Encapsulation (Đóng gói)
```java
public class Ball extends MovableObject {
    private int radius;                    // Private fields
    private boolean attached;
    private double speedMultiplier;
    
    public boolean isAttached() {          // Public getters
        return attached;
    }
}
```

#### 2️⃣ Inheritance (Kế thừa)
```
GameObject (Abstract)
├── MovableObject (Abstract)
│   ├── Ball
│   ├── Paddle
│   ├── LaserBeam
│   └── Powerup
└── Brick
```

#### 3️⃣ Polymorphism (Đa hình)
```java
// Method overriding
@Override
public void update() { /* Custom logic */ }

@Override
public void render(Graphics2D g2d) { /* Custom rendering */ }

// Enum polymorphism
PowerupType.ENLARGE, LASER, CATCH...
```

#### 4️⃣ Abstraction (Trừu tượng)
```java
public abstract class GameObject {
    public abstract void update();
    public abstract void render(Graphics2D g2d);
}
```

### Design Patterns

- **Factory Pattern:** `BrickFactory`, `PowerUpFactory`
- **Singleton Pattern:** `SoundManager`, `ConfigManager`, `SaveGameManager`
- **Strategy Pattern:** Enum-based behavior (`PowerupType`, `BrickType`)

---

## 📁 CẤU TRÚC PROJECT

```
arkanoid/
├── src/
│   ├── main/              # Entry point
│   │   └── ArkanoidGame.java
│   ├── core/              # Game logic
│   │   ├── GameManager.java
│   │   ├── LevelManager.java
│   │   └── GameBounds.java
│   ├── entities/          # Game objects
│   │   ├── GameObject.java (Abstract)
│   │   ├── MovableObject.java (Abstract)
│   │   ├── Ball.java
│   │   ├── Paddle.java
│   │   ├── Brick.java
│   │   ├── Powerup.java
│   │   └── LaserBeam.java
│   ├── managers/          # Singletons
│   │   ├── ConfigManager.java
│   │   ├── SoundManager.java
│   │   ├── HighScoreManager.java
│   │   ├── SaveGameManager.java
│   │   └── FontManager.java
│   ├── factories/         # Factory Pattern
│   │   ├── BrickFactory.java
│   │   └── PowerUpFactory.java
│   ├── ui/                # GUI
│   │   ├── GamePanel.java
│   │   ├── MenuPanel.java
│   │   └── HighScorePanel.java
│   ├── effects/           # Visual effects
│   │   └── CameraShake.java
│   ├── utils/             # Utilities
│   │   ├── GameLogger.java
│   │   └── PerformanceMonitor.java
│   └── test/              # JUnit tests
│       ├── core/
│       ├── entities/
│       ├── factories/
│       └── managers/
├── assets/
│   ├── Backgrounds/       # 18 stage backgrounds
│   ├── Sounds/            # 14 sound effects
│   ├── Sprites/           # Ball, bricks, powerups, paddle
│   └── Fonts/             # emulogic.ttf
├── docs/
│   ├── CLASS_DIAGRAM.puml
│   ├── DEVELOPMENT.md
│   ├── SAVE_GAME.md
│   ├── COMBO_SYSTEM.md
│   ├── SPEED_SYSTEM_ANALYSIS.md
│   ├── GAME_BALANCE_ANALYSIS.md
│   └── PROJECT_EVALUATION.md
├── High Scores/           # Runtime (gitignored)
├── Saves/                 # Runtime (gitignored)
├── config.properties
├── pom.xml
├── run.bat
└── maven-compile.bat
```

---

## 🛠️ CÔNG NGHỆ

- **Language:** Java 17+
- **GUI:** Swing (JFrame, JPanel, Graphics2D)
- **Build Tool:** Maven
- **Testing:** JUnit 5
- **Version Control:** Git
- **IDE:** IntelliJ IDEA / Eclipse / VS Code

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
    </dependency>
</dependencies>
```

---


## 🙏 CREDITS

- **Original Game:** Taito Corporation (1986)
- **Font:** Emulogic by Freaky Fonts
- **Sprites & Sounds:** Custom created for educational purposes

---

**🎮 ENJOY THE GAME! 🚀**

Made with ❤️ using Java & OOP principles

