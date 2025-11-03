# 💾 Save/Load Game System

**Last Updated:** November 3, 2025

---

## 🎮 HOW TO USE

### Save Game

#### Auto-Save
- Game tự động lưu vào **Slot 1** mỗi **30 giây**
- Không cần thao tác, hoạt động trong background

#### Manual Save (F5)
1. Nhấn **F5** trong game
2. Chọn slot (1, 2, hoặc 3)
3. Confirm để lưu
4. Game tiếp tục sau khi lưu

### Load Game

#### From Menu
1. Chọn **LOAD GAME** từ menu
2. Xem thông tin các save slots:
   - Level, Score, Lives
   - "Empty" nếu slot trống
3. Chọn slot muốn load
4. Game khôi phục trạng thái đã lưu

#### In-Game (F9)
1. Nhấn **F9** trong game
2. Chọn slot
3. Game pause và load state

### Delete Save

#### From Menu
1. Chọn **DELETE SAVE** từ menu
2. Xem thông tin các save slots
3. Chọn slot muốn xóa
4. Confirm deletion (WARNING message)
5. Save bị xóa vĩnh viễn

#### In-Game (F6)
1. Nhấn **F6** trong game
2. Game pause, chọn slot muốn xóa
3. Confirm deletion
4. Slot trở về Empty

⚠️ **Cảnh báo:** Xóa save không thể hoàn tác!

---

## 💾 SAVED DATA

### Game State
- ✅ Score, Lives, Current Level
- ✅ Paddle position & powerups (Enlarged, Laser, Catch)
- ✅ Ball(s) position, velocity, attached status
- ✅ All bricks (position, type, hits remaining)
- ✅ Falling powerups
- ✅ Powerup timers (Slow, Laser duration)

---

## ⌨️ CONTROLS

| Key | Action |
|-----|--------|
| **F5** | Save game (choose slot) |
| **F6** | Delete save (with confirmation) |
| **F9** | Load game (choose slot) |

---

## 📁 SAVE FILES

```
arkanoid/
└── Saves/
    ├── save_slot_1.dat    # Auto-save
    ├── save_slot_2.dat    # Manual save
    └── save_slot_3.dat    # Manual save
```

- **Format**: Binary (.dat)
- **Auto-created**: Saves/ folder created automatically
- **Slots**: 3 independent save slots

---

## 🎯 FEATURES

✅ **Implemented:**
- [x] Auto-save every 30 seconds (Slot 1)
- [x] Manual save with 3 slots (F5)
- [x] Load from menu or in-game (F9)
- [x] Display save slot info
- [x] Full game state restoration
- [x] Pause/Resume on save/load

---

## 🐛 ERROR HANDLING

### Corrupt Save
- Error message displayed
- Save file not deleted (can retry)

### Missing Save
- Shows "Empty" in dialog
- Cannot load from empty slot

### All Operations Logged
Check `arkanoid.log` for save/load events:
```
[INFO] Game saved to slot 1
[INFO] Auto-save completed
[INFO] Game loaded from slot 2
```

---

**💡 Tip:** Slot 1 luôn là auto-save, sử dụng Slot 2 & 3 cho manual saves!


