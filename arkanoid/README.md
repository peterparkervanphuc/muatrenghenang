# 🎮 ARKANOID GAME

Classic brick-breaker game được xây dựng bằng Java Swing với kiến trúc OOP chuyên nghiệp.

[![Java](https://img.shields.io/badge/Java-11+-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

> 📦 **Repository:** [github.com/peterparkervanphuc/muatrenghenang](https://github.com/peterparkervanphuc/muatrenghenang)

---

## 🚀 QUICK START

### Cách Chạy Game

#### ✅ IntelliJ IDEA (Khuyến nghị)
1. Mở project trong IntelliJ IDEA
2. Mark folders:
   - `src/` → **Sources Root**
   - `assets/` → **Resources Root**
3. Run `ArkanoidGame.main()` (Shift + F10)

#### ✅ Script File
```batch
run.bat          # Windows
```

#### ✅ Command Line
```bash
# Compile
javac -d bin -sourcepath src src/main/ArkanoidGame.java

# Run
java -cp bin;assets main.ArkanoidGame
```

---

## 🎮 CONTROLS

### Menu
- **↑↓** - Navigate
- **Enter** - Select
- **ESC** - Back to menu

### In-Game
- **← →** (A/D) - Move paddle
- **Space** - Launch ball / Fire laser
- **F5** - Save game (3 slots)
- **F6** - Delete save (with confirmation)
- **F9** - Load game
- **ESC** - Pause / Return to menu

---

## ✨ FEATURES

### Core Gameplay
- ✅ **5 Levels** với độ khó tăng dần
- ✅ **Ball speed** tăng 9% mỗi level
- ✅ **9 brick types** (8 màu + silver)
- ✅ **7 powerups** với effects đặc biệt
- ✅ **Lives system** (3 mạng)
- ✅ **High score** top 10
- ✅ **60 FPS** smooth gameplay

### Advanced Features
- 💾 **Save/Load System**
  - Auto-save mỗi 30 giây
  - Manual save: 3 slots
  - Lưu toàn bộ game state
- 🎵 **Sound System**
  - Menu music
  - Sound effects (hit, break, powerup, death)
- 📊 **Config System**
  - Tùy chỉnh qua `config.properties`
  - Lives, speed, FPS, etc.
- 📝 **Logging**
  - Game events log vào `arkanoid.log`

---

## 🎁 POWERUPS

| Icon | Powerup | Effect | Duration |
|------|---------|--------|----------|
| 🔵 | **ENLARGE** | Tăng kích thước paddle | Permanent |
| 🔴 | **LASER** | Bắn laser phá gạch | 15 giây |
| 🟢 | **CATCH** | Bắt bóng khi chạm paddle | Until launch |
| 🟡 | **SLOW** | Giảm tốc bóng 60% | 10 giây |
| 🟣 | **DUPLICATE** | Tạo thêm 2 bóng | Permanent |
| 🟤 | **BREAK** | Phá 1 hàng brick dưới | Instant |
| 🔷 | **PLAYER** | +1 mạng | Instant |

### Powerup Conflicts
- **LASER ↔ ENLARGE**: Exclusive (không thể có cả 2)
- **CATCH ↔ DUPLICATE**: Exclusive
- **Drop chance**: 45% khi phá gạch

---

## 🧱 BRICK TYPES

| Color | Hits | Points | Special |
|-------|------|--------|---------|
| White | 1 | 50 | - |
| Orange | 1 | 60 | - |
| Light Blue | 1 | 70 | - |
| Green | 1 | 80 | - |
| Red | 1 | 90 | - |
| Blue | 1 | 100 | - |
| Purple | 1 | 110 | - |
| Yellow | 1 | 120 | - |
| **Silver** | **3** | **50** | **Camera shake** |

---

## 📁 PROJECT STRUCTURE

```
arkanoid/
├── src/
│   ├── core/              # Game logic
│   ├── entities/          # Game objects
│   ├── factories/         # Factory patterns
│   ├── managers/          # System managers
│   ├── ui/                # User interface
│   ├── effects/           # Visual effects
│   ├── utils/             # Utilities
│   └── main/              # Entry point
├── assets/
│   ├── Backgrounds/       # 5 level backgrounds
│   ├── Sprites/           # Game images
│   ├── Sounds/            # Audio files
│   └── Fonts/             # Custom fonts
├── docs/
│   ├── DEVELOPMENT.md     # Technical docs
│   └── SAVE_GAME.md       # Save system guide
├── Saves/                 # Save game files
├── High Scores/           # Score data
└── config.properties      # Configuration
```

---

## 🏗️ TECHNICAL HIGHLIGHTS

### OOP Architecture
```
GameObject (abstract)
├── MovableObject (abstract)
│   ├── Paddle
│   ├── Ball
│   ├── Powerup
│   └── LaserBeam
└── Brick
```

### Design Patterns
- **Singleton**: Managers (Config, Sound, SaveGame, HighScore)
- **Factory**: BrickFactory, PowerUpFactory
- **MVC**: Separation of concerns
- **Observer**: Event handling

### Key Technologies
- **Java Swing**: GUI framework
- **Java 2D**: Graphics rendering
- **Serialization**: Save/load system
- **Properties**: Configuration management

---

## ⚙️ CONFIGURATION

Chỉnh sửa `config.properties`:

```properties
# Window
window.width=800
window.height=600
window.title=Arkanoid

# Gameplay
game.initial.lives=3
game.max.level=5
game.fps=60

# Physics
ball.initial.speed=6
paddle.speed=8

# Sound
sound.enabled=true
```

---

## 📚 DOCUMENTATION

- 📖 [DEVELOPMENT.md](docs/DEVELOPMENT.md) - Technical documentation
- 💾 [SAVE_GAME.md](docs/SAVE_GAME.md) - Save/Load system guide
- 🎯 [CLASS_DIAGRAM.puml](docs/CLASS_DIAGRAM.puml) - UML diagram

---

## 🎯 GAME TIPS

1. **Powerup Strategy**: 
   - LASER tốt cho clear bricks nhanh
   - CATCH giúp kiểm soát ball tốt hơn
   - SLOW cho người chơi mới

2. **Scoring**: 
   - Yellow bricks cho nhiều điểm nhất (120)
   - Silver bricks khó phá nhưng cho powerup

3. **Save System**:
   - Auto-save mỗi 30s vào Slot 1
   - Manual save (F5) cho strategies khác nhau

---

## 🐛 TROUBLESHOOTING

### Game không chạy?
1. Kiểm tra Java version: `java -version` (cần Java 11+)
2. Đảm bảo `src/` và `assets/` đã mark đúng trong IDE
3. Check `arkanoid.log` để xem lỗi

### Không có âm thanh?
1. Kiểm tra `config.properties`: `sound.enabled=true`
2. Đảm bảo WAV files trong `assets/Sounds/`

### Save game không hoạt động?
1. Kiểm tra thư mục `Saves/` được tạo
2. Check permissions ghi file
3. Xem `arkanoid.log` để debug

---

## 🤝 CONTRIBUTING

Mọi đóng góp đều được chào đón! 

### Cách contribute:
1. Fork repository
2. Tạo feature branch: `git checkout -b feature/AmazingFeature`
3. Commit changes: `git commit -m 'Add AmazingFeature'`
4. Push to branch: `git push origin feature/AmazingFeature`
5. Open Pull Request

---

## 📝 LICENSE

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👤 AUTHOR

**Peter Parker Van Phuc**

- GitHub: [@peterparkervanphuc](https://github.com/peterparkervanphuc)
- Repository: [muatrenghenang](https://github.com/peterparkervanphuc/muatrenghenang)

---

## 🙏 ACKNOWLEDGMENTS

- Original Arkanoid by TAITO (1986)
- Java Swing documentation
- IntelliJ IDEA

---

**⭐ Star this repo if you like it!**

│   └── entities.LaserBeam ✅
└── entities.Brick ✅
```

### OOP Principles Applied:
1. ✅ **Encapsulation** - Private fields, getters/setters
2. ✅ **Inheritance** - entities.GameObject hierarchy
3. ✅ **Polymorphism** - Method overriding (update, render)
4. ✅ **Abstraction** - Abstract classes & methods

### Design Patterns:
- ✅ **Singleton** - managers.SoundManager, managers.ConfigManager, GameLogger
- ✅ **Factory** - BrickFactory, PowerUpFactory, core.LevelManager
- ✅ **MVC-like** - ui.GamePanel (Controller), entities.GameObject (Model), render() (View)

---

## 🔧 Development Quick Reference

### Run in IntelliJ IDEA:
1. Open project
2. Mark directories:
   - `src/` → **Sources Root**
   - `assets/` → **Resources Root**
3. Run `main.ArkanoidGame.java`

### Build Commands (Command Line):
```bash
# Compile
javac -d bin -sourcepath src src\*.java

# Run
java -cp bin main.ArkanoidGame
```

### Configuration Access:

```java
import managers.ConfigManager;

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
