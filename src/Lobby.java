import javax.swing.*;
import java.awt.*;

public class Lobby extends JPanel {

    public Lobby() {
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLobbyScreen(g);
    }

    private void drawLobbyScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String title = "Arkanoid";
        String message = "Press SPACE to Start";

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Helvetica", Font.BOLD, 60));

        FontMetrics fmTitle = g2d.getFontMetrics();
        int titleWidth = fmTitle.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 50);

        g2d.setFont(new Font("Helvetica", Font.PLAIN, 24));
        FontMetrics fmMessage = g2d.getFontMetrics();
        int messageWidth = fmMessage.stringWidth(message);
        g2d.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 2 + 20);
    }
}
