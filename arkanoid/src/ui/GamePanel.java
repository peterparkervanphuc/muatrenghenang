package ui;

import core.GameBounds;
import core.GameManager;
import core.LevelManager;
import effects.CameraShake;
import entities.Ball;
import entities.Brick;
import entities.LaserBeam;
import entities.Paddle;
import entities.Powerup;
import factories.PowerUpFactory;
import main.ArkanoidGame;
import managers.FontManager;
import managers.HighScoreManager;
import managers.SaveGameManager;
import managers.SoundManager;
import utils.GameLogger;
import utils.PerformanceMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main game panel where the Arkanoid gameplay happens
 */
public class GamePanel extends JPanel implements KeyListener {
    private ArkanoidGame mainFrame;
    private GameManager gameManager;
    private Timer gameTimer;

    private Paddle paddle;
    private ArrayList<Ball> balls;
    private ArrayList<Brick> bricks;
    private ArrayList<Powerup> powerups;
    private int breakableBricksCount; // <-- BIẾN ĐẾM MỚI
    private BufferedImage backgroundImage;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    // entities.Powerup timers
    private long slowPowerupEndTime = 0;
    private boolean slowPowerupActive = false;
    private long laserPowerupEndTime = 0;
    private boolean laserPowerupActive = false;

    // Camera shake effect
    private CameraShake cameraShake;

    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;
    private static final long SLOW_POWERUP_DURATION = 10000;
    private static final long LASER_POWERUP_DURATION = 15000;

    public GamePanel(ArkanoidGame mainFrame) {
        this.mainFrame = mainFrame;
        this.gameManager = new GameManager();

        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        balls = new ArrayList<>();
        bricks = new ArrayList<>();
        powerups = new ArrayList<>();

        cameraShake = new CameraShake();

        gameTimer = new Timer(DELAY, e -> {
            update();
            repaint();
        });
    }

    public void startNewGame() {
        gameManager.resetGame();
        initializeLevel();
        gameTimer.start();
        SoundManager.getInstance().stopMenuMusic();
        SoundManager.getInstance().playGameStartSound();
    }

    private void initializeLevel() {
        gameManager.setPaddleEnlarged(false);
        int paddleX = GameBounds.PLAY_LEFT + (GameBounds.PLAY_WIDTH - 80) / 2;
        int paddleY = GameBounds.PLAY_BOTTOM - 80;
        paddle = new Paddle(paddleX, paddleY, false);
        paddle.disableLaser();
        paddle.disableCatch();

        balls.clear();
        Ball ball = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, gameManager.getCurrentLevel());
        ball.attachToPaddle(paddle);
        ball.restoreNormalSpeed();
        balls.add(ball);

        spacePressed = false;
        slowPowerupEndTime = 0;
        laserPowerupEndTime = 0;

        // Load level bricks
        bricks.clear();
        bricks = LevelManager.loadLevel(gameManager.getCurrentLevel());

        // === ĐẾM GẠCH CÓ THỂ VỠ ===
        breakableBricksCount = 0;
        for (Brick brick : bricks) {
            if (brick.isBreakable()) {
                breakableBricksCount++;
            }
        }
        // ========================

