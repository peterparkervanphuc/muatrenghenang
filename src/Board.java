import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// Lớp Board giờ đóng vai trò gần giống như GameManager và Renderer (tạm thời)
// Quản lý các đối tượng (Ball, Paddle, Bricks) và điều khiển luồng game (Timer).
public class Board extends JPanel implements ActionListener, MouseMotionListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private int score;
    private boolean inGame = true;
    private int bricksRemaining;
    private Timer closeTimer;

    public Board() {
        setBackground(Color.white);
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseMotionListener(this);
        score = 0;

        // Chú thích: Khởi tạo các đối tượng thay vì các biến riêng lẻ.
        paddle = new Paddle(350, 550);
        ball = new Ball(100, 100, 2, 2); // Khởi tạo đối tượng Ball

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
            // Chú thích: Logic di chuyển giờ được đóng gói trong lớp Ball.
            // Board chỉ cần gọi ball.move().
            ball.move();

            // Chú thích: Logic va chạm tường
            // Chúng ta gọi getter của Ball thay vì dùng biến local.
            if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getWidth()) {
                ball.reverseDX(); // Yêu cầu Ball tự đổi hướng
            }
            if (ball.getY() <= 0) {
                ball.reverseDY(); // Yêu cầu Ball tự đổi hướng
            }

            // Chú thích: Logic va chạm với Paddle
            if (ball.getBounds().intersects(paddle.getBounds())) {
                int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                int ballCenter = ball.getX() + ball.getWidth() / 2;
                int intersect = ballCenter - paddleCenter;

                double normalizedIntersect = (double) intersect / (paddle.getWidth() / 2);

                // Cập nhật tốc độ mới cho Ball
                ball.setDX((int) (normalizedIntersect * 5));
                ball.reverseDY();

                // Đặt lại vị trí Ball để tránh kẹt
                ball.setPosition(ball.getX(), paddle.getY() - ball.getHeight());
            }

            // Chú thích: Logic thua
            if (ball.getY() > getHeight()) {
                inGame = false;
                timer.stop();
                scheduleGameClose();
            }

            checkCollisions(); // Kiểm tra va chạm với gạch
            repaint();
        }
    }

    private void checkCollisions() {
        // Chú thích: Lấy Rectangle từ đối tượng Ball
        Rectangle ballRect = ball.getBounds();

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j].isVisible()) {
                    if (ballRect.intersects(bricks[i][j].getBounds())) {
                        bricks[i][j].setVisible(false);
                        ball.reverseDY(); // Yêu cầu Ball đổi hướng
                        score += 10;
                        bricksRemaining--;

                        if (bricksRemaining == 0) {
                            inGame = false;
                            timer.stop();
                            scheduleGameClose();
                        }
                        return;
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

        // Chú thích: Board yêu cầu các đối tượng TỰ VẼ chính chúng (Tính đa hình)
        ball.draw(g2d);     // Gọi hàm draw() của Ball
        paddle.draw(g2d);   // Gọi hàm draw() của Paddle
        drawBricks(g2d);    // Hàm này gọi draw() của từng Brick

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
    }

    private void drawBricks(Graphics g) {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                // Chú thích: Yêu cầu từng viên gạch tự vẽ
                bricks[i][j].draw(g);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int newX = mouseX - paddle.getWidth() / 2;
        if (newX < 0) newX = 0;
        if (newX > getWidth() - paddle.getWidth()) newX = getWidth() - paddle.getWidth();

        // Chú thích: Ra lệnh cho paddle di chuyển, thay vì đổi 'x' trực tiếp
        paddle.setPosition(newX);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
}