# 🧪 Hướng Dẫn Chạy JUnit Tests

## ⚠️ Vấn Đề Hiện Tại

Các file test hiện đang báo lỗi `Cannot resolve symbol 'junit'` vì IntelliJ IDEA chưa có JUnit library.

---

## ✅ Giải Pháp: Thêm JUnit Library Trực Tiếp Vào IntelliJ

### **Phương Pháp 1: Tự động (KHUYẾN NGHỊ) - IntelliJ sẽ tự fix**

1. **Mở file test bất kỳ** (VD: `GameTest.java`)

2. **Đặt con trỏ vào dòng lỗi đỏ** (VD: dòng `import org.junit.jupiter.api.Test;`)

3. **Nhấn tổ hợp phím:**
   - Windows/Linux: `Alt + Enter`
   - Mac: `Option + Enter`

4. **Chọn:** `Add 'JUnit5.8.1' to classpath`
   
5. **Chọn:** `Use 'JUnit5.8.1' from IntelliJ IDEA distribution`

6. **Click OK** → IntelliJ sẽ tự động thêm JUnit vào project

7. **Đợi vài giây** để IntelliJ index lại

✅ **XONG!** Tất cả lỗi đỏ sẽ biến mất!

---

### **Phương Pháp 2: Thủ công (nếu Phương Pháp 1 không hiện)**

#### **Bước 1: Mở Project Structure**
1. **File** → **Project Structure...** (hoặc nhấn `Ctrl + Alt + Shift + S`)

#### **Bước 2: Thêm JUnit Library**
1. Chọn **Modules** ở menu bên trái
2. Chọn module **arkanoid**
3. Click tab **Dependencies**
4. Click nút **+** (Add) ở dưới cùng
5. Chọn **Library...** → **New Library** → **From Maven...**

#### **Bước 3: Download JUnit từ Maven**
1. Trong ô tìm kiếm, nhập: `org.junit.jupiter:junit-jupiter-api:5.10.0`
2. Click **Search** hoặc nhấn Enter
3. Chọn version **5.10.0** (hoặc mới nhất)
4. Click **OK**
5. IntelliJ sẽ download JUnit (~2-3 MB)

#### **Bước 4: Thêm JUnit Engine**
1. Lặp lại Bước 2-3 với: `org.junit.jupiter:junit-jupiter-engine:5.10.0`

#### **Bước 5: Apply & OK**
1. Click **Apply**
2. Click **OK**

#### **Bước 6: Rebuild Project**
1. **Build** → **Rebuild Project**
2. Đợi IntelliJ compile xong

---

### **Phương Pháp 3: Không cần Maven - Download JUnit JAR thủ công**

