import javax.swing.*;
public class Main extends JFrame {
    public Main(){
        setTitle("Arkanoid Game");
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}