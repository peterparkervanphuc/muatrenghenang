# 🎮 ARKANOID GAME

Game phá gạch cổ điển được xây dựng bằng Java Swing với kiến trúc OOP chuyên nghiệp.

[![Java](https://img.shields.io/badge/Java-25-orange)](https://www.oracle.com/java/)

---

## 🚀 CÁCH CHẠY GAME

### Sử dụng file batch (Khuyến nghị)
```bash
run.bat
```

### Sử dụng IntelliJ IDEA
1. Mở project trong IntelliJ IDEA
2. Đánh dấu thư mục:
   - `src/` → **Sources Root**
   - `assets/` → **Resources Root**
3. Chạy file `main.ArkanoidGame.java`

---

## 🎮 ĐIỀU KHIỂN

### Menu
- **↑↓** - Di chuyển lựa chọn
- **Enter** - Xác nhận
- **ESC** - Quay lại menu

### Trong game
- **← →** hoặc **A/D** - Di chuyển thanh đỡ
- **Space** - Phóng bóng / Bắn laser
- **F5** - Lưu game (3 slot)
- **F6** - Xóa file lưu
- **F9** - Load game
- **ESC** - Tạm dừng / Quay lại menu

---

## ✨ TÍNH NĂNG

### Gameplay cơ bản
- 🎯 **5 màn chơi** với độ khó tăng dần
- ⚡ **Tốc độ bóng tăng 9%** mỗi màn
- 🧱 **9 loại gạch**: 8 màu thường + gạch bạc (3 lần đánh)
- 🎁 **7 power-up**: ENLARGE, LASER, CATCH, SLOW, DUPLICATE, BREAK, PLAYER
- ❤️ **Hệ thống mạng**: Bắt đầu với 3 mạng
- 🏆 **Bảng xếp hạng**: Top 10 điểm cao nhất
- 🎬 **60 FPS** mượt mà

### Tính năng nâng cao
- 💾 **Hệ thống lưu/tải**: 3 slot lưu thủ công (F5 lưu, F9 tải)
- 🔊 **Âm thanh**: Nhạc nền menu và hiệu ứng âm thanh
- ⚙️ **Tùy chỉnh**: Cấu hình qua file `config.properties`
- 📝 **Ghi log**: Các sự kiện được ghi vào `arkanoid.log`

---

## 🎁 POWER-UPS

| Power-up | Hiệu ứng | Thời gian |
|----------|----------|-----------|
| 🔵 **ENLARGE** | Tăng kích thước thanh đỡ | Vĩnh viễn |
| 🔴 **LASER** | Bắn laser phá gạch | 15 giây |
| 🟢 **CATCH** | Bắt bóng khi chạm thanh đỡ | Đến khi phóng |
| 🟡 **SLOW** | Giảm tốc độ bóng 60% | 10 giây |
| 🟣 **DUPLICATE** | Tạo thêm 2 quả bóng | Vĩnh viễn |
| 🟤 **BREAK** | Phá 1 hàng gạch dưới cùng | Ngay lập tức |
| 🔷 **PLAYER** | +1 mạng | Ngay lập tức |

### Quy tắc Power-up
- **LASER ↔ ENLARGE**: Không thể có cả 2 cùng lúc
- **CATCH ↔ DUPLICATE**: Không thể có cả 2 cùng lúc
- **Tỉ lệ rơi**: 45% khi phá gạch

---

## 🧱 LOẠI GẠCH

| Màu sắc | Số lần đánh | Điểm | Đặc biệt |
|---------|-------------|------|----------|
| Trắng | 1 | 50 | - |
| Cam | 1 | 60 | - |
| Xanh nhạt | 1 | 70 | - |
| Xanh lá | 1 | 80 | - |
| Đỏ | 1 | 90 | - |
| Xanh dương | 1 | 100 | - |
| Tím | 1 | 110 | - |
| Vàng | 1 | 120 | - |
| **Bạc** | **3** | **50** | **Rung camera** |

---

## 📁 CẤU TRÚC PROJECT

```
arkanoid/
├── src/
│   ├── core/              # Logic game (GameManager, LevelManager)
│   ├── entities/          # Đối tượng game (Ball, Brick, Paddle, Powerup)
│   ├── factories/         # Factory pattern (BrickFactory, PowerUpFactory)
│   ├── managers/          # Quản lý hệ thống (Sound, HighScore, SaveGame)
│   ├── ui/                # Giao diện (GamePanel, MenuPanel)
│   ├── effects/           # Hiệu ứng (CameraShake)
│   ├── utils/             # Tiện ích (GameLogger, PerformanceMonitor)
│   └── main/              # Điểm khởi đầu (ArkanoidGame.java)
├── assets/
│   ├── Backgrounds/       # Nền các màn (Stage 1-5)
│   ├── Sprites/           # Hình ảnh game
│   ├── Sounds/            # File âm thanh (.wav)
│   └── Fonts/             # Font chữ
├── docs/                  # Tài liệu
├── Saves/                 # File lưu game
├── High Scores/           # Dữ liệu điểm cao
└── config.properties      # Cấu hình game
```

---

## 🏗️ KIẾN TRÚC KỸ THUẬT

### Thiết kế OOP
```
GameObject (abstract)
├── MovableObject (abstract)
│   ├── Ball
│   ├── Paddle
│   ├── Powerup
│   └── LaserBeam
└── Brick
```

### Design Patterns
- **Singleton**: ConfigManager, SoundManager, HighScoreManager, SaveGameManager
- **Factory**: BrickFactory, PowerUpFactory
- **MVC Pattern**: Tách biệt UI, logic và dữ liệu
- **Kế thừa & Đa hình**: Hệ thống phân cấp GameObject với abstract methods

### Công nghệ
- **Java 25** với Swing GUI framework
- **Java 2D Graphics** cho rendering
- **Serialization** cho chức năng lưu/tải
- **Properties** cho quản lý cấu hình
- **Multithreading** cho game loop mượt mà

---

## ⚙️ CẤU HÌNH

Chỉnh sửa `config.properties` để tùy chỉnh:

```properties
# Cài đặt cửa sổ
window.width=800
window.height=600
window.title=Arkanoid

# Cài đặt gameplay
game.initial.lives=3
game.max.level=5
game.fps=60

# Vật lý
ball.initial.speed=6
paddle.speed=8

# Âm thanh
sound.enabled=true
```

---

## 📚 TÀI LIỆU

- 📖 [DEVELOPMENT.md](arkanoid/docs/DEVELOPMENT.md) - Hướng dẫn phát triển
- 💾 [SAVE_GAME.md](arkanoid/docs/SAVE_GAME.md) - Tài liệu hệ thống Save/Load

---

## 🐛 XỬ LÝ LỖI

### Game không chạy được
- Kiểm tra Java version: `java -version` (cần Java 11+)
- Chạy từ thư mục gốc của project
- Kiểm tra `arkanoid.log` để xem lỗi

### Không có âm thanh
- Kiểm tra `sound.enabled=true` trong `config.properties`
- Đảm bảo file WAV có trong `assets/Sounds/`

### Lỗi Save/Load
- Kiểm tra thư mục `Saves/` đã được tạo
- Kiểm tra quyền ghi file
- Xem `arkanoid.log` để debug

---

## 🎯 MẸO CHƠI GAME

- **LASER** rất tốt để phá gạch nhanh
- **CATCH** giúp kiểm soát bóng tốt hơn
- **SLOW** phù hợp cho người mới chơi
- Gạch vàng cho điểm cao nhất (120)
- Gạch bạc cần 3 lần đánh nhưng có thể rơi power-up

---

**⭐ Chúc bạn chơi game vui vẻ!**

