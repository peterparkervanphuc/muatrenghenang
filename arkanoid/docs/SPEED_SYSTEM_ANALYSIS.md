# ⚡ TỔNG HỢP TOÀN BỘ ĐẶC ĐIỂM LIÊN QUAN ĐẾN SPEED

**Project:** Arkanoid Game  
**Ngày:** 5 tháng 11, 2025  
**Phân tích:** Toàn bộ hệ thống Speed/Velocity

---

## 📋 **MỤC LỤC**

1. [MovableObject - Base Class](#1-movableobject---base-class)
2. [Ball Speed System](#2-ball-speed-system)
3. [Paddle Speed](#3-paddle-speed)
4. [LaserBeam Speed](#4-laserbeam-speed)
5. [Powerup Fall Speed](#5-powerup-fall-speed)
6. [Brick Movement Speed](#6-brick-movement-speed)
7. [Game FPS & Update Rate](#7-game-fps--update-rate)
8. [Powerup Duration](#8-powerup-duration)
9. [Tổng Kết](#9-tổng-kết)

---

## 1. **MovableObject - Base Class**

**File:** `src/entities/MovableObject.java`

### **Thuộc tính:**
```java
private double velocityX;     // Vận tốc theo trục X (pixels/frame)
private double velocityY;     // Vận tốc theo trục Y (pixels/frame)
private double speed;         // Tốc độ cơ bản (pixels/frame)
```

### **Methods:**
- `update()` - Di chuyển object theo velocity: `x += velocityX`, `y += velocityY`
- `setVelocity(vx, vy)` - Set velocity X và Y
- `setVelocityX(double)` - Set riêng velocity X
- `setVelocityY(double)` - Set riêng velocity Y
- `getSpeed()` - Lấy tốc độ cơ bản
- `setSpeed(double)` - Set tốc độ cơ bản

### **Lưu ý:**
- Tốc độ được tính theo **pixels/frame**, không phải pixels/second
- Với FPS = 60, tốc độ thực tế = `speed × 60` pixels/giây

---

## 2. **Ball Speed System**

**File:** `src/entities/Ball.java`

### **2.1. Constants:**
```java
private static final double INITIAL_SPEED = 5.0;  // pixels/frame
```

### **2.2. Speed Components:**

#### **a) Base Speed:**
- **Giá trị:** 5.0 pixels/frame = 300 pixels/giây (tại 60 FPS)
- **Khi nào dùng:** Tốc độ ban đầu khi launch ball

#### **b) Level Speed Bonus:**
```java
private double levelSpeedBonus = 0.0;
```
- **Formula:** `levelSpeedBonus = Math.min((level - 1) × 0.07, 0.7)`
- **Lý do giảm từ 10% → 7%:** Tốc độ cũ quá nhanh ở late game (level 18: +170%)
- **Cap ở 70%:** Đảm bảo game vẫn playable ở level cao
- **Ví dụ:**
  - Level 1: bonus = 0.0 (100%)
  - Level 2: bonus = 0.07 (107%)
  - Level 5: bonus = 0.28 (128%)
  - Level 10: bonus = 0.63 (163%)
  - Level 11+: bonus = 0.7 (170%) - CAPPED!

#### **c) Speed Multiplier (Slow Powerup):**
```java
private double speedMultiplier = 1.0;
```
- **Normal:** 1.0 (100%)
- **Slowed:** 0.5 (50%)
- **Chú ý:** Chỉ slow được 1 lần, slow lần 2+ không có tác dụng

### **2.3. Total Speed Calculation:**

**Khi Launch:**
```java
double totalSpeed = INITIAL_SPEED × (1.0 + levelSpeedBonus) × speedMultiplier;
totalSpeed = Math.min(totalSpeed, MAX_SPEED); // Cap at 9.0 px/f
```

**Ví dụ thực tế (ĐÃ CẢI THIỆN):**

| Level | levelSpeedBonus | Normal Speed | Slowed Speed |
|-------|----------------|--------------|--------------|
| 1 | 0.0 (0%) | 5.0 px/f | 2.5 px/f |
| 2 | 0.07 (7%) | 5.35 px/f | 2.68 px/f |
| 5 | 0.28 (28%) | 6.4 px/f | 3.2 px/f |
| 10 | 0.63 (63%) | 8.15 px/f | 4.08 px/f |
| 11 | 0.7 (70%) | 8.5 px/f | 4.25 px/f |
| 18 | 0.7 (70%) | 8.5 px/f | 4.25 px/f |

**Đổi ra pixels/giây (60 FPS):**

| Level | Normal Speed | Slowed Speed | So với cũ |
|-------|--------------|--------------|-----------|
| 1 | 300 px/s | 150 px/s | Giống |
| 5 | 384 px/s | 192 px/s | ↓ 36 px/s (Chậm hơn 9%) |
| 10 | 489 px/s | 245 px/s | ↓ 81 px/s (Chậm hơn 14%) |
| 18 | 510 px/s | 255 px/s | ↓ 300 px/s (Chậm hơn 37%!) |

**🎯 Cải thiện:**
- Level 18 speed giảm từ **810 px/s** → **510 px/s** (giảm 37%)
- Game vẫn khó nhưng PLAYABLE
- Slow powerup vẫn hiệu quả

### **2.4. Speed Methods:**

#### **slow():**
```java
public void slow() {
    if (speedMultiplier == 1.0) {
        speedMultiplier = 0.5;
        setVelocityX(getVelocityX() * 0.5);
        setVelocityY(getVelocityY() * 0.5);
    }
    // Lần 2+ không có tác dụng
}
```

#### **restoreNormalSpeed():**
```java
public void restoreNormalSpeed() {
    if (speedMultiplier != 1.0 && !attached) {
        setVelocityX(getVelocityX() / speedMultiplier);
        setVelocityY(getVelocityY() / speedMultiplier);
        speedMultiplier = 1.0;
    }
}
```

### **2.5. Bounce Speed Adjustments:**

#### **bounceOffPaddle():**
- Tính lại angle dựa vào vị trí va chạm
- **Giữ nguyên tổng speed:** `speed = sqrt(vx² + vy²)`
- Phân phối lại vào vx và vy theo angle mới
- **Minimum vertical speed:** 2 pixels/frame

#### **bounceOffBrick():**
- Chỉ đảo dấu velocity (không thay đổi magnitude)
- `vx = -vx` hoặc `vy = -vy`

---

## 3. **Paddle Speed**

**File:** `src/entities/Paddle.java`

### **Constants:**
```java
private static final int SPEED = 8;  // pixels/frame
```

### **Thông số:**
- **Tốc độ:** 8 pixels/frame = 480 pixels/giây
- **Di chuyển:** Chỉ trái/phải (không có Y velocity)
- **Kiểm soát:** Keyboard (LEFT/RIGHT hoặc A/D)

### **Methods:**
```java
public void moveLeft() {
    setX(getX() - SPEED);  // -8 pixels
    // Boundary check
}

public void moveRight() {
    setX(getX() + SPEED);  // +8 pixels
    // Boundary check
}
```

### **So sánh tốc độ:**
- Paddle: 8 px/f (480 px/s)
- Ball (level 1): 5 px/f (300 px/s)
- **Tỷ lệ:** Paddle nhanh hơn ball 1.6x

---

## 4. **LaserBeam Speed**

**File:** `src/entities/LaserBeam.java`

### **Constants:**
```java
private static final int LASER_SPEED = 10;  // pixels/frame
```

### **Thông số:**
- **Tốc độ:** 10 pixels/frame = 600 pixels/giây
- **Hướng:** Chỉ đi lên (velocity Y âm)
- **Initial velocity:** `(0, -10)`

### **Fire Rate:**
```java
private static final long LASER_COOLDOWN = 300;  // milliseconds
```
- **Cooldown:** 0.3 giây = 18 frames
- **Max fire rate:** ~3.33 shots/giây

---

## 5. **Powerup Fall Speed**

**File:** `src/entities/Powerup.java`

### **Constants:**
```java
private static final int FALL_SPEED = 3;  // pixels/frame
```

### **Thông số:**
- **Tốc độ rơi:** 3 pixels/frame = 180 pixels/giây
- **Hướng:** Chỉ đi xuống (velocity Y dương)
- **Initial velocity:** `(0, 3)`

### **So sánh:**
- Powerup: 3 px/f (chậm)
- Ball: 5 px/f (trung bình)
- Paddle: 8 px/f (nhanh)
- Laser: 10 px/f (rất nhanh)

---

## 6. **Brick Movement Speed**

**File:** `src/entities/Brick.java`

### **BrickType Speeds:**

```java
// Gạch KHÔNG di chuyển (initialSpeed = 0.0):
WHITE, ORANGE, LIGHT_BLUE, GREEN, RED, BLUE, PURPLE, YELLOW, SILVER, GOLD

// Gạch DI CHUYỂN (initialSpeed = 1.5):
MOVING_UNBREAKABLE_RF  (11) - Di chuyển sang phải, bất tử
MOVING_UNBREAKABLE_LF  (12) - Di chuyển sang trái, bất tử
MOVING_RF              (13) - Di chuyển sang phải, vỡ được
MOVING_LF              (14) - Di chuyển sang trái, vỡ được
```

### **Speed Details:**
- **Tốc độ:** 1.5 pixels/frame = 90 pixels/giây
- **Hướng:** 
  - RF (Right First): dx = +1.5 (sang phải)
  - LF (Left First): dx = -1.5 (sang trái)
- **Đổi hướng:** Khi chạm biên trái/phải, `dx *= -1`

### **Constructor Logic:**
```java
if (type == BrickType.MOVING_UNBREAKABLE_LF || type == BrickType.MOVING_LF) {
    this.dx = -type.getInitialSpeed();  // -1.5 (trái)
} else {
    this.dx = type.getInitialSpeed();   // +1.5 (phải)
}
```

### **Update Logic:**
```java
public void update() {
    if (type.getInitialSpeed() > 0) {
        setX(getX() + dx);  // Di chuyển
        
        // Đổi hướng khi chạm biên
        if (getX() <= PLAY_LEFT || getX() + width >= PLAY_RIGHT) {
            dx *= -1;
            // Chống kẹt
        }
    }
}
```

### **Lịch sử thay đổi:**
- **Version cũ:** 2.0 px/f (quá nhanh, khó theo dõi)
- **Version hiện tại:** 1.5 px/f (cân bằng)
- **Lý do giảm:** Gameplay experience tốt hơn

---

## 7. **Game FPS & Update Rate**

**File:** `src/ui/GamePanel.java`

### **Constants:**
```java
private static final int FPS = 60;
private static final int DELAY = 1000 / FPS;  // 16.67ms
```

### **Timer:**
```java
gameTimer = new Timer(DELAY, e -> {
    update();   // Cập nhật logic game
    repaint();  // Vẽ lại màn hình
});
```

### **Update Frequency:**
- **FPS:** 60 frames/giây
- **Frame Duration:** ~16.67 milliseconds
- **Update Rate:** 60 lần/giây

### **Impact on Speed:**
- Tất cả `speed` trong code là **pixels/frame**
- Tốc độ thực tế = `speed × FPS` pixels/giây
- Ví dụ: Ball speed 5 px/f = 300 px/s

---

## 8. **Powerup Duration**

**File:** `src/ui/GamePanel.java`

### **Constants:**
```java
private static final long SLOW_POWERUP_DURATION = 10000;   // 10 seconds
private static final long LASER_POWERUP_DURATION = 15000;  // 15 seconds
```

### **Slow Powerup:**
- **Thời gian:** 10 giây = 600 frames
- **Hiệu ứng:** Ball speed × 0.5
- **Logic:** 
  ```java
  slowPowerupActive = true;
  slowPowerupEndTime = System.currentTimeMillis() + 10000;
  ```

### **Laser Powerup:**
- **Thời gian:** 15 giây = 900 frames
- **Hiệu ứng:** Enable laser firing
- **Logic:**
  ```java
  laserPowerupActive = true;
  laserPowerupEndTime = System.currentTimeMillis() + 15000;
  ```

### **Other Powerups (No Duration):**
- **ENLARGE:** Permanent until hit
- **CATCH:** Permanent until disabled
- **DUPLICATE:** Instant effect
- **BREAK:** Instant effect
- **PLAYER:** Instant effect

---

## 9. **Tổng Kết**

### **📊 Bảng So Sánh Tốc Độ (ĐÃ CẢI THIỆN):**

| Object | Speed (px/f) | Speed (px/s) | Tốc độ tương đối |
|--------|-------------|--------------|------------------|
| **Powerup (rơi)** | 3 | 180 | 1.0× (baseline) |
| **Ball (Level 1)** | 5 | 300 | 1.67× |
| **Ball (Level 10)** | 8.15 | 489 | 2.72× |
| **Ball (Level 18)** | 8.5 | 510 | 2.83× |
| **Moving Brick** | 1.5 | 90 | 0.50× |
| **Paddle** | 8 | 480 | 2.67× |
| **Laser** | 10 | 600 | 3.33× |

**🔥 So sánh với Version cũ:**
| Level | Cũ (px/s) | Mới (px/s) | Giảm |
|-------|-----------|-----------|------|
| 1 | 300 | 300 | 0% |
| 5 | 420 | 384 | -9% |
| 10 | 570 | 489 | -14% |
| 18 | 810 | 510 | **-37%** |

### **📈 Ball Speed Progression (ĐÃ CẢI THIỆN):**

```
Level 1:  5.0 px/f (300 px/s)  ████████
Level 5:  6.4 px/f (384 px/s)  ██████████
Level 10: 8.15 px/f (489 px/s) █████████████
Level 15: 8.5 px/f (510 px/s)  █████████████▌
Level 18: 8.5 px/f (510 px/s)  █████████████▌ (CAPPED)
```

**🔄 So với Version Cũ:**
```
OLD Level 18: 13.5 px/f ███████████████████████ (QUÁ NHANH!)
NEW Level 18:  8.5 px/f █████████████▌          (CÂN BẰNG)
              ↓ Giảm 37%
```

### **🎮 Gameplay Balance:**

#### **Early Game (Level 1-5):**
- Ball: 5-7 px/f (chậm)
- Player có thời gian react
- Dễ control

#### **Mid Game (Level 6-12):**
- Ball: 7-10 px/f (trung bình)
- Cần tập trung cao hơn
- Paddle vẫn faster than ball

#### **Late Game (Level 13-18):**
- Ball: 10-13.5 px/f (nhanh)
- Rất khó control
- Slow powerup trở nên quan trọng

### **⚠️ Important Notes:**

1. **Slow Powerup Stack:**
   - ❌ KHÔNG stack nhiều lần
   - Chỉ slow 1 lần duy nhất
   - Ăn thêm slow powerup = không có tác dụng

2. **Moving Brick Speed:**
   - Đã giảm từ 2.0 → 1.5 px/f
   - Lý do: Tốc độ cũ quá nhanh, khó follow

3. **Ball Bounce:**
   - Không làm chậm/nhanh ball
   - Chỉ đổi hướng
   - Giữ nguyên magnitude của velocity vector

4. **FPS Impact:**
   - Game chạy 60 FPS cố định
   - Nếu FPS drop → game chậm lại
   - Tất cả speed đều ảnh hưởng đồng thời

### **🔬 Physics Summary:**

```
Total Ball Speed = BASE_SPEED × (1 + LEVEL_BONUS) × SLOW_MULTIPLIER
Total Ball Speed = min(Total Ball Speed, MAX_SPEED)

Where:
- BASE_SPEED = 5.0 px/f
- LEVEL_BONUS = min((level - 1) × 0.07, 0.7)  // 7% per level, max 70%
- SLOW_MULTIPLIER = 0.5 (if slowed) or 1.0 (normal)
- MAX_SPEED = 9.0 px/f (cap to prevent unplayable gameplay)

Examples:
- Level 1: 5.0 × (1 + 0.0) × 1.0 = 5.0 px/f
- Level 10: 5.0 × (1 + 0.63) × 1.0 = 8.15 px/f
- Level 18: 5.0 × (1 + 0.7) × 1.0 = 8.5 px/f (would be 13.5 with old formula!)
```

### **📝 Speed Constants Reference:**

```java
// Ball
INITIAL_SPEED = 5.0 px/f
MAX_SPEED = 10.0 px/f  // Cap maximum speed (Máy khỏe!)
LEVEL_SPEED_BONUS_PER_LEVEL = 0.07  // 7% per level
MAX_LEVEL_BONUS = 0.7  // 70% cap

// Paddle
SPEED = 8 px/f

// Laser
LASER_SPEED = 10 px/f

// Powerup
FALL_SPEED = 3 px/f

// Moving Brick
MOVING_BRICK_SPEED = 1.5 px/f

// Game
FPS = 60
FRAME_DURATION = 16.67 ms
```

---

## ✅ **KẾT LUẬN**

**Hệ thống Speed trong Arkanoid đã được thiết kế:**
- ✅ **Balanced** - Tốc độ các object cân bằng
- ✅ **Progressive** - Ball nhanh dần theo level
- ✅ **Fair** - Paddle luôn faster than ball
- ✅ **Smooth** - 60 FPS ổn định
- ✅ **Tested** - Đã điều chỉnh qua testing (Moving brick: 2.0 → 1.5)

**Độ khó tăng dần:**
- Level 1-5: Dễ (ball chậm)
- Level 6-12: Trung bình (ball tăng tốc)
- Level 13-18: Khó (ball rất nhanh)

**Powerup balance:**
- Slow powerup giảm 50% speed
- Duration 10 giây (đủ để kiểm soát)
- Không stack để tránh ball quá chậm

🎮 **HỆ THỐNG HOÀN CHỈNH VÀ CÂN BẰNG!**