#### **Bước 1: Download JUnit JARs**
Tải các file sau từ Maven Central:
- [junit-jupiter-api-5.10.0.jar](https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.10.0/junit-jupiter-api-5.10.0.jar)
- [junit-jupiter-engine-5.10.0.jar](https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.10.0/junit-jupiter-engine-5.10.0.jar)
- [junit-platform-commons-1.10.0.jar](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-commons/1.10.0/junit-platform-commons-1.10.0.jar)
- [junit-platform-engine-1.10.0.jar](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-engine/1.10.0/junit-platform-engine-1.10.0.jar)
- [opentest4j-1.3.0.jar](https://repo1.maven.org/maven2/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar)
- [apiguardian-api-1.1.2.jar](https://repo1.maven.org/maven2/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar)

#### **Bước 2: Tạo thư mục lib**
```
D:\JavaProjects\Arkanoid\arkanoid\lib\
```
Copy tất cả JARs vào đây

#### **Bước 3: Thêm JARs vào IntelliJ**
1. **File** → **Project Structure** → **Modules**
2. Chọn tab **Dependencies**
3. Click **+** → **JARs or directories...**
4. Chọn tất cả JARs trong thư mục `lib/`
5. Click **OK**

---

## 🎯 Chạy Tests

Sau khi thêm JUnit, tests sẽ hoạt động:

### **Option 1: Chạy từng test class**
1. Mở file test: `GameTest.java`, `GameManagerTest.java`, hoặc `ConfigManagerTest.java`
2. Click icon ▶️ màu xanh bên cạnh tên class
3. Chọn **Run 'ClassName'**

### **Option 2: Chạy tất cả tests**
1. Chuột phải vào thư mục `src/test/java/`
2. Chọn **Run 'Tests in 'java''**

### **Option 3: Chạy từng test method**
1. Click icon ▶️ bên cạnh test method
2. Chọn **Run** hoặc **Debug**

---

## 📁 Cấu Trúc Test

```
arkanoid/src/test/java/
├── GameTest.java           # Tests game logic, collisions, scoring (10 tests)
├── GameManagerTest.java    # Tests game state management (8 tests)
└── ConfigManagerTest.java  # Tests configuration loading (6 tests)
```

**Tổng: 24 test cases**

---

## ✅ Kiểm Tra Tests Đã Hoạt Động

Tests thành công khi thấy:
- ✅ **Không có lỗi đỏ** trong test files
- ✅ **Icon ▶️ màu xanh** bên cạnh test classes/methods
- ✅ **Chạy test** → Console hiển thị: `Tests passed: X`
- ✅ **Green checkmark** ✓ bên cạnh test methods

---

## ⚡ Troubleshooting

### ❌ **Lỗi: "Cannot resolve symbol 'Test'"**
→ JUnit chưa được thêm vào project  
→ Làm lại **Phương Pháp 1** hoặc **Phương Pháp 2**

### ❌ **Không thấy icon ▶️ bên cạnh test**
→ IntelliJ chưa nhận diện test folder  
→ **Chuột phải vào `src/test/java/`** → **Mark Directory as** → **Test Sources Root**

### ❌ **Test không chạy được**
→ Rebuild project: **Build** → **Rebuild Project**

### ❌ **"No tests found"**
→ Kiểm tra test methods có annotation `@Test`  
→ Kiểm tra class/methods là `public`

---

## 📊 Test Coverage

- **GameTest.java**: 10 test cases
  - ✅ Ball-Brick collision detection
  - ✅ Brick destruction (normal & silver)
  - ✅ Score increase when brick destroyed
  - ✅ Lives decrease when ball lost
  - ✅ Game over state
  - ✅ Paddle boundary checking
  - ✅ Ball-Paddle bounce physics
  - ✅ Factory pattern (BrickFactory, PowerUpFactory)
  - ✅ Level progression
  - ✅ Random powerup creation

- **GameManagerTest.java**: 8 test cases
  - ✅ Initial game state (lives, level, game over)
  - ✅ Score management (add score)
  - ✅ Life management (lose life, add life)
  - ✅ Game over when no lives
  - ✅ Level progression
  - ✅ Game reset functionality

- **ConfigManagerTest.java**: 6 test cases
  - ✅ Window dimensions loading
  - ✅ Initial lives configuration
  - ✅ Max level configuration
  - ✅ Missing keys with default values
  - ✅ Boolean values loading

---

## 🎓 Cho Bảng Điểm

✅ **Unit Test bằng JUnit**: 24 test cases đầy đủ  
✅ **Test Structure**: Test folder riêng biệt (`src/test/java/`)  
✅ **Test Coverage**: Game logic, state management, configuration  
✅ **JUnit 5**: Sử dụng JUnit Jupiter (phiên bản mới nhất)  
✅ **Annotations**: `@Test`, `@BeforeEach`, `@DisplayName`  
✅ **Assertions**: `assertEquals`, `assertTrue`, `assertFalse`, `assertNotNull`  

---

## 🚀 Quick Fix (Nhanh nhất)

**Chỉ cần 3 bước:**
```
1. Mở file GameTest.java
2. Đặt cursor vào dòng import org.junit...
3. Nhấn Alt + Enter → Chọn "Add JUnit5 to classpath"
✅ XONG!
```

---

## 💡 Tips

- Sau khi thêm JUnit, **Rebuild Project** để IntelliJ nhận diện
- Test files phải có annotation `@Test` trước methods
- Test methods nên có tên rõ ràng: `testXxxYyy()`
- Sử dụng `@DisplayName` để mô tả test rõ hơn
- Chạy tests thường xuyên để phát hiện bugs sớm

---

## 📞 Support

Nếu vẫn gặp vấn đề:
1. Kiểm tra Java JDK đã cài đúng (Java 11+)
2. Restart IntelliJ IDEA
3. Invalidate Caches: **File** → **Invalidate Caches** → **Invalidate and Restart**