        powerups.clear();
        loadBackground();
    }

    private void loadBackground() {
        try {
            int level = gameManager.getCurrentLevel();
            String bgPath = "Backgrounds/Stage " + level + ".png";
            var bgStream = getClass().getClassLoader().getResourceAsStream(bgPath);
            if (bgStream != null) {
                backgroundImage = ImageIO.read(bgStream);
                bgStream.close();
            } else {
                backgroundImage = null;
            }
        } catch (Exception e) {
            System.err.println("Could not load background: " + e.getMessage());
            backgroundImage = null;
        }
    }

    private void update() {
        if (gameManager.isGameOver()) {
            gameTimer.stop();
            return;
        }

        cameraShake.update();

        // === CẬP NHẬT GẠCH (ĐỂ GẠCH DI CHUYỂN) ===
        for (Brick brick : bricks) {
            brick.update();
        }
        // =====================================

        // Check if slow powerup expired
        if (slowPowerupActive && System.currentTimeMillis() > slowPowerupEndTime) {
            slowPowerupActive = false;
            for (Ball b : balls) { b.restoreNormalSpeed(); }
        }

        // Check if laser powerup expired
        if (laserPowerupActive && System.currentTimeMillis() > laserPowerupEndTime) {
            laserPowerupActive = false;
            if (paddle.hasLaser()) { paddle.disableLaser(); paddle.getLasers().clear(); }
        }

        // Update paddle position
        if (leftPressed) paddle.moveLeft();
        if (rightPressed) paddle.moveRight();

        // Launch ball on space press
        if (spacePressed) {
            for (Ball ball : balls) {
                if (ball.isAttached()) {
                    ball.launch();
                    spacePressed = false;
                    paddle.disableCatch();
                }
            }
        }

        // Update balls
        boolean needRespawn = false;
        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update(paddle);

            if (ball.getY() > GameBounds.PLAY_BOTTOM) {
                ballIterator.remove();
                if (balls.isEmpty()) needRespawn = true;
            }

            if (!needRespawn) {
                checkBallBrickCollision(ball);
            }

            if (!needRespawn && ball.intersects(paddle.getBounds()) && !ball.isAttached()) {
                if (paddle.hasCatch()) {
                    ball.attachToPaddle(paddle);
                    SoundManager.getInstance().playShipHitSound();
                } else {
                    ball.bounceOffPaddle(paddle);
                    SoundManager.getInstance().playShipHitSound();
                }
            }
        }

        // Handle ball respawn
        if (needRespawn) {
            gameManager.loseLife();
            SoundManager.getInstance().playDeathSound();
            cameraShake.shake(8, 20);
            spacePressed = false;
            resetAllPowerups();

            if (gameManager.getLives() > 0) {
                Ball newBall = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, gameManager.getCurrentLevel());
                newBall.attachToPaddle(paddle);
                balls.add(newBall);
            } else {
                gameManager.setGameOver(true);
                SoundManager.getInstance().playGameOverSound();
                showGameOverDialog();
            }
        }

        // Update powerups
        Iterator<Powerup> powerupIterator = powerups.iterator();
        while (powerupIterator.hasNext()) {
            Powerup powerup = powerupIterator.next();
            powerup.update();

            if (powerup.intersects(paddle.getBounds())) {
                applyPowerup(powerup);
                powerupIterator.remove();
            }

            if (powerup.getY() > getHeight()) {
                powerupIterator.remove();
            }
        }

        // Update paddle (laser beams)
        if (paddle.hasLaser()) {
            paddle.update();
            checkLaserBrickCollision();
        }

        // === SỬA LOGIC QUA MÀN ===
        if (breakableBricksCount <= 0) { // <-- SỬA TỪ bricks.isEmpty()
            levelCompleted();
            return;
        }
    }

    private void checkBallBrickCollision(Ball ball) {
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            if (ball.intersects(brick.getBounds())) {
                ball.bounceOffBrick(brick);

                // === SỬA LOGIC VA CHẠM GẠCH MỚI ===
                if (brick.isBreakable()) {
                    // GẠCH CÓ THỂ VỠ (Normal, Silver)
                    brick.hit();

                    if (brick.isDestroyed()) {
                        breakableBricksCount--; // <-- TRỪ BIẾN ĐẾM
                        gameManager.addScore(brick.getPoints());
                        brickIterator.remove();

                        Powerup powerup = PowerUpFactory.createPowerUpFromBrick(brick.getX(), brick.getY(), 0.45);
                        if (powerup != null) powerups.add(powerup);
                        SoundManager.getInstance().playWallHitSound();

                    } else if (brick.isSilver()) {
                        cameraShake.shake(4, 8);
                        SoundManager.getInstance().playSilverWallHitSound();
                    } else {
                        SoundManager.getInstance().playWallHitSound();
                    }
                } else {
                    // GẠCH BẤT TỬ (GOLD, MOVING) - Âm thanh bật lại mạnh, tạo rung màn hình
                    cameraShake.shake(4, 8);
                    SoundManager.getInstance().playShipHitSound();
                }
                // ===================================

                break;
            }
        }
    }

    private void checkLaserBrickCollision() {
        for (LaserBeam laser : paddle.getLasers()) {
            Iterator<Brick> brickIterator = bricks.iterator();
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();

                if (laser.intersects(brick.getBounds())) {

                    // === SỬA LOGIC VA CHẠM LASER MỚI ===
                    if (brick.isBreakable()) {
                        // GẠCH CÓ THỂ VỠ
                        brick.hit();
                        laser.setActive(false);

                        if (brick.isDestroyed()) {
                            breakableBricksCount--; // <-- TRỪ BIẾN ĐẾM
                            gameManager.addScore(brick.getPoints());
                            brickIterator.remove();
                            SoundManager.getInstance().playLaserBeamHitSound();
                        }
                    } else {
                        // GẠCH BẤT TỬ (GOLD, MOVING) - Âm thanh bật lại mạnh
                        laser.setActive(false);
                        SoundManager.getInstance().playShipHitSound();
                    }
                    // =====================================
                    break;
                }
            }
        }
    }

    private void applyPowerup(Powerup powerup) {
        switch (powerup.getType()) {
            case Powerup.PowerupType.ENLARGE:
                if (paddle.hasLaser()) break;
                paddle.enlarge();
                gameManager.setPaddleEnlarged(true);
                SoundManager.getInstance().playEnlargePowerupSound();
                break;

            case Powerup.PowerupType.LASER:
                if (paddle.isEnlarged()) {
                    paddle.shrink();
                    gameManager.setPaddleEnlarged(false);
                }
                laserPowerupActive = true;
                laserPowerupEndTime = System.currentTimeMillis() + LASER_POWERUP_DURATION;
                paddle.enableLaser();
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.CATCH:
                if (balls.size() > 1) break;
                paddle.enableCatch();
                for (Ball ball : balls) {
                    if (!ball.isAttached()) ball.attachToPaddle(paddle);
                }
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.SLOW:
                slowPowerupActive = true;
                slowPowerupEndTime = System.currentTimeMillis() + SLOW_POWERUP_DURATION;
                for (Ball ball : balls) ball.slow();
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.DUPLICATE:
                if (paddle.hasCatch()) break;
                if (balls.size() < 10) {
                    int ballsToCreate = Math.min(2, balls.size());
                    for (int i = 0; i < ballsToCreate; i++) {
                        Ball original = balls.get(i);
                        Ball newBall = new Ball(original.getX(), original.getY(), gameManager.getCurrentLevel());
                        if (original.isAttached()) {
                            newBall.attachToPaddle(paddle);
                        } else {
                            newBall.setVelocity(-original.getDx(), original.getDy());
                        }
                        balls.add(newBall);
                    }
                }
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.BREAK:
                destroyBottomRow(); // <-- Sẽ gọi hàm đã sửa
                SoundManager.getInstance().playBreakPowerupSound();
                break;

            case Powerup.PowerupType.PLAYER:
                gameManager.addLife();
                SoundManager.getInstance().playPlayerPowerupSound();
                break;
        }
    }

    private void destroyBottomRow() {
        if (bricks.isEmpty()) return;

        int maxY = bricks.stream()
                .mapToInt(brick -> (int)brick.getY())
                .max()
                .orElse(0);

        // Chỉ xóa gạch VỠ ĐƯỢC ở hàng dưới cùng
        // Gạch bất tử (GOLD, MOVING) sẽ được giữ lại
        bricks.removeIf(brick -> {
            if (brick.getY() == maxY && brick.isBreakable()) {
                breakableBricksCount--;
                return true; // Xóa gạch này
            }
            return false; // Giữ lại gạch bất tử hoặc gạch không phải hàng dưới
        });
    }

    private void resetAllPowerups() {
        powerups.clear();
        if (paddle.hasLaser()) {
            paddle.disableLaser();
            paddle.getLasers().clear();
        }
        if (paddle.hasCatch()) {
            paddle.disableCatch();
        }
        if (paddle.isEnlarged()) {
            paddle.shrink();
            gameManager.setPaddleEnlarged(false);
        }
        slowPowerupActive = false;
        slowPowerupEndTime = 0;
        laserPowerupActive = false;
        laserPowerupEndTime = 0;
    }

    private void levelCompleted() {
        gameTimer.stop();
        gameManager.nextLevel();

        if (gameManager.getCurrentLevel() <= 5) {
            Timer delayTimer = new Timer(2000, e -> {
                balls.clear();
                powerups.clear();
                initializeLevel();
                gameTimer.start();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        } else {
            gameManager.setGameOver(true);
            showVictoryDialog();
        }
    }

    private void showGameOverDialog() {
        Timer delayTimer = new Timer(1000, e -> {
            int score = gameManager.getScore();
            String name = JOptionPane.showInputDialog(this,
                    "Game Over! Your score: " + score + "\nEnter your name:",
                    "Game Over", JOptionPane.PLAIN_MESSAGE);

            if (name != null && !name.trim().isEmpty()) {
                HighScoreManager.addScore(name.trim(), score);
            }
            mainFrame.showMenu();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void showVictoryDialog() {
        // Play victory sound
        SoundManager.getInstance().playWinSound();

        Timer delayTimer = new Timer(1000, e -> {
            int score = gameManager.getScore();
            String name = JOptionPane.showInputDialog(this,
                    "Congratulations! You completed all levels!\nYour score: " + score + "\nEnter your name:",
                    "Victory!", JOptionPane.PLAIN_MESSAGE);

            if (name != null && !name.trim().isEmpty()) {
                HighScoreManager.addScore(name.trim(), score);
            }
            mainFrame.showMenu();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shakeX = cameraShake.getOffsetX();
        int shakeY = cameraShake.getOffsetY();
        g2d.translate(shakeX, shakeY);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        for (Brick brick : bricks) {
            brick.render(g2d);
        }
        paddle.render(g2d);
        for (Ball ball : balls) {
            ball.render(g2d);
        }
        for (Powerup powerup : powerups) {
            powerup.render(g2d);
        }

        g2d.translate(-shakeX, -shakeY);
        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);

        try {
            Font font = FontManager.getGameFont(16);
            g2d.setFont(font);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
        }

        g2d.drawString("SCORE: " + gameManager.getScore(), 20, 30);
        g2d.drawString("LIVES: " + gameManager.getLives(), 20, 50);
        g2d.drawString("LEVEL: " + gameManager.getCurrentLevel(), getWidth() - 180, 30);

        boolean anyBallAttached = false;
        for (Ball ball : balls) {
            if (ball.isAttached()) {
                anyBallAttached = true;
                break;
            }
        }

        if (anyBallAttached) {
            try {
                Font launchFont = FontManager.getGameFont(20);
                g2d.setFont(launchFont);
            } catch (Exception e) {
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
            }
            String message = "Press SPACE to launch";
            FontMetrics fm = g2d.getFontMetrics();
            int messageWidth = fm.stringWidth(message);
            int x = (getWidth() - messageWidth) / 2;
            int y = getHeight() / 2 + 100;
            g2d.setColor(Color.BLACK);
            g2d.drawString(message, x + 2, y + 2);
            g2d.setColor(Color.YELLOW);
            g2d.drawString(message, x, y);
        }
    }

    private SaveGameManager.GameState createGameState() {
        SaveGameManager.GameState state = new SaveGameManager.GameState();
        state.score = gameManager.getScore();
        state.lives = gameManager.getLives();
        state.currentLevel = gameManager.getCurrentLevel();
        state.gameOver = gameManager.isGameOver();
        state.paddleEnlarged = gameManager.isPaddleEnlarged();
        state.paddleX = paddle.getX();
        state.paddleY = paddle.getY();
        state.paddleHasLaser = paddle.hasLaser();
        state.paddleHasCatch = paddle.hasCatch();
        for (Ball ball : balls) {
            state.balls.add(new SaveGameManager.BallState(ball));
        }
        for (Brick brick : bricks) {
            state.bricks.add(new SaveGameManager.BrickState(brick));
        }
        for (Powerup powerup : powerups) {
            state.powerups.add(new SaveGameManager.PowerupState(powerup));
        }
        state.slowPowerupEndTime = slowPowerupEndTime;
        state.slowPowerupActive = slowPowerupActive;
        state.laserPowerupEndTime = laserPowerupEndTime;
        state.laserPowerupActive = laserPowerupActive;
        return state;
    }

    private void restoreGameState(SaveGameManager.GameState state) {
        if (state == null) return;

        restoreGameManagerState(state);

        paddle = new Paddle((int)state.paddleX, (int)state.paddleY, state.paddleEnlarged);
        if (state.paddleHasLaser) paddle.enableLaser();
        if (state.paddleHasCatch) paddle.enableCatch();

        balls.clear();
        for (SaveGameManager.BallState ballState : state.balls) {
            Ball ball = new Ball(ballState.x, ballState.y);
            ball.setVelocity(ballState.velocityX, ballState.velocityY);
            ball.setSpeedMultiplier(ballState.speedMultiplier);
            ball.setLevelSpeedBonus(ballState.levelSpeedBonus);
            ball.setAttached(ballState.attached);
            if (ballState.attached) {
                ball.attachToPaddle(paddle);
            }
            balls.add(ball);
        }

        bricks.clear();
        for (SaveGameManager.BrickState brickState : state.bricks) {
            try {
                Brick.BrickType type = Brick.BrickType.valueOf(brickState.brickType);
                Brick brick = new Brick((int)brickState.x, (int)brickState.y, type);
                brick.setHitsRemaining(brickState.hitsRemaining);
                bricks.add(brick);
            } catch (IllegalArgumentException e) {
                GameLogger.error("Invalid brick type: " + brickState.brickType);
            }
        }

        // === SỬA LỖI 1: ĐẾM LẠI GẠCH SAU KHI LOAD ===
        breakableBricksCount = 0;
        for (Brick brick : bricks) {
            if (brick.isBreakable()) {
                breakableBricksCount++;
            }
        }
        // =======================================

        powerups.clear();
        for (SaveGameManager.PowerupState powerupState : state.powerups) {
            try {
                Powerup.PowerupType type = Powerup.PowerupType.valueOf(powerupState.powerupType);
                Powerup powerup = new Powerup((int)powerupState.x, (int)powerupState.y, type);
                powerups.add(powerup);
            } catch (IllegalArgumentException e) {
                GameLogger.error("Invalid powerup type: " + powerupState.powerupType);
            }
        }

        slowPowerupEndTime = state.slowPowerupEndTime;
        slowPowerupActive = state.slowPowerupActive;
        laserPowerupEndTime = state.laserPowerupEndTime;
        laserPowerupActive = state.laserPowerupActive;

        loadBackground();
        GameLogger.info("Game state restored successfully");
    }

    private void restoreGameManagerState(SaveGameManager.GameState state) {
        gameManager.resetGame();
        gameManager.addScore(state.score);
        int currentLives = gameManager.getLives();
        int targetLives = state.lives;
        if (targetLives > currentLives) {
            for (int i = 0; i < targetLives - currentLives; i++) gameManager.addLife();
        } else if (targetLives < currentLives) {
            for (int i = 0; i < currentLives - targetLives; i++) gameManager.loseLife();
        }
        for (int i = 1; i < state.currentLevel; i++) {
            gameManager.nextLevel();
        }
        gameManager.setGameOver(state.gameOver);
        gameManager.setPaddleEnlarged(state.paddleEnlarged);
    }

    public void saveGame(int slot) {
        // ... (code saveGame giữ nguyên)
    }

    public void loadGame(int slot) {
        // ... (code loadGame giữ nguyên)
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            spacePressed = true;

            if (paddle.hasLaser()) {
                boolean anyBallLaunched = false;
                for (Ball ball : balls) {
                    if (!ball.isAttached()) {
                        anyBallLaunched = true;
                        break;
                    }
                }
                if (anyBallLaunched) {
                    paddle.fireLaser();
                    SoundManager.getInstance().playLaserBeamSound();
                }
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            gameTimer.stop();
            int option = JOptionPane.showConfirmDialog(this,
                    "Return to menu?", "Paused", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                mainFrame.showMenu();
            } else {
                gameTimer.start();
            }
        }

        // ... (code phím F5, F9, F6 giữ nguyên) ...
        if (key == KeyEvent.VK_F5) { /* ... */ }
        if (key == KeyEvent.VK_F9) { /* ... */ }
        if (key == KeyEvent.VK_F6) { /* ... */ }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}