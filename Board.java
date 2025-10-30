public class Board {
    // ...existing code...

    public Board() {
        // ...existing code...

        // Khởi tạo gạch (ĐÃ SỬA)
        bricks = new Brick[BRICK_ROWS][BRICK_COLS];
        bricksRemaining = BRICK_ROWS * BRICK_COLS; // Bắt đầu với tổng số gạch
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                
                // === LOGIC MỚI ĐỂ TẠO GẠCH CỨNG ===
                boolean unbreakable = false;
                // i == BRICK_ROWS - 1 (là hàng dưới cùng)
                if (i == BRICK_ROWS - 1 && (j == 3 || j == 4 || j == 5)) {
                    unbreakable = true;
                    bricksRemaining--; // Gạch cứng không tính vào điểm thắng
                }
                // ==================================

                // Sửa lại hàm khởi tạo để truyền 'unbreakable'
                bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, unbreakable);
            }
        }

        // ...existing code...
    }

    // ...existing code...

    private void checkCollisions() {
        Rectangle ballRect = ball.getBounds();

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {

                Brick b = bricks[i][j]; // Lấy viên gạch

                if (b.isVisible()) {
                    if (ballRect.intersects(b.getBounds())) {

                        // === LOGIC MỚI ĐỂ XỬ LÝ GẠCH CỨNG ===
                        if (b.isUnbreakable()) {
                            // 1. Gạch cứng
                            ball.reverseDY(); // Chỉ nảy bóng
                        } else {
                            // 2. Gạch thường
                            b.setVisible(false); // Vỡ gạch
                            ball.reverseDY();
                            score += 10;
                            bricksRemaining--;
                        }
                        // ===================================

                        return; // Thoát khỏi vòng lặp sau khi tìm thấy va chạm
                    }
                }
            }
        }
    }

    // ...existing code...
}

