# 🎮 ARKANOID GAME

Classic brick-breaker game xây dựng bằng Java Swing với thiết kế OOP chuyên nghiệp.

[![GitHub](https://img.shields.io/badge/GitHub-muatrenghenang-blue)](https://github.com/peterparkervanphuc/muatrenghenang)
[![Java](https://img.shields.io/badge/Java-11+-orange)](https://www.oracle.com/java/)

> 📦 **Repository:** [peterparkervanphuc/muatrenghenang](https://github.com/peterparkervanphuc/muatrenghenang)  
> 📂 **Project Location:** `/arkanoid/`

---

## 🚀 Cách Chạy Game

### ✅ Phương Pháp 1: IntelliJ IDEA (Khuyến nghị cho Development)

1. Mở project trong IntelliJ IDEA
2. Đảm bảo folder đã mark đúng:
   - `src/` → **Mark as Sources Root**
   - `assets/` → **Mark as Resources Root**
3. Mở file `ArkanoidGame.java`
4. Click nút **Run** ▶️ (hoặc `Shift + F10`)

**Lợi ích:** 
- Tự động compile
- Debug dễ dàng
- Hot reload khi sửa code
- Full IDE features

### ✅ Phương Pháp 2: Script File (Cho Quick Testing / CI/CD / Distribution)

**Windows:**
```batch
run.bat
```
Hoặc double-click vào `run.bat` trong File Explorer

**Lợi ích:**
- Tự động compile và chạy
- Không cần IDE
- Phù hợp cho CI/CD automation
- Quick testing
- Distribute cho người không có IntelliJ

**Use Cases:**
- 🧪 Quick testing sau khi pull code
- 🤖 CI/CD automation (GitHub Actions, Jenkins)
- 📦 Distribution cho người dùng cuối
- ⚡ Chạy nhanh không cần mở IDE

### ✅ Phương Pháp 3: Command Line Manual (Advanced)
```bash
# Compile
cd src
javac *.java -d ../bin -encoding UTF-8

# Chạy
cd ..
java -cp bin ArkanoidGame
```

---

## 🎯 Game Controls (Điều khiển)

### Menu:
- **Enter/Space** - Bắt đầu game / Chọn
- **↑↓** - Di chuyển trong menu
- **H** - Xem high scores
- **ESC** - Quay lại menu

### In-Game:
- **← →** (Left/Right Arrow) - Di chuyển paddle
- **Space** - Launch ball khi attached
- **Space** - Fire laser (khi có powerup LASER)
- **P** - Pause game
- **ESC** - Quay về menu

---

## 🎁 Powerups (7 loại)

1. **ENLARGE** (Blue) - Tăng kích thước paddle
2. **LASER** (Red) - Bắn laser phá gạch
3. **CATCH** (Green) - Bắt bóng khi chạm paddle
4. **SLOW** (Yellow) - Làm chậm bóng 10 giây
5. **DUPLICATE** (Pink) - Tạo thêm bóng
6. **BREAK** (Brown) - Phá 1 hàng brick dưới cùng
7. **PLAYER** (Cyan) - Extra life (+1 mạng)

---

## 🧱 Brick Types (9 loại)

| Color | Hits | Points |
|-------|------|--------|
| WHITE | 1 | 50 |
| ORANGE | 1 | 60 |
| LIGHT_BLUE | 1 | 70 |
| GREEN | 1 | 80 |
| RED | 1 | 90 |
| BLUE | 1 | 100 |
| PURPLE | 1 | 110 |
| YELLOW | 1 | 120 |
| **SILVER** | **3** | **50** |

**⭐ Đặc biệt:** SILVER bricks cần đánh **3 lần** và có camera shake effect!

---

## ✨ Game Features

- ✅ **5 Levels** với độ khó tăng dần và background riêng
- ✅ **Ball speed** tăng 8% mỗi level
- ✅ **45% chance** powerup rơi khi phá gạch
- ✅ **High Score System** lưu top 10 scores
- ✅ **Lives system** (3 mạng mặc định)
- ✅ **Camera Shake** khi đập silver brick hoặc mất mạng
- ✅ **Sound effects** và menu music
- ✅ **Configuration system** qua config.properties
- ✅ **Logging system** với file log
- ✅ **60 FPS** smooth gameplay

---

## ⚙️ Configuration

Chỉnh sửa `config.properties` để customize game:

```properties
# Window settings
window.width=800
window.height=600

# Game settings
game.initial.lives=3
game.max.level=5
game.fps=60

# Ball & Paddle
ball.initial.speed=6
paddle.speed=8

# Sound
sound.enabled=true

# Debug
debug.mode=false
```

---

## 📁 Project Structure

```
arkanoid/
├── src/                   # Java source code (Sources Root)
│   ├── GameObject.java    # Abstract base class
│   ├── MovableObject.java # Abstract movable objects
│   ├── Paddle.java        # Player paddle
│   ├── Ball.java          # Game ball
│   ├── Brick.java         # Bricks
│   ├── Powerup.java       # Powerup items
│   ├── LaserBeam.java     # Laser projectiles
│   ├── GamePanel.java     # Main game engine
│   ├── GameManager.java   # State management
│   ├── LevelManager.java  # Level factory
│   ├── SoundManager.java  # Audio manager
│   └── ... (23 files)
├── assets/               # Game assets (Resources Root)
│   ├── Backgrounds/      # Level backgrounds
│   ├── Sounds/           # Audio files
│   ├── Sprites/          # Images
│   └── Fonts/            # Custom fonts
├── bin/                  # Compiled classes (auto-generated)
├── docs/                 # Documentation
│   ├── ARCHITECTURE.md   # Kiến trúc hệ thống
│   ├── IMPROVEMENTS.md   # Các cải tiến
│   └── DEVELOPMENT.md    # Hướng dẫn dev
├── High Scores/          # Score data
├── config.properties     # Configuration
├── run.bat              # Quick run script (Windows)
├── pom.xml              # Maven config (optional)
└── arkanoid.iml         # IntelliJ project file
```

---

## 🏗️ Kiến Trúc OOP

### UML Class Diagram:
📊 **Xem sơ đồ chi tiết:** [`docs/CLASS_DIAGRAM.puml`](docs/CLASS_DIAGRAM.puml)  
📖 **Hướng dẫn xem:** [`docs/UML_GUIDE.md`](docs/UML_GUIDE.md)

### Inheritance Hierarchy:
```
GameObject (abstract)
├── MovableObject (abstract)
│   ├── Paddle ✅
│   ├── Ball ✅
│   ├── Powerup ✅
│   └── LaserBeam ✅
└── Brick ✅
```

### OOP Principles Applied:
1. ✅ **Encapsulation** - Private fields, getters/setters
2. ✅ **Inheritance** - GameObject hierarchy
3. ✅ **Polymorphism** - Method overriding (update, render)
4. ✅ **Abstraction** - Abstract classes & methods

### Design Patterns:
- ✅ **Singleton** - SoundManager, ConfigManager, GameLogger
- ✅ **Factory** - BrickFactory, PowerUpFactory, LevelManager
- ✅ **MVC-like** - GamePanel (Controller), GameObject (Model), render() (View)

---

## 🔧 Development Quick Reference

### Run in IntelliJ IDEA:
1. Open project
2. Mark directories:
   - `src/` → **Sources Root**
   - `assets/` → **Resources Root**
3. Run `ArkanoidGame.java`

### Build Commands (Command Line):
```bash
# Compile
javac -d bin -sourcepath src src\*.java

# Run
java -cp bin ArkanoidGame
```

### Configuration Access:
```java
ConfigManager config = ConfigManager.getInstance();
int lives = config.getInt("game.initial.lives", 3);
boolean soundOn = config.getBoolean("sound.enabled", true);
```

### Logging:
```java
GameLogger.info("Game started");
GameLogger.warning("Asset missing");
GameLogger.error("Failed to load", exception);
GameLogger.debug("Debug info");
```

### View Logs:
```bash
type arkanoid.log
```

---

## 📚 Documentation

Xem thêm tài liệu chi tiết trong thư mục `docs/`:
- **ARCHITECTURE.md** - Kiến trúc và cấu trúc code chi tiết
- **IMPROVEMENTS.md** - Các cải tiến và design patterns
- **DEVELOPMENT.md** - Hướng dẫn phát triển và OOP refactoring

---

## ⚠️ Troubleshooting

### Problem: "Cannot find Java"
**Solution:** Cài đặt JDK 11 trở lên và thêm vào PATH

### Problem: "Images not loading"
**Solution:** Chạy từ thư mục gốc `arkanoid/`, không phải từ `src/`

### Problem: "Sound không chơi"
**Solution:** 
- Kiểm tra `sound.enabled=true` trong config.properties
- Kiểm tra file .wav trong folder `assets/Sounds/`

### Problem: "Compilation errors"
**Solution:** 
```bash
cd src
del ..\bin\*.class
javac *.java -d ../bin -encoding UTF-8
```

---

## 🎊 Credits & Info

**Original Arkanoid:** Taito Corporation  
**Implementation:** Java Swing với OOP principles  
**Development:** October 2025  
**Version:** 1.0.0  
**Rating:** ⭐⭐⭐⭐⭐ Professional Grade

---

**Chúc bạn chơi game vui vẻ! 🎮🎉**
