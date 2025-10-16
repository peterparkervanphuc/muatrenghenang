import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Board extends JPanel implements ActionListener, MouseMotionListener {
    private Timer timer;
    private int ballX=100,ballY=100;
    private int ballDX=2,ballDY=2;
    private final int BALL_SIZE=20;
    private Paddle paddle;
    private Brick[][] bricks;
    private final int BRICK_ROWS = 5;
    private final int BRICK_COLS = 10;
    private int score;
    private boolean inGame = true;

    public Board() {
       setBackground(Color.white);
       setFocusable(true);
       addMouseMotionListener(this); // Thêm MouseMotionListener thay vì KeyListener
       paddle = new Paddle(350,550);
       score = 0;

       bricks = new Brick[BRICK_ROWS][BRICK_COLS];
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j] = new Brick(j * 75 + 45, i * 25 + 50);
            }
        }

       timer = new Timer(10,this);
       timer.start();
    }
    public void startGame() {
        setFocusable(true); // Cho phép panel này nhận focus
        requestFocusInWindow(); // Yêu cầu focus ngay lập tức để nhận sự kiện phím
        timer.start(); // Bắt đầu vòng lặp game
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            ballX += ballDX;
            ballY += ballDY;

            if (ballX <= 0 || ballX >= getWidth() - BALL_SIZE) {
                ballDX = -ballDX;
            }
            if (ballY <= 0) { // Top wall only
                ballDY = -ballDY;
            }

            Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
            if (ballRect.intersects(paddle.getBounds())) {
                // More dynamic bounce
                int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                int ballCenter = ballX + BALL_SIZE / 2;
                int intersect = ballCenter - paddleCenter;

                // Normalize the intersect value and scale it
                double normalizedIntersect = (double) intersect / (paddle.getWidth() / 2);
                ballDX = (int) (normalizedIntersect * 5); // Max horizontal speed of 5

                ballDY = -ballDY; // Reverse vertical direction
                ballY = paddle.getY() - BALL_SIZE; // Place ball above the paddle
            }

            // Loss condition
            if (ballY > getHeight()) {
                inGame = false;
                timer.stop();
            }

            checkCollisions();
            repaint();
        }
    }

    private void checkCollisions() {
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j].isVisible()) {
                    if (ballRect.intersects(bricks[i][j].getBounds())) {
                        bricks[i][j].setVisible(false);
                        ballDY = -ballDY;
                        score += 10;
                    }
                }
            }
        }

        // Win condition
        boolean allBricksDestroyed = true;
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j].isVisible()) {
                    allBricksDestroyed = false;
                    break;
                }
            }
            if (!allBricksDestroyed) {
                break;
            }
        }

        if (allBricksDestroyed) {
            inGame = false;
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
        paddle.draw(g);
        drawBricks(g);
        drawHUD(g);

        if (!inGame) {
            drawEndGameMessage(g);
        }
    }

    private void drawEndGameMessage(Graphics g) {
        String msg;
        boolean allBricksDestroyed = true;
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j].isVisible()) {
                    allBricksDestroyed = false;
                    break;
                }
            }
        }

        if (allBricksDestroyed) {
            msg = "You Win!";
        } else {
            msg = "Game Over";
        }

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
                bricks[i][j].draw(g);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        // Di chuyển paddle theo vị trí chuột, trừ đi nửa chiều rộng paddle để chuột ở giữa
        int newX = mouseX - paddle.getWidth() / 2;
        // Giới hạn paddle trong khung game
        if (newX < 0) {
            newX = 0;
        }
        if (newX > getWidth() - paddle.getWidth()) {
            newX = getWidth() - paddle.getWidth();
        }
        paddle.setPosition(newX);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e); // Xử lý giống với mouseMoved
    }
}
