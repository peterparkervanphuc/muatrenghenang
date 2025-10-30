import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon; // <-- IMPORT ĐƯỢC THÊM
import java.awt.Image;        // <-- IMPORT ĐƯỢC THÊM
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;

// Lớp Board giờ đóng vai trò gần giống như GameManager và Renderer (tạm thời)
// Quản lý các đối tượng (Ball, Paddle, Bricks) và điều khiển luồng game (Timer).
public class Board extends JPanel implements ActionListener, MouseMotionListener, KeyListener {
    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;
    private List<Ball> balls = new ArrayList<>();  // Thêm danh sách balls
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private int levelNumber=1;
    // Biến trạng thái game
    private int score;
    private int lives;
    private boolean inGame = true;
    private boolean isStarted = false; // Biến kiểm soát trạng thái bắt đầu
    private boolean isPaused = false;  // Biến kiểm soát trạng thái Tạm dừng
    private int bricksRemaining;
    private Timer closeTimer;
    private Level level;
    private int[][] map;
    private Image backgroundImage;
    private List<PowerUp> activePowerUps = new ArrayList<>();
    public Board() {
        try {
            backgroundImage = ImageIO.read(new File("assets/background.png"));
        } catch (Exception e) {
            System.out.println("Error loading background image!");
            e.printStackTrace();
        }
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseMotionListener(this);
        addKeyListener(this); // Thêm KeyListener
        score = 0;
        lives = 3; // Khởi tạo 3 mạng sống

        // Khởi tạo các đối tượng
        paddle = new Paddle(350, 550);
        // Đặt bóng lên trên paddle
        int initialBallX = paddle.getX() + (paddle.getWidth() / 2) - (Ball.getBallSize() / 2);
        int initialBallY = paddle.getY() - Ball.getBallSize() - 1; // -1 cho an toàn
        ball = new Ball(initialBallX, initialBallY, 2, -2); // Vận tốc ban đầu
        balls.add(ball);  // Thêm ball vào danh sách

        // Khởi tạo gạch (ĐÃ SỬA)
        bricks = new Brick[BRICK_ROWS][BRICK_COLS];
        loadMap(bricks, levelNumber);

        timer = new Timer(10, this);
        timer.start();
    }

