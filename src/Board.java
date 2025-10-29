import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Lớp Board giờ đóng vai trò gần giống như GameManager và Renderer (tạm thời)
// Quản lý các đối tượng (Ball, Paddle, Bricks) và điều khiển luồng game (Timer).
public class Board extends JPanel implements ActionListener, MouseMotionListener, KeyListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private int score;
    private int lives;
    private boolean inGame = true;
    private boolean isStarted = false; // Biến kiểm soát trạng thái bắt đầu
    private int bricksRemaining;
    private Timer closeTimer;

    public Board() {
        setBackground(Color.white);
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseMotionListener(this);
        addKeyListener(this); // Thêm KeyListener
        score = 0;
        lives = 3;

        // Khởi tạo các đối tượng
        paddle = new Paddle(350, 550);

        // Đặt bóng lên trên paddle
        int initialBallX = paddle.getX() + (paddle.getWidth() / 2) - (Ball.getBallSize() / 2);
        int initialBallY = paddle.getY() - Ball.getBallSize() - 1; // -1 cho an toàn
        ball = new Ball(initialBallX, initialBallY, 2, -2); // Vận tốc ban đầu

        // Khởi tạo gạch
        bricks = new Brick[BRICK_ROWS][BRICK_COLS];
        bricksRemaining = BRICK_ROWS * BRICK_COLS;
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50);
            }
        }

        timer = new Timer(10, this);
        timer.start();
    }

    public void startGame() {
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            // Chỉ chạy logic game khi isStarted == true
            if (isStarted) {
                ball.move();
                checkWallCollisions(); // Tách logic va chạm tường ra
                checkPaddleCollision(); // Tách logic va chạm paddle
                checkGameStatus(); // Kiểm tra thắng/thua
                checkCollisions(); // Kiểm tra va chạm gạch
            } else {
                // Nếu game chưa bắt đầu, giữ bóng dính trên paddle
                int ballX = paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2);
                int ballY = paddle.getY() - ball.getHeight() - 1;
                ball.setPosition(ballX, ballY);
            }

            repaint();
        }
    }

    private void checkWallCollisions() {
        if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getWidth()) {
            ball.reverseDX();
        }
        if (ball.getY() <= 0) {
            ball.reverseDY();
        }
    }

    private void checkPaddleCollision() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            int ballCenter = ball.getX() + ball.getWidth() / 2;
            int intersect = ballCenter - paddleCenter;

            double normalizedIntersect = (double) intersect / (paddle.getWidth() / 2);

            ball.setDX((int) (normalizedIntersect * 5));
            ball.reverseDY();

            // Đặt lại vị trí Ball để tránh kẹt
            ball.setPosition(ball.getX(), paddle.getY() - ball.getHeight());
        }
    }

    private void checkGameStatus() {
        // Logic thua
        if (ball.getY() > getHeight()) {
            lives--;
            if (lives <= 0) {
                inGame = false;
            timer.stop();
            scheduleGameClose();}
            else { isStarted = false; }
        }

        // Logic thắng
        if (bricksRemaining == 0) {
            inGame = false;
            timer.stop();
            scheduleGameClose();
        }
    }

    private void checkCollisions() {
        Rectangle ballRect = ball.getBounds();

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j].isVisible()) {
                    if (ballRect.intersects(bricks[i][j].getBounds())) {
                        bricks[i][j].setVisible(false);
                        ball.reverseDY();
                        score += 10;
                        bricksRemaining--;
                        return; // Thoát khỏi vòng lặp sau khi tìm thấy va chạm
                    }
                }
            }
        }
    }

    private void scheduleGameClose() {
        closeTimer = new Timer(2000, ev -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
                System.exit(0);
            }
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        // Yêu cầu các đối tượng tự vẽ (Đa hình)
        ball.draw(g2d);

        paddle.draw(g2d);
        drawBricks(g2d);
        drawHUD(g2d);

        if (!inGame) {
            drawEndGameMessage(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawEndGameMessage(Graphics g) {
        String msg = (bricksRemaining == 0) ? "You Win!" : "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics fm = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        String livesStr = "Lives: " + lives;
        FontMetrics fm = getFontMetrics(g.getFont());
        int livesWidth = fm.stringWidth(livesStr);
        g.drawString(livesStr, getWidth() - livesWidth - 10, 20);
    }

    private void drawBricks(Graphics g) {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j].draw(g);
            }
        }
    }

    // === Phương thức của MouseMotionListener ===
    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int newX = mouseX - paddle.getWidth() / 2;
        if (newX < 0) newX = 0;
        if (newX > getWidth() - paddle.getWidth()) newX = getWidth() - paddle.getWidth();

        paddle.setPosition(newX);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // === Các phương thức của KeyListener (ĐÃ DI CHUYỂN VÀO TRONG CLASS) ===
    @Override
    public void keyTyped(KeyEvent e) {} // Bỏ trống

    @Override
    public void keyPressed(KeyEvent e) {
        // KHI NHẤN SPACE THÌ BẮT ĐẦU GAME
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!isStarted && inGame) {
                isStarted = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {} // Bỏ trống

} // <-- Dấu ngoặc cuối cùng của CLASS BOARD