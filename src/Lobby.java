import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Lobby extends JPanel {
    private Image backgroundImage;

    public Lobby() {
        try {
            backgroundImage = ImageIO.read(new File("assets/sanhcho.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        drawLobbyScreen(g);
    }

    private void drawLobbyScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String title = "Arkanoid";
        String message = "Press SPACE to Start";

        g2d.setColor(Color.WHITE); // Đổi màu chữ thành trắng để dễ nhìn trên nền
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
