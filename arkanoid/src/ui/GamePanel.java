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
import managers.SoundManager;
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
    
    private BufferedImage backgroundImage;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;
    
    // entities.Powerup timers
    private long slowPowerupEndTime = 0;
    private boolean slowPowerupActive = false;
    
    // Camera shake effect
    private CameraShake cameraShake;

    private static final int FPS = 60;
    private static final int DELAY = 1000 / FPS;
    private static final long SLOW_POWERUP_DURATION = 10000; // 10 seconds
    
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
        
        // Initialize camera shake
        cameraShake = new CameraShake();

        // Initialize game timer
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
        // Reset all powerup effects when starting new level
        gameManager.setPaddleEnlarged(false);
        
        // Create paddle (centered in playable area, normal size)
        int paddleX = GameBounds.PLAY_LEFT + (GameBounds.PLAY_WIDTH - 80) / 2;
        int paddleY = GameBounds.PLAY_BOTTOM - 80;
        paddle = new Paddle(paddleX, paddleY, false); // Always start with normal size
        
        // Disable all paddle powerups
        paddle.disableLaser();
        paddle.disableCatch();
        
        // Create ball (attached to paddle, ready to launch with Space key)
        // entities.Ball speed increases with each level
        balls.clear();
        Ball ball = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, gameManager.getCurrentLevel());
        ball.attachToPaddle(paddle);  // entities.Ball is now attached and waiting for Space key
        ball.restoreNormalSpeed(); // Reset ball speed to normal
        balls.add(ball);
        
        // Reset space key state
        spacePressed = false;
        
        // Reset powerup timers
        slowPowerupEndTime = 0;
        
        // Load level bricks
        bricks.clear();
        bricks = LevelManager.loadLevel(gameManager.getCurrentLevel());
        
        // Clear powerups
        powerups.clear();
        
        // Load background
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
        
        // Update camera shake
        cameraShake.update();

        // Check if slow powerup expired
        if (slowPowerupActive && System.currentTimeMillis() > slowPowerupEndTime) {
            slowPowerupActive = false;
            // Restore normal speed for all balls
            for (Ball ball : balls) {
                ball.restoreNormalSpeed();
            }
        }
        
        // Update paddle position
        if (leftPressed) {
            paddle.moveLeft();
        }
        if (rightPressed) {
            paddle.moveRight();
        }
        
        // Launch ball on space press
        if (spacePressed) {
            for (Ball ball : balls) {
                if (ball.isAttached()) {
                    ball.launch();
                    spacePressed = false;
                    // Disable catch after launching to prevent re-catching on next collision
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
            
            // Check if ball fell off screen (below bottom border)
            if (ball.getY() > GameBounds.PLAY_BOTTOM) {
                ballIterator.remove();
                
                if (balls.isEmpty()) {
                    needRespawn = true;
                }
            }
            
            // Check collision with bricks
            if (!needRespawn) {
                checkBallBrickCollision(ball);
            }
            
            // Check collision with paddle
            if (!needRespawn && ball.intersects(paddle.getBounds()) && !ball.isAttached()) {
                // If paddle has Catch powerup, attach ball instead of bouncing
                if (paddle.hasCatch()) {
                    ball.attachToPaddle(paddle);
                    SoundManager.getInstance().playShipHitSound();
                } else {
                    // Normal bounce if no Catch powerup
                    ball.bounceOffPaddle(paddle);
                    SoundManager.getInstance().playShipHitSound();
                }
            }
        }
        
        // Handle ball respawn after iterator loop
        if (needRespawn) {
            gameManager.loseLife();
            SoundManager.getInstance().playDeathSound();
            
            // Strong shake when losing a life
            cameraShake.shake(8, 20);

            // Reset space key to prevent auto-launch
            spacePressed = false;
            
            // Reset all powerups when ball is lost
            resetAllPowerups();
            
            if (gameManager.getLives() > 0) {
                // Respawn ball on paddle (attached state) with current level speed
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
            
            // Check if caught by paddle
            if (powerup.intersects(paddle.getBounds())) {
                // FIX: Only apply powerup if at least one ball is launched (not attached)
                // Prevents powerups from activating when all balls are waiting to launch
                boolean anyBallLaunched = false;
                for (Ball ball : balls) {
                    if (!ball.isAttached()) {
                        anyBallLaunched = true;
                        break;
                    }
                }

                if (anyBallLaunched) {
                    applyPowerup(powerup);
                }
                // Always remove powerup after touching paddle (whether applied or not)
                powerupIterator.remove();
            }
            
            // Remove if off screen
            if (powerup.getY() > getHeight()) {
                powerupIterator.remove();
            }
        }
        
        // Update paddle (includes laser beams update) if paddle has laser
        if (paddle.hasLaser()) {
            paddle.update();
            checkLaserBrickCollision();
        }
        
        // Check if level completed (do this last)
        if (bricks.isEmpty()) {
            levelCompleted();
            return; // Stop updating immediately
        }
    }
    
    private void checkBallBrickCollision(Ball ball) {
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            
            if (ball.intersects(brick.getBounds())) {
                // Determine collision side and bounce
                ball.bounceOffBrick(brick);
                
                // Damage brick
                brick.hit();
                
                if (brick.isDestroyed()) {
                    gameManager.addScore(brick.getPoints());
                    brickIterator.remove();
                    
                    // Random powerup drop using PowerUpFactory (45% chance)
                    Powerup powerup = PowerUpFactory.createPowerUpFromBrick(brick.getX(), brick.getY(), 0.45);
                    if (powerup != null) {
                        powerups.add(powerup);
                    }
                    
                    SoundManager.getInstance().playWallHitSound();
                } else if (brick.isSilver()) {
                    // Screen shake ONLY for silver brick hit
                    cameraShake.shake(4, 8);
                    SoundManager.getInstance().playSilverWallHitSound();
                } else {
                    SoundManager.getInstance().playWallHitSound();
                }
                
                break; // Only collide with one brick per update
            }
        }
    }
    
    private void checkLaserBrickCollision() {
        for (LaserBeam laser : paddle.getLasers()) {
            Iterator<Brick> brickIterator = bricks.iterator();
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();
                
                if (laser.intersects(brick.getBounds())) {
                    brick.hit();
                    laser.setActive(false);
                    
                    if (brick.isDestroyed()) {
                        gameManager.addScore(brick.getPoints());
                        brickIterator.remove();
                        SoundManager.getInstance().playLaserBeamHitSound();
                    }
                    break;
                }
            }
        }
    }
    
    private void applyPowerup(Powerup powerup) {
        switch (powerup.getType()) {
            case Powerup.PowerupType.ENLARGE:
                // If Laser is active, don't activate Enlarge (Laser has priority)
                if (paddle.hasLaser()) {
                    // Do nothing - Laser continues to work, Enlarge is ignored
                    break;
                }
                paddle.enlarge();
                gameManager.setPaddleEnlarged(true);
                SoundManager.getInstance().playEnlargePowerupSound();
                break;

            case Powerup.PowerupType.LASER:
                // If Enlarge is active, shrink paddle when Laser is activated
                if (paddle.isEnlarged()) {
                    paddle.shrink();
                    gameManager.setPaddleEnlarged(false);
                }
                paddle.enableLaser();
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.CATCH:
                // CONFLICT: If player already used Duplicate, ignore Catch
                // (Prevent combining Catch + Duplicate for game balance)
                if (balls.size() > 1) {
                    // Multiple balls means Duplicate was used - ignore Catch
                    break;
                }

                // CATCH has priority: disable Laser if it's active
                if (paddle.hasLaser()) {
                    paddle.disableLaser();
                }

                // Enable catch mode first
                paddle.enableCatch();

                // IMPORTANT: Attach ALL balls to paddle immediately, regardless of position
                // This puts the game into "launch" state where player must press Space
                for (Ball ball : balls) {
                    if (!ball.isAttached()) {
                        ball.attachToPaddle(paddle);
                    }
                }
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.SLOW:
                // Slow powerup: slow all balls for 10 seconds
                slowPowerupActive = true;
                slowPowerupEndTime = System.currentTimeMillis() + SLOW_POWERUP_DURATION;
                for (Ball ball : balls) {
                    ball.slow();
                }
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.DUPLICATE:
                // CONFLICT: If Catch is active, ignore Duplicate
                // (Prevent combining Catch + Duplicate for game balance)
                if (paddle.hasCatch()) {
                    // Catch is active - ignore Duplicate
                    break;
                }

                // Duplicate: create 2 new balls for each existing ball (max 10 total)
                if (balls.size() < 10) { // Limit to 10 balls
                    int ballsToCreate = Math.min(2, balls.size());
                    for (int i = 0; i < ballsToCreate; i++) {
                        Ball original = balls.get(i);
                        Ball newBall = new Ball(original.getX(), original.getY(), gameManager.getCurrentLevel());

                        // If original ball is attached, new ball is also attached
                        if (original.isAttached()) {
                            newBall.attachToPaddle(paddle);
                        } else {
                            // entities.Ball is moving - create new ball with opposite direction
                            newBall.setVelocity(-original.getDx(), original.getDy());
                        }

                        balls.add(newBall);
                    }
                }
                SoundManager.getInstance().playPlayerPowerupSound();
                break;

            case Powerup.PowerupType.BREAK:
                // Destroy one row of bricks at the bottom
                destroyBottomRow();
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
        
        // Find the maximum Y coordinate (bottom-most bricks)
        int maxY = bricks.stream()
            .mapToInt(brick -> (int)brick.getY())
            .max()
            .orElse(0);
        
        // Remove all bricks at that Y coordinate
        bricks.removeIf(brick -> brick.getY() == maxY);
    }
    
    /**
     * Reset all active powerup effects
     * Called when ball is lost OR when starting a new level
     */
    private void resetAllPowerups() {
        // Clear all falling powerups to prevent activation during respawn
        powerups.clear();

        // Disable all paddle powerups
        if (paddle.hasLaser()) {
            paddle.disableLaser();
        }
        if (paddle.hasCatch()) {
            paddle.disableCatch();
        }
        
        // Shrink paddle if enlarged
        if (paddle.isEnlarged()) {
            paddle.shrink();
            gameManager.setPaddleEnlarged(false);
        }
        
        // Reset slow powerup timer
        slowPowerupActive = false;
        slowPowerupEndTime = 0;
        
        // Note: Extra lives (PLAYER powerup) are NOT removed
        // Note: Duplicate balls will be lost naturally when all balls fall
    }
    
    private void levelCompleted() {
        gameTimer.stop();
        
        gameManager.nextLevel();
        
        if (gameManager.getCurrentLevel() <= 5) {
            // Load next level after delay
            Timer delayTimer = new Timer(2000, e -> {
                // Clear all active powerups and balls before next level
                balls.clear();
                powerups.clear();
                
                // Initialize new level with ball attached to paddle
                initializeLevel();
                
                // Restart game timer
                gameTimer.start();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        } else {
            // Game completed!
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
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Apply camera shake offset
        int shakeX = cameraShake.getOffsetX();
        int shakeY = cameraShake.getOffsetY();
        g2d.translate(shakeX, shakeY);

        // Draw background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Draw bricks
        for (Brick brick : bricks) {
            brick.render(g2d);
        }
        
        // Draw paddle
        paddle.render(g2d);
        
        // Draw balls (Polymorphism!)
        for (Ball ball : balls) {
            ball.render(g2d);
        }
        
        // Draw powerups
        for (Powerup powerup : powerups) {
            powerup.render(g2d);
        }
        
        // Reset camera shake transform before drawing UI
        g2d.translate(-shakeX, -shakeY);

        // Draw UI (without shake effect)
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
        
        // Draw score
        g2d.drawString("SCORE: " + gameManager.getScore(), 20, 30);
        
        // Draw lives
        g2d.drawString("LIVES: " + gameManager.getLives(), 20, 50);
        
        // Draw level
        g2d.drawString("LEVEL: " + gameManager.getCurrentLevel(), getWidth() - 180, 30);

        // Draw "Press SPACE to launch" message when ball is attached
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

            // Draw shadow for better visibility
            g2d.setColor(Color.BLACK);
            g2d.drawString(message, x + 2, y + 2);

            // Draw main text
            g2d.setColor(Color.YELLOW);
            g2d.drawString(message, x, y);
        }
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
            
            // Fire laser ONLY if paddle has laser AND at least one ball is launched
            if (paddle.hasLaser()) {
                // Check if any ball is launched (not attached)
                boolean anyBallLaunched = false;
                for (Ball ball : balls) {
                    if (!ball.isAttached()) {
                        anyBallLaunched = true;
                        break;
                    }
                }

                // Only fire laser if ball is in play
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
