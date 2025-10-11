import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Arkanoid go",350,300);
    }
}
