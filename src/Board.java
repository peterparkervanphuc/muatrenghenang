import javax.swing.*;
import java.awt.*;
import java.awt.event.*; //béo béo béo
public class Board extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private int ballX=100,ballY=100;
    private int ballDX=2,ballDY=2;
    private final int BALL_SIZE=20;
    private Paddle paddle;
    public Board() {
       setBackground(Color.white);
       setFocusable(true);
       addKeyListener(this);
       paddle =new Paddle(350,550);
       timer=new Timer(10,this);
       timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ballX=ballX+ballDX;
        ballY=ballY+ballDY;
        if(ballX<=0||ballX>=getWidth()-BALL_SIZE){
            ballDX=-ballDX;
        }
        if(ballY<=0||ballY>=getHeight()-BALL_SIZE){
            ballDY=-ballDY;
        }
        if(new Rectangle(ballX,ballY,BALL_SIZE,BALL_SIZE).intersects(paddle.getBounds())) {
            ballDX=-ballDX;
            ballDY=-ballDY;
            ballY=paddle.getY()-BALL_SIZE;
        }
        if(ballY>getHeight()){
            ballX=100;
            ballY=100;
            ballDX=2;
            ballDY=2;
        }
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
        paddle.draw(g);
    }
    @Override
    public void keyPressed(KeyEvent e){
        int key=e.getKeyCode();
        if (key==KeyEvent.VK_LEFT){
            paddle.moveLeft();
        }else if (key==KeyEvent.VK_RIGHT){
            paddle.moveRight(getWidth());
        }
    }
    @Override public void keyReleased(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}
}
