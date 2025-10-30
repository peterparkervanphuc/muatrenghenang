import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

/**
 * Panel to display high scores
 */
public class HighScorePanel extends JPanel implements KeyListener {
    private ArkanoidGame mainFrame;
    private BufferedImage scoresBackground;
    private ArrayList<HighScoreManager.ScoreEntry> scores;
    
    public HighScorePanel(ArkanoidGame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        loadImages();
        refreshScores();
    }
    
    private void loadImages() {
        try {
            var scoresStream = getClass().getClassLoader().getResourceAsStream("Sprites/Menu/Scores.png");
            if (scoresStream != null) {
                scoresBackground = ImageIO.read(scoresStream);
                scoresStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load scores background: " + e.getMessage());
        }
    }
    
    public void refreshScores() {
        scores = HighScoreManager.loadHighScores();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background if available
        if (scoresBackground != null) {
            g2d.drawImage(scoresBackground, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Draw title
        try {
            Font titleFont = FontManager.getGameFont(36);
            g2d.setFont(titleFont);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
        }
        
        g2d.setColor(Color.YELLOW);
        String title = "HIGH SCORES";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 80);
        
        // Draw scores
        try {
            Font scoreFont = FontManager.getGameFont(20);
            g2d.setFont(scoreFont);
        } catch (Exception e) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
        }
        
        int startY = 150;
        int spacing = 35;
        
        // Draw header
        g2d.setColor(Color.CYAN);
        g2d.drawString("RANK", 200, startY);
        g2d.drawString("NAME", 320, startY);
        g2d.drawString("SCORE", 500, startY);
        
        // Draw line under header
        g2d.drawLine(180, startY + 10, 620, startY + 10);
        
        // Draw scores
        for (int i = 0; i < scores.size() && i < 10; i++) {
            HighScoreManager.ScoreEntry entry = scores.get(i);
            
            // Alternate colors for better readability
            if (i % 2 == 0) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(new Color(200, 200, 200));
            }
            
            int y = startY + 50 + i * spacing;
            
            // Rank
            String rank = String.format("%2d.", i + 1);
            g2d.drawString(rank, 200, y);
            
            // Name (truncate if too long)
            String name = entry.name;
            if (name.length() > 10) {
                name = name.substring(0, 10);
            }
            g2d.drawString(name, 320, y);
            
            // Score
            String scoreStr = String.format("%,6d", entry.score);
            g2d.drawString(scoreStr, 500, y);
        }
        
        // Draw instructions
        g2d.setColor(Color.GRAY);
        try {
            Font smallFont = FontManager.getGameFont(14);
            g2d.setFont(smallFont);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        
        String instruction = "PRESS ESC TO RETURN TO MENU";
        fm = g2d.getFontMetrics();
        int instructX = (getWidth() - fm.stringWidth(instruction)) / 2;
        g2d.drawString(instruction, instructX, 550);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            mainFrame.showMenu();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
