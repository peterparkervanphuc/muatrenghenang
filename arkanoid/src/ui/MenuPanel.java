package ui;

import main.ArkanoidGame;
import managers.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Main menu panel with options to start game, view high scores, or exit.
 */
public class MenuPanel extends JPanel implements KeyListener {
    private ArkanoidGame mainFrame;
    private BufferedImage logoImage;
    private BufferedImage menuBackground;
    private int selectedOption = 0;
    private String[] menuOptions = {"START GAME", "LOAD GAME", "DELETE SAVE", "HIGH SCORES", "HELP", "EXIT"};

    public MenuPanel(ArkanoidGame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        loadImages();
    }
    
    private void loadImages() {
        try {
            // Load logo from classpath
            var logoStream = getClass().getClassLoader().getResourceAsStream("Sprites/Menu/logo.png");
            if (logoStream != null) {
                logoImage = ImageIO.read(logoStream);
                logoStream.close();
            }

            // Load menu background from classpath
            var bgStream = getClass().getClassLoader().getResourceAsStream("Sprites/Menu/menu.jpg");
            if (bgStream != null) {
                menuBackground = ImageIO.read(bgStream);
                bgStream.close();
            }
        } catch (Exception e) {
            System.err.println("Could not load menu images: " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw menu background
        if (menuBackground != null) {
            g2d.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Draw logo
        if (logoImage != null) {
            int logoWidth = 400;
            int logoHeight = 150;
            int logoX = (getWidth() - logoWidth) / 2;
            int logoY = 80;
            g2d.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null);
        } else {
            // Fallback text logo
            try {
                Font titleFont = FontManager.getGameFont(48);
                g2d.setFont(titleFont);
            } catch (Exception e) {
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
            }
            
            g2d.setColor(Color.CYAN);
            String title = "ARKANOID";
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = (getWidth() - fm.stringWidth(title)) / 2;
            g2d.drawString(title, titleX, 150);
        }
        
        // Draw menu options
        try {
            Font menuFont = FontManager.getGameFont(20);
            g2d.setFont(menuFont);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
        }
        
        int startY = 300;
        int spacing = 50;
        
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW);
                // Draw selection indicator
                g2d.drawString(">", 250, startY + i * spacing);
            } else {
                g2d.setColor(Color.WHITE);
            }
            
            g2d.drawString(menuOptions[i], 300, startY + i * spacing);
        }
        
        // Draw instructions at bottom
        g2d.setColor(Color.GRAY);
        try {
            Font smallFont = FontManager.getGameFont(12);
            g2d.setFont(smallFont);
        } catch (Exception e) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        }
        
        String instructions = "USE ARROW KEYS TO SELECT - PRESS ENTER TO CONFIRM";
        FontMetrics fm = g2d.getFontMetrics();
        int instructX = (getWidth() - fm.stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instructX, 550);
        
        // Draw copyright
        g2d.setColor(Color.DARK_GRAY);
        String copyright = "© 1986 TAITO CORP.";
        int copyrightX = (getWidth() - fm.stringWidth(copyright)) / 2;
        g2d.drawString(copyright, copyrightX, 580);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_UP) {
            selectedOption--;
            if (selectedOption < 0) {
                selectedOption = menuOptions.length - 1;
            }
            repaint();
        } else if (key == KeyEvent.VK_DOWN) {
            selectedOption++;
            if (selectedOption >= menuOptions.length) {
                selectedOption = 0;
            }
            repaint();
        } else if (key == KeyEvent.VK_ENTER) {
            handleSelection();
        }
    }
    
    private void handleSelection() {
        switch (selectedOption) {
            case 0: // Start Game
                mainFrame.startGame();
                break;
            case 1: // Load Game
                mainFrame.loadGame();
                break;
            case 2: // Delete Save
                mainFrame.deleteSave();
                break;
            case 3: // High Scores
                mainFrame.showHighScores();
                break;
            case 4: // Help
                showHelp();
                break;
            case 5: // Exit
                System.exit(0);
                break;
        }
    }
    
    private void showHelp() {
        try {
            var helpStream = getClass().getClassLoader().getResourceAsStream("Sprites/Menu/Arkanoid Help.png");
            if (helpStream == null) {
                throw new Exception("Help image not found");
            }
            BufferedImage helpImage = ImageIO.read(helpStream);
            helpStream.close();

            JDialog helpDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Help", true);
            JLabel imageLabel = new JLabel(new ImageIcon(helpImage));
            helpDialog.add(imageLabel);
            helpDialog.pack();
            helpDialog.setLocationRelativeTo(this);
            helpDialog.setVisible(true);
            
        } catch (Exception e) {
            // Show text help instead
            String helpText = "ARKANOID CONTROLS:\n\n" +
                    "LEFT/RIGHT ARROWS or A/D - Move paddle\n" +
                    "SPACE - Launch ball / Fire laser\n" +
                    "ESC - Pause game\n\n" +
                    "POWERUPS:\n" +
                    "E - Enlarge paddle\n" +
                    "L - Laser cannon\n" +
                    "C - Catch ball\n" +
                    "S - Slow ball\n" +
                    "D - Duplicate balls\n" +
                    "B - Break bottom row\n" +
                    "P - Extra life\n\n" +
                    "Destroy all bricks to advance!\n" +
                    "Silver bricks require 2 hits.";
            
            JOptionPane.showMessageDialog(this, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
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
