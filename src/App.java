import javax.swing.SwingUtilities;

public class App{
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            GameFrame game = new GameFrame();
            game.setVisible(true);
        });
    }
}