package main;

import managers.ConfigManager;
import managers.SoundManager;
import ui.GamePanel;
import ui.HighScorePanel;
import ui.MenuPanel;
import utils.GameLogger;

import javax.swing.*;
import java.awt.*;

/**
 * Main class for Arkanoid Game
 * Entry point of the application
 */
public class ArkanoidGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private HighScorePanel highScorePanel;

    public ArkanoidGame() {
        // Load configuration
        ConfigManager config = ConfigManager.getInstance();

        setTitle(config.getWindowTitle());
        setSize(config.getWindowWidth(), config.getWindowHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(config.getBoolean("window.resizable", false));
        setLocationRelativeTo(null);
        
        // Initialize CardLayout for screen switching
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        highScorePanel = new HighScorePanel(this);
        
        // Add panels to main panel
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(highScorePanel, "HIGHSCORE");
        
        add(mainPanel);
        
        // Show menu first
        showMenu();
    }
    
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
        menuPanel.requestFocusInWindow();
        SoundManager.getInstance().playMenuMusic();
    }
    
    public void startGame() {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.startNewGame();
        gamePanel.requestFocusInWindow();
    }
    
    public void showHighScores() {
        cardLayout.show(mainPanel, "HIGHSCORE");
        highScorePanel.refreshScores();
        highScorePanel.requestFocusInWindow();
    }
    
    public static void main(String[] args) {
        // Initialize logger first
        GameLogger.init();
        GameLogger.info("Starting Arkanoid Game...");

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            GameLogger.warning("Could not set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                ArkanoidGame game = new ArkanoidGame();
                game.setVisible(true);
                GameLogger.info("Game initialized successfully");
            } catch (Exception e) {
                GameLogger.error("Failed to start game", e);
                JOptionPane.showMessageDialog(null,
                    "Failed to start game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
