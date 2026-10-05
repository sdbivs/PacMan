import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame{
    private boolean multiPlayerMode;
    
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private PacMan pacman;
    private GameOver gameOver;
    private MainMenu mainMenu;

    public GameFrame(){
        FontLoader.registerCustomFont();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        pacman = new PacMan(this);
        gameOver = new GameOver(this);
        mainMenu = new MainMenu(this);

        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(pacman, "PacMan");
        mainPanel.add(gameOver, "GameOver");
        add(mainPanel);

        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        showMainMenu();
    }

    public void showMainMenu(){
        cardLayout.show(mainPanel, "MainMenu");
    }

    public void startGame(boolean multiplayer){
        multiPlayerMode = multiplayer;
        pacman.tileMap = multiplayer ? pacman.multiMap : pacman.singleMap;

        pacman.resetGame();

        cardLayout.show(mainPanel, "PacMan");
        pacman.requestFocusInWindow();
    }

    public void showGameOver(int finalScore){
        pacman.gameLoop.stop();
        gameOver.setFinalScore(finalScore);
        cardLayout.show(mainPanel, "GameOver");
    }
}