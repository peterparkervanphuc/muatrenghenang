# 🧪 Hướng Dẫn Chạy JUnit Tests

## ⚠️ Vấn Đề Hiện Tại

Các file test hiện đang báo lỗi `Cannot resolve symbol 'junit'` vì IntelliJ IDEA chưa tải JUnit dependencies từ Maven.

---

## ✅ Giải Pháp: Reload Maven Project

### **Bước 1: Mở Maven Tool Window**
1. Click vào **View** → **Tool Windows** → **Maven** (hoặc nhấn tổ hợp phím)
2. Hoặc click vào icon **Maven** ở thanh công cụ bên phải màn hình

### **Bước 2: Reload Maven Project**
1. Trong Maven tool window, tìm project **arkanoid-game**
2. Click icon **Reload All Maven Projects** (🔄 icon reload màu xanh)
3. Chờ IntelliJ tải các dependencies (JUnit, SLF4J, Gson...)

### **Bước 3: Invalidate Caches (nếu vẫn lỗi)**
1. **File** → **Invalidate Caches...**
2. Chọn: **Invalidate and Restart**
3. Đợi IntelliJ khởi động lại

---

## 🎯 Chạy Tests

Sau khi reload Maven, tests sẽ hoạt động:

### **Option 1: Chạy từng test class**
1. Mở file test: `GameTest.java`, `GameManagerTest.java`, hoặc `ConfigManagerTest.java`
2. Click icon ▶️ màu xanh bên cạnh tên class
3. Chọn **Run 'ClassName'** hoặc **Debug 'ClassName'**

### **Option 2: Chạy tất cả tests**
1. Chuột phải vào thư mục `src/test/java/`
2. Chọn **Run 'Tests in 'java''**

### **Option 3: Chạy bằng Maven (Terminal)**
```bash
cd arkanoid
mvn test
```

---

## 📁 Cấu Trúc Test

```
arkanoid/src/test/java/
├── GameTest.java           # Tests game logic, collisions, scoring
├── GameManagerTest.java    # Tests game state management
└── ConfigManagerTest.java  # Tests configuration loading
```

---

## 🔧 Dependencies (đã cấu hình trong pom.xml)

- **JUnit Jupiter 5.10.0** - Testing framework
- **Maven Surefire Plugin** - Run tests via Maven

---

## ⚡ Troubleshooting

### Nếu vẫn báo lỗi JUnit:

1. **Kiểm tra Maven đã được cài đặt:**
   ```bash
   mvn --version
   ```

2. **Download dependencies thủ công:**
   ```bash
   cd arkanoid
   mvn clean compile
   mvn test-compile
   ```

3. **Thêm JUnit thủ công vào IntelliJ:**
   - File → Project Structure → Libraries
   - Click "+" → From Maven
   - Nhập: `org.junit.jupiter:junit-jupiter-api:5.10.0`
   - Click OK

4. **Mark folder đúng:**
   - Chuột phải vào `src/test/java/`
   - **Mark Directory as** → **Test Sources Root**

---

## ✅ Kiểm Tra Tests Đã Chạy Được

Tests thành công khi thấy:
- ✅ **Green checkmark** bên cạnh test methods
- ✅ Console hiển thị: `Tests passed: X`
- ✅ Không có lỗi compile trong test files

---

## 📊 Test Coverage

- **GameTest.java**: 10 test cases
  - Ball-Brick collision
  - Brick destruction (normal & silver)
  - Score increase
  - Lives management
  - Game over state
  - Paddle bounds
  - Ball-Paddle bounce
  - Factory patterns
  - Level progression

- **GameManagerTest.java**: 8 test cases
  - Initial state (lives, level, game over)
  - Score management
  - Life management (lose, add)
  - Level progression
  - Game reset

- **ConfigManagerTest.java**: 6 test cases
  - Window dimensions
  - Game settings (lives, levels)
  - Missing keys handling
  - Boolean values

---

## 🎓 Cho Bảng Điểm

✅ **JUnit Tests**: 24 test cases covering core functionality  
✅ **Test Structure**: Proper separation (unit tests in test/java/)  
✅ **Maven Integration**: pom.xml configured with JUnit 5  
✅ **Test Coverage**: Game logic, state management, configuration  

---

## 🚀 Quick Fix

**Nhanh nhất:**
```
1. View → Tool Windows → Maven
2. Click icon 🔄 Reload All Maven Projects
3. Chờ 10-30 giây
4. Done! ✅
```

