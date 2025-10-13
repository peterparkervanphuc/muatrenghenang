import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent; //thư viện dùng để dùng space chuyển từ lobby//

public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Lobby lobbyPanel;
    private Board gamePanel; //quản lý//

    public Main(){
        setTitle("Arkanoid Game");
        setSize(800, 600);//chỉnh bé thôi//
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        //cardlayou quản lý chuyển các màn sảnh với game nhé
          cardLayout = new CardLayout();
          mainPanel = new JPanel(cardLayout);
          //tạo panel riêng biệt cho cardlayout quản lý
          lobbyPanel = new Lobby();
          gamePanel = new Board();

          mainPanel.add(lobbyPanel, "Lobby");
          mainPanel.add(gamePanel, "Game");

          //thêm panel chính vào JFrame
        add(mainPanel);
        //addKeylistener dùng để nhấn phím chuyển giữa các panel nhé t đang để space sau này dùng chuột hay gì đổi sau
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Nếu màn hình lobby đang hiển thị và người dùng nhấn SPACE...
                if (lobbyPanel.isShowing() && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // ...chuyển sang màn hình game
                    cardLayout.show(mainPanel, "GAME");
                    // Và yêu cầu game panel bắt đầu chạy
                    gamePanel.startGame();
                }
            }
        });
        setFocusable(true);//cnay để JFrame nhận Keylistener
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocusInWindow();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);

    }

}