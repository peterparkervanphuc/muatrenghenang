import javax.swing.*;
public class Main extends JFrame {
    public Main(){
        setTitle("Arkanoid Game");
        setSize(800, 600);//chỉnh bé thôi//
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        //add(new GamePanel());

        //pack();
        setLocationRelativeTo(null);
        add(new Board());
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);

    }

}//giang la thang beo//