    public void startGame() {
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }
     public void loadMap(Brick[][] bricks,int levelNumber) {
         level = new Level(levelNumber);
         bricksRemaining = 0;
         map=level.getLevel();
         // Sửa trong constructor - thêm gạch laser
         for (int i = 0; i < BRICK_ROWS; i++) {
             for (int j = 0; j < BRICK_COLS; j++) {
                 BrickType type;
                 if(map[i][j]==1) {
                     type = BrickType.NORMAL;
                     bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, type);
                 }else if(map[i][j]==2) {
                     type = BrickType.UNBREAKABLE;
                     bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, type);

                 }else if(map[i][j]==3) {
                     type = BrickType.FIREBALL_POWER;
                     bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, type);
                 }else if(map[i][j]==4) {
                     type = BrickType.LASER_POWER;
                     bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, type);
                 }else {
                     bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50, BrickType.NORMAL);
                     bricks[i][j].setVisible(false);
                 }
                 if (map[i][j]==1||map[i][j]==4||map[i][j]==3) {
                     bricksRemaining++;
                 }
             }
         }
     }
    // === VÒNG LẶP GAME (GAME LOOP) ===
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            if (isStarted && !isPaused) {
                for (Ball ball : balls) {
                    ball.updateFireballStatus(); // Thêm dòng này để cập nhật trạng thái fireball
                    ball.move();
                    checkWallCollisions(ball);
                    checkPaddleCollision(ball);
                    checkCollisions(ball);
                }
                checkGameStatus();
                updatePowerUps();

                // Update paddle lasers
                paddle.updateLasers();

                // Check laser collisions with bricks
                checkLaserCollisions();
            }
            else if (!isStarted) {
                int ballX = paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2);
                int ballY = paddle.getY() - ball.getHeight() - 1;
                ball.setPosition(ballX, ballY);
            }
            repaint();
        }
    }

    // === LOGIC VA CHẠM ===
    private void checkWallCollisions(Ball ball) {
        if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getWidth()) {
            ball.reverseDX();
        }
        if (ball.getY() <= 0) {
            ball.reverseDY();
        }
    }

    private void checkPaddleCollision(Ball ball) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            int ballCenter = ball.getX() + ball.getWidth() / 2;
            int intersect = ballCenter - paddleCenter;

            double normalizedIntersect = (double) intersect / (paddle.getWidth() / 2);

            ball.setDX((int) (normalizedIntersect * 5));
            ball.reverseDY();

            ball.setPosition(ball.getX(), paddle.getY() - ball.getHeight());
        }
    }

    private void checkCollisions(Ball ball) {
        Rectangle ballRect = ball.getBounds();
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                Brick b = bricks[i][j];
                if (b.isVisible() && ballRect.intersects(b.getBounds())) {
                    BrickType type = b.getType();
                    if (!type.isBreakable()) {
                        ball.reverseDY();
                    } else {
                        b.setVisible(false);
                        bricksRemaining--;
                        score += 10;
                        if (!ball.isFireball()) {
                            ball.reverseDY();
                        }
                        if (type.hasPowerUp()) {
                            spawnPowerUp(b);
                        }
                    }
                    return;
                }
            }
        }
    }

    private void spawnPowerUp(Brick b) {
        activePowerUps.add(new PowerUp(b.getX(), b.getY(), b.getType()));
    }

    private void updatePowerUps() {
        Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            if (!isPaused) {
                p.move();
            }
            if (paddle.getBounds().intersects(p.getBounds())) {
                activatePowerUp(p);
                iterator.remove();
            } else if (p.isOffScreen(getHeight())) {
                iterator.remove();
            }
        }
    }

    private void activatePowerUp(PowerUp p) {
        if (p.getType() == BrickType.FIREBALL_POWER) {
            for (Ball b : balls) {
                b.setFireball(true);
            }
        } else if (p.getType() == BrickType.LASER_POWER) {
            paddle.activateLaser();
        }
    }

    private void checkLaserCollisions() {
        List<Laser> lasers = paddle.getActiveLasers();
        Iterator<Laser> laserIterator = lasers.iterator();

        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            Rectangle laserRect = laser.getBounds();

            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_COLS; j++) {
                    Brick brick = bricks[i][j];
                    if (brick.isVisible() && brick.getType().isBreakable() &&
                            laserRect.intersects(brick.getBounds())) {
                        brick.setVisible(false);
                        bricksRemaining--;
                        score += 10;
                        laserIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    // === LOGIC TRẠNG THÁI GAME (THẮNG/THUA/MẠNG) ===
    private void checkGameStatus() {
        boolean allBallsLost = true;
        for (Ball ball : balls) {
            if (ball.getY() <= getHeight()) {
                allBallsLost = false;
                break;
            }
        }

        if (allBallsLost) {
            lives--;
            if (lives <= 0) {
                inGame = false;
                timer.stop();
                scheduleGameClose();
            } else {
                isStarted = false;
                // Reset ball position
                ball.setPosition(
                        paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2),
                        paddle.getY() - ball.getHeight() - 1
                );
            }
        }

        if (bricksRemaining == 0) {
            levelNumber++;
            loadMap(bricks,levelNumber);
            isStarted = false;
            if(levelNumber>4) {
                inGame = false;
                timer.stop();
                scheduleGameClose();
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

    // === CÁC HÀM VẼ (RENDERING) ===
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // === VẼ BACKGROUND TRƯỚC TIÊN (MỚI) ===
        if (backgroundImage != null) {
            // Vẽ ảnh nền lấp đầy toàn bộ panel
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        // =====================================

        // Code vẽ game cũ (phải nằm sau background)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        for (Ball ball : balls) {  // Vẽ tất cả balls
            ball.draw(g2d);
        }
        paddle.draw(g2d);
        drawBricks(g2d);

        for (PowerUp p : activePowerUps) {
            p.draw(g);
        }

        drawHUD(g2d);

        if (!inGame) {
            drawEndGameMessage(g2d);
        }

        if (isPaused && inGame) {
            drawPauseScreen(g2d);
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
        g.drawString("Lives: " + lives, 670, 20);
        g.drawString("Level: "+levelNumber,360,20);
    }

    private void drawBricks(Graphics g) {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j].draw(g);
            }
        }
    }

    private void drawPauseScreen(Graphics g) {
        String msg = "PAUSED";
        Font font = new Font("Helvetica", Font.BOLD, 40);
        FontMetrics fm = getFontMetrics(font);
        g.setColor(Color.BLUE);
        g.setFont(font);
        g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }


    // === CÁC BỘ LẮNG NGHE (LISTENERS) ===
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isPaused) {
            int mouseX = e.getX();
            int newX = mouseX - paddle.getWidth() / 2;
            if (newX < 0) newX = 0;
            if (newX > getWidth() - paddle.getWidth()) newX = getWidth() - paddle.getWidth();
            paddle.setPosition(newX);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!isStarted && inGame && !isPaused) {
                isStarted = true;
            } else if (paddle.hasLaser()) {
                paddle.shoot(); // Shoot lasers when space is pressed
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (inGame && isStarted) {
                isPaused = !isPaused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}