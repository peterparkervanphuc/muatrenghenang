# 💾 SAVE/LOAD GAME SYSTEM

**Version:** 2.1  
**Updated:** November 6, 2025

---

## 📋 OVERVIEW

Arkanoid game hỗ trợ lưu và tải game với **3 save slots** độc lập. Hệ thống lưu toàn bộ trạng thái game bao gồm level, score, lives, và vị trí của tất cả entities.

---

## 🎮 CÁCH SỬ DỤNG

### Quick Save/Load

```
F5 - Quick Save vào slot 1
F9 - Quick Load từ slot 1
```

### Save/Load Menu

```
F6 - Mở menu Save/Load
    ├─ Chọn slot 1-3
    ├─ Xem thông tin save (level, score, timestamp)
    └─ Delete save nếu cần
```

---

## 💾 FILE STRUCTURE

### Save Directory

```
Saves/
├── save_slot_1.dat
├── save_slot_2.dat
└── save_slot_3.dat
```

**Note:** Thư mục `Saves/` được tạo tự động khi chơi lần đầu.

### File Format

- **Format:** Java Serialization (`.dat`)
- **Encoding:** Binary
- **Size:** ~2-10 KB per save (tùy số lượng entities)

---

## 📊 DỮ LIỆU ĐƯỢC LƯU

### GameState Class

```java
public class GameState implements Serializable {
    // Game progress
    public int score;
    public int lives;
    public int currentLevel;
    public boolean gameOver;
    public long saveTimestamp;
    
    // Paddle state
    public double paddleX;
    public double paddleY;
    public boolean paddleEnlarged;
    public boolean paddleHasLaser;
    public boolean paddleHasCatch;
    
    // Powerup timers
    public long slowPowerupEndTime;
    public long laserPowerupEndTime;
    public boolean slowPowerupActive;
    public boolean laserPowerupActive;
    
    // Entities
    public List<BallState> balls;
    public List<BrickState> bricks;
    public List<PowerupState> powerups;
}
```

### Ball State

```java
public class BallState implements Serializable {
    public double x, y;
    public double velocityX, velocityY;
    public boolean attached;
    public double speedMultiplier;      // 1.0 normal, 0.5 slowed
    public double levelSpeedBonus;      // Based on level
}
```

### Brick State

```java
public class BrickState implements Serializable {
    public double x, y;
    public String brickType;            // Enum name (e.g., "RED", "SILVER")
    public int hitsRemaining;           // SILVER: 1-3, others: 0-1
}
```

### Powerup State

```java
public class PowerupState implements Serializable {
    public double x, y;
    public String powerupType;          // Enum name (e.g., "ENLARGE", "LASER")
}
```

---

## 🔧 API DOCUMENTATION

### SaveGameManager (Singleton)

```java
public class SaveGameManager {
    // Get instance
    public static SaveGameManager getInstance()
    
    // Save game
    public boolean saveGame(int slot, GameState state)
    
    // Load game
    public GameState loadGame(int slot)
    
    // Check if save exists
    public boolean hasSaveData(int slot)
    
    // Delete save
    public boolean deleteSave(int slot)
}
```

### Usage Example

**Saving:**
```java
// Create game state
SaveGameManager.GameState state = new SaveGameManager.GameState();
state.score = gameManager.getScore();
state.lives = gameManager.getLives();
state.currentLevel = gameManager.getCurrentLevel();
// ... fill other fields

// Save to slot
boolean success = SaveGameManager.getInstance().saveGame(1, state);
if (success) {
    System.out.println("Game saved!");
}
```

**Loading:**
```java
// Load from slot
SaveGameManager.GameState state = SaveGameManager.getInstance().loadGame(1);

if (state != null) {
    // Restore game state
    gameManager.setScore(state.score);
    gameManager.setLives(state.lives);
    gameManager.setCurrentLevel(state.currentLevel);
    
    // Restore paddle
    paddle.setX(state.paddleX);
    paddle.setY(state.paddleY);
    
    // Restore balls
    balls.clear();
    for (BallState bs : state.balls) {
        Ball ball = new Ball(bs.x, bs.y, state.currentLevel);
        ball.setVelocity(bs.velocityX, bs.velocityY);
        ball.setSpeedMultiplier(bs.speedMultiplier);
        ball.setAttached(bs.attached);
        balls.add(ball);
    }
    
    // ... restore bricks, powerups
}
```

---

## 🛡️ ERROR HANDLING

### Common Errors

