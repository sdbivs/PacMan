import javax.swing.*;
import java.awt.*;

public class GameOver extends JPanel{
    private GameFrame gameFrame;
    private JLabel finalScoreLabel;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;

    public GameOver(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setLayout(null);
        setBackground(Color.BLACK);

        int btnWidth = 350;
        int btnHeight = 65;
        int centerX = (boardWidth / 2) - (btnWidth / 2);

        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Emulogic", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setBounds(0, 100, boardWidth, 50);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(gameOverLabel);

        finalScoreLabel = new JLabel("Final Score: 0");
        finalScoreLabel.setFont(new Font("Emulogic", Font.BOLD, 18));
        finalScoreLabel.setForeground(Color.WHITE);
        finalScoreLabel.setBounds(0, 175, boardWidth, 35);
        finalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(finalScoreLabel);


        ImageIcon restartNormal = createScaledIcon("/Buttons/restart.png", btnWidth, btnHeight);
        ImageIcon restartHover = createScaledIcon("/Buttons/restartSelected.png", btnWidth, btnHeight);
        ImageIcon menuNormal = createScaledIcon("/Buttons/mainMenuBtn.png", btnWidth, btnHeight);
        ImageIcon menuHover = createScaledIcon("/Buttons/mainSelected.png", btnWidth, btnHeight);

        JButton restartButton = new JButton(restartNormal);
        styleButton(restartButton);
        restartButton.setBounds(centerX, 250, btnWidth, btnHeight);

        if(restartHover != null){
            restartButton.setRolloverIcon(restartHover);
        }

        restartButton.addActionListener(e -> {
            gameFrame.startGame(false);
        });
        add(restartButton);

        JButton mainMenuButton = new JButton(menuNormal);
        styleButton(mainMenuButton);
        mainMenuButton.setBounds(centerX, 340, btnWidth, btnHeight);

        if(menuHover != null){
            mainMenuButton.setRolloverIcon(menuHover);
        }

        mainMenuButton.addActionListener(e -> {
            gameFrame.showMainMenu();
        });
        add(mainMenuButton);

        setPreferredSize(new Dimension(boardWidth, 21 * tileSize));
    }

    private void styleButton(JButton button){
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setFinalScore(int score){
        finalScoreLabel.setText("Final Score: " + score);
    }

    private ImageIcon createScaledIcon(String path, int width, int height){
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if(imgURL != null){
                Image rawImg = new ImageIcon(imgURL).getImage();
                Image scaledImg = rawImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        }catch(Exception e){
            System.err.println("Could not load asset: " + path);
        }
        return null;
    }
}