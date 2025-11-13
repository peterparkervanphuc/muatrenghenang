package main;

import managers.ConfigManager;
import managers.SaveGameManager;
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

    public void loadGame() {
        manageSaves(false); // false = load mode
    }

    /**
     * Manage save slots - Load or Delete
     * @param deleteMode true for delete, false for load
     */
    private void manageSaves(boolean deleteMode) {
        String title = deleteMode ? "Delete Save" : "Load Game";
        String action = deleteMode ? "DELETE" : "LOAD";

        // Build save info with better formatting
        StringBuilder message = new StringBuilder();
        message.append(deleteMode ? "Select a save slot to DELETE:\n\n" : "Select a save slot to LOAD:\n\n");

        boolean hasAnySave = false;
        for (int i = 1; i <= 3; i++) {
            SaveGameManager.SaveInfo info = SaveGameManager.getInstance().getSaveInfo(i);
            if (info != null) {
                hasAnySave = true;
                message.append(String.format("Slot %d: Level %d | Score %d | Lives %d\n",
                    i, info.level(), info.score(), info.lives()));
            } else {
                message.append(String.format("Slot %d: Empty\n", i));
            }
        }

        if (deleteMode && !hasAnySave) {
            JOptionPane.showMessageDialog(this,
                "No save files to delete!",
                "Delete Save",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = {"Slot 1", "Slot 2", "Slot 3", "Cancel"};

        int choice = JOptionPane.showOptionDialog(this,
            message.toString(),
            title,
            JOptionPane.DEFAULT_OPTION,
            deleteMode ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (choice >= 0 && choice < 3) {
            int slot = choice + 1;

            if (deleteMode) {
                // Delete mode
                if (SaveGameManager.getInstance().hasSaveData(slot)) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to DELETE save in Slot " + slot + "?\n" +
                        "This action cannot be undone!",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (SaveGameManager.getInstance().deleteSave(slot)) {
                            JOptionPane.showMessageDialog(this,
                                "Save deleted successfully!",
                                "Delete Save",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                "Failed to delete save!",
                                "Delete Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Slot " + slot + " is already empty!",
                        "Delete Save",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Load mode
                if (SaveGameManager.getInstance().hasSaveData(slot)) {
                    SoundManager.getInstance().stopMenuMusic(); // Stop menu music when loading game
                    cardLayout.show(mainPanel, "GAME");
                    gamePanel.loadGame(slot);
                    gamePanel.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No save data found in slot " + slot + "!",
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Delete save files
     */
    public void deleteSave() {
        manageSaves(true); // true = delete mode
    }

    public void showHighScores() {
        showHighScores(false); // Don't play music when coming from menu
    }

    public void showHighScores(boolean playMusic) {
        cardLayout.show(mainPanel, "HIGHSCORE");
        highScorePanel.refreshScores();
        highScorePanel.requestFocusInWindow();
        if (playMusic) {
            SoundManager.getInstance().playHighScoresMusic();
        }
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