**IOException:**
```java
try {
    saveGame(slot, state);
} catch (IOException e) {
    JOptionPane.showMessageDialog(this,
        "Failed to save game: " + e.getMessage(),
        "Save Error",
        JOptionPane.ERROR_MESSAGE);
}
```

**ClassNotFoundException:**
```java
// Khi load save từ version cũ không tương thích
try {
    loadGame(slot);
} catch (ClassNotFoundException e) {
    JOptionPane.showMessageDialog(this,
        "Save file is incompatible with this version",
        "Load Error",
        JOptionPane.ERROR_MESSAGE);
}
```

**Save Corruption:**
```java
// Auto-detect corrupted saves
GameState state = loadGame(slot);
if (state == null) {
    // Corrupted or not found
    return false;
}
```

---

## ⚠️ IMPORTANT NOTES

### 1. Serialization Version

```java
private static final long serialVersionUID = 1L;
```

**Important:** Khi thay đổi class structure, phải:
- Tăng `serialVersionUID`
- HOẶC implement custom `readObject()`/`writeObject()`

### 2. Save Slot Limits

- **Maximum slots:** 3
- **Maximum file size:** ~10 KB per save
- **No automatic cleanup**

### 3. Compatibility

**Forward compatibility:** ✅ Saves từ version cũ có thể load (với limitations)  
**Backward compatibility:** ❌ Saves từ version mới KHÔNG load được ở version cũ

### 4. Data Validation

Khi load game, validate:
```java
// Check valid range
if (state.lives < 0 || state.lives > 99) {
    state.lives = 5;  // Reset to default
}

// Check valid level
if (state.currentLevel < 1 || state.currentLevel > 18) {
    state.currentLevel = 1;
}

// Check ball count
if (state.balls.isEmpty()) {
    // Add default ball
}
```

---

## 🎯 BEST PRACTICES

### When to Save

**Good times:**
- Player presses F5 (manual save)
- Before quitting game
- After completing difficult level
- Before risky moves

**Bad times:**
- During ball movement (unpredictable state)
- During powerup activation
- During level transition

### Auto-Save Feature (Optional)

```java
// Auto-save every 5 minutes
Timer autoSaveTimer = new Timer(300000, e -> {
    if (!gameManager.isGameOver()) {
        saveGame(0);  // Slot 0 for auto-save
    }
});
```

### Save File Management

```java
// Get all saves info
public List<SaveInfo> getAllSaves() {
    List<SaveInfo> saves = new ArrayList<>();
    for (int slot = 1; slot <= 3; slot++) {
        if (hasSaveData(slot)) {
            GameState state = loadGame(slot);
            saves.add(new SaveInfo(slot, state.currentLevel, 
                                  state.score, state.saveTimestamp));
        }
    }
    return saves;
}
```

---

## 🔒 SECURITY CONSIDERATIONS

### File Permissions

```java
// Ensure save directory is writable
File saveDir = new File("Saves/");
if (!saveDir.exists()) {
    saveDir.mkdir();
}

if (!saveDir.canWrite()) {
    throw new IOException("Cannot write to Saves directory");
}
```

### Data Integrity

**Checksum (Optional):**
```java
// Add checksum to detect tampering
public class GameState implements Serializable {
    // ... fields
    private transient String checksum;
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        checksum = calculateChecksum();
        out.defaultWriteObject();
    }
    
    private void readObject(ObjectInputStream in) 
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (!verifyChecksum()) {
            throw new IOException("Save file corrupted");
        }
    }
}
```

---

## 📝 CHANGELOG

### Version 2.1 (Current)
- ✅ Added combo system state saving
- ✅ Added powerup timer saving
- ✅ Improved error messages
- ✅ Added save file validation

### Version 2.0
- ✅ 3 save slots
- ✅ Save all entities (balls, bricks, powerups)
- ✅ Save paddle state
- ✅ F5/F9 quick save/load

### Version 1.0
- ✅ Basic save/load
- ✅ 1 save slot only

---

## 🐛 KNOWN ISSUES

1. **Moving Bricks:** Direction not saved (resets to default)
   - **Workaround:** Will be fixed in v2.2

2. **Laser Beams:** Not saved (removed on load)
   - **Impact:** Minor, lasers are transient

3. **Camera Shake:** Not saved
   - **Impact:** None, visual effect only

---

## 📞 SUPPORT

If save/load not working:

1. Check `Saves/` directory exists
2. Check disk space (need ~10 KB free)
3. Check file permissions
4. Check `arkanoid.log` for errors
5. Try different save slot

---

**💾 SAVE YOUR PROGRESS OFTEN!**

Use F5 to quick save, especially before challenging levels!